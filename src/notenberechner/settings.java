package notenberechner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class settings extends Shell {
	//private static ResourceBundle messages = ResourceBundle.getBundle("notenberechner.messages"); //$NON-NLS-1$
	private static ResourceBundle messages = app.messages; //$NON-NLS-1$
    
    private boolean restart = false;

    private static final Map<String, String> COUNTRY_MAP = new HashMap<>();
    
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.notenberechner";
    private static final String PROPERTIES_FILE = CONFIG_DIR + "/config.properties";
	private static final String NOTEN_FILE = CONFIG_DIR + "/noten.properties";

    private static final String settings_lang = "main.lang";

    private Table table;
    
	private static final Properties properties = new Properties();

    static {
        // Mapping of country names to country codes
        COUNTRY_MAP.put("USA", "en_US");
        COUNTRY_MAP.put("Germany", "de_DE");
    }
    
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			settings shell = new settings(display);
	        
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public settings(Display display) {
		super(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		setLayout(new FillLayout(SWT.HORIZONTAL));
        
    	messages = app.messages; //$NON-NLS-1$
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText(messages.getString("settings.Tap_Display.text")); //$NON-NLS-1$
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblAnzeige = new Label(composite, SWT.NONE);
		lblAnzeige.setText(messages.getString("settings.Tap_Display.text")); //$NON-NLS-2$ //$NON-NLS-1$
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText(messages.getString("settings.lblNewLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Combo language_combo = new Combo(composite, SWT.NONE);
		language_combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				restart = true;
			}
		});
		language_combo.setItems(COUNTRY_MAP.keySet().toArray(new String[0]));
		language_combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		language_combo.setText(messages.getString("settings.combo.text")); //$NON-NLS-1$ //$NON-NLS-2$
		new Label(composite, SWT.NONE);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new RowLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1));
		
		Button save_settings = new Button(composite_1, SWT.NONE);
		save_settings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        properties.setProperty(settings_lang, getCountryCodeFromName(language_combo.getText()));
		        try (FileOutputStream fileOutputStream = new FileOutputStream(PROPERTIES_FILE)) {
		            properties.store(fileOutputStream, null);
		        } catch (IOException e1) {
		            e1.printStackTrace();
		        }
				
				if (restart) {
					MessageBox infoBox = new MessageBox(getShell(), SWT.ICON_INFORMATION  | SWT.OK | SWT.CANCEL);
			        infoBox.setText(messages.getString("messagesBox.info.title"));
			        infoBox.setMessage(messages.getString("messagesBox.info.message.restart_programm"));
			        // Die Methode open gibt den Wert des vom Benutzer ausgewählten Buttons zurück
			        int response = infoBox.open();
			        
			        // Reaktion auf die Benutzerantwort
			        if (response == SWT.OK) {
						close();
		                app restart = new app();
		                restart.restart();
					} else {
						close();
					}
				} else {
					close();
				}
			}
		});
		save_settings.setText(messages.getString("settings.btnNewButton.text")); //$NON-NLS-1$
		
		Button close_settings = new Button(composite_1, SWT.NONE);
		close_settings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		close_settings.setText(messages.getString("settings.btnNewButton_1.text"));
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText(messages.getString("settings.Tap_Data.text")); //$NON-NLS-1$
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite_2);
		composite_2.setLayout(new GridLayout(1, false));
		
		Label lblNewLabel_1 = new Label(composite_2, SWT.NONE);
		lblNewLabel_1.setText("Data (Germany)"); //$NON-NLS-1$
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(120);
		tblclmnNewColumn.setText(messages.getString("settings.Data.Column.text"));
	
		for (int i = 0; i < 16; i++) {
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setWidth(30);
			if (i > 9) {
    			tableColumn.setText(String.valueOf(i)); //$NON-NLS-1$
            } else {
    			tableColumn.setText("0" + i); //$NON-NLS-1$
            }
		}
		
		try {	
			FileInputStream fileInputStream = new FileInputStream(NOTEN_FILE);
			properties.load(fileInputStream);
		    System.out.println(properties.size());
		    
		    // Konvertieren Sie die Schlüssel in eine Liste
            List<Object> keyList = new ArrayList<>(properties.keySet());

			for (int i = 0; i < properties.size(); i++){
                String keyAtIndex = String.valueOf(keyList.get(i));
			    
				String noten = properties.getProperty(keyAtIndex, "default_value");
				String[] array = noten.split(" ");
		    
	            TableItem item = new TableItem(table, SWT.NONE);
	            item.setText(0, keyAtIndex);
				for (int j = 0; j < array.length; j++) {
		            item.setText(j + 1, String.valueOf(array[j]));
				}
			}			
		} catch (Exception e2) {
			// TODO: handle exception
		}
        
        /*TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt) && i != 0) {
                            final int column = i;
                            final Text text = new Text(table, SWT.NONE);
                            Listener textListener = new Listener() {
                                @Override
                                public void handleEvent(final Event e) {
                                    switch (e.type) {
                                        case SWT.FocusOut:
                                            item.setText(column, text.getText());
                                            text.dispose();
                                            break;
                                        case SWT.Traverse:
                                            switch (e.detail) {
                                                case SWT.TRAVERSE_RETURN:
                                                    item.setText(column, text.getText());
                                                    // Fall durchlaufen
                                                case SWT.TRAVERSE_ESCAPE:
                                                    text.dispose();
                                                    e.doit = false;
                                            }
                                            break;
                                    }
                                }
                            };
                            text.addListener(SWT.FocusOut, textListener);
                            text.addListener(SWT.Traverse, textListener);
                            editor.setEditor(text, item, i);
                            text.setText(item.getText(i));
                            text.selectAll();
                            text.setFocus();
                            return;
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible)
                        return;
                    index++;
                }
            }
        });*/
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText(messages.getString("settings.text")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
		setSize(550, 400);

	}

    private static String getCountryCodeFromName(String countryName) {
        return COUNTRY_MAP.get(countryName);
    }

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
