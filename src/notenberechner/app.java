package notenberechner;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.RowData;

public class app {
	public static ResourceBundle messages = ResourceBundle.getBundle("lang.messages"); //$NON-NLS-1$

	protected Shell shlNotenberechner;
	protected Display display = Display.getDefault();

	private Text max_points_text;
	private Text points_text;
	private Text max_points_label_out;
	private Text points_label_out;

	private static final String CONFIG_DIR = System.getProperty("user.home") + "/.notenberechner";
	private static final String PROPERTIES_FILE = CONFIG_DIR + "/config.properties";
	private static final String NOTEN_FILE = CONFIG_DIR + "/noten.properties";

	private static final String settings_lang = "main.lang";

	private static final Properties properties = new Properties();
	
	private static String ProjektVersion = null;

	static {
		// Stelle sicher, dass der Konfigurationsordner vorhanden ist
		File configDir = new File(CONFIG_DIR);
		if (!configDir.exists()) {
			configDir.mkdir();
		}

		/*
		 * configDir = new File(NOTEN_FILE); if (!configDir.exists()) {
		 * configDir.mkdir(); }
		 */
		properties.setProperty("germany.g-kurs", "97 93 90 85 80 75 70 65 60 53 47 40 33 27 20 0");
		properties.setProperty("germany.ea-kurs", "97 93 90 85 80 75 70 65 60 55 50 45 38 32 25 0");
		properties.setProperty("germany.oberstufe", "95 90 85 80 75 70 65 60 55 50 45 40 33 27 20 0");
		try (FileOutputStream fileOutputStream = new FileOutputStream(NOTEN_FILE)) {
			properties.store(fileOutputStream, null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(NOTEN_FILE);
	}
	
	static {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = null;
			try {
				model = reader.read(new FileReader("pom.xml"));
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String version = model.getVersion();
            System.out.println("Version: " + version);
            ProjektVersion = version;
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			app window = new app();
			window.open();
		} catch (Exception window_error) {
			window_error.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		try {
			FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE);
			properties.load(fileInputStream);
			String savedValue = properties.getProperty(settings_lang, "default_value");

			String[] parts = savedValue.split("_");

			if (parts.length == 2) {
				@SuppressWarnings("deprecation")
				Locale englishLocale = new Locale(parts[0], parts[1]);
				messages = ResourceBundle.getBundle("lang.messages", englishLocale);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createContents();

		MessageBox infoBox = new MessageBox(shlNotenberechner, SWT.ICON_INFORMATION | SWT.OK | SWT.CANCEL);
		infoBox.setText(messages.getString("messagesBox.info.title"));
		infoBox.setMessage(messages.getString("messagesBox.info.message.noten_values"));
		// Die Methode open gibt den Wert des vom Benutzer ausgewählten Buttons zurück
		int response = infoBox.open();

		// Reaktion auf die Benutzerantwort
		if (response == SWT.CANCEL) {
			display.dispose();
		}

		shlNotenberechner.open();
		shlNotenberechner.layout();

		while (!shlNotenberechner.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

        display.dispose();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlNotenberechner =  new Shell();
		shlNotenberechner.setSize(400, 280);
		shlNotenberechner.setText("Notenberechner");
		shlNotenberechner.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		try {
			//set window icon
	        String iconPath = "resources /res/icon.png";
	        Image icon = new Image(display, app.class.getClassLoader().getResourceAsStream(iconPath));
	        shlNotenberechner.setImage(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Composite composite = new Composite(shlNotenberechner, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		lblNewLabel.setText(messages.getString("app.lblNewLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
		RowLayout rl_composite_1 = new RowLayout(SWT.HORIZONTAL);
		composite_1.setLayout(rl_composite_1);

		
		Combo combo = new Combo(composite_1, SWT.NONE);
		RowData rd_combo = new RowData(SWT.DEFAULT, SWT.DEFAULT);
		rd_combo.exclude = true;
		combo.setLayoutData(rd_combo);
		try {
			FileInputStream fileInputStream = new FileInputStream(NOTEN_FILE);
			properties.load(fileInputStream);
		    System.out.println(properties.size());
		    
		    // Konvertieren Sie die Schlüssel in eine Liste
            List<Object> keyList = new ArrayList<>(properties.keySet());

			for (int i = 0; i < properties.size(); i++){
                String keyAtIndex = String.valueOf(keyList.get(i));
			    System.out.println("app." + keyAtIndex + ".text");
				try {
		            combo.add(messages.getString("app." + keyAtIndex + ".text"));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
			}	
		} catch (Exception e) {
		    System.out.println(e);
		}

		//butto old
		Button g_RadioButton = new Button(composite_1, SWT.RADIO);
		g_RadioButton.setText(messages.getString("app.germany.g-kurs.text"));

		Button ea_RadioButton = new Button(composite_1, SWT.RADIO);
		ea_RadioButton.setText(messages.getString("app.germany.ea-kurs.text"));

		Button h_RadioButton = new Button(composite_1, SWT.RADIO);
		h_RadioButton.setText(messages.getString("app.germany.oberstufe.text")); //$NON-NLS-1$
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new GridLayout(2, false));

		Label max_points_label = new Label(composite_2, SWT.NONE);
		max_points_label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		max_points_label.setText(messages.getString("app.max_points_label.text"));

		max_points_text = new Text(composite_2, SWT.BORDER);
		max_points_text.setFocus();
		GridData gd_max_points_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_max_points_text.widthHint = 60;
		max_points_text.setLayoutData(gd_max_points_text);

		Label points_label = new Label(composite_2, SWT.NONE);
		points_label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		points_label.setText(messages.getString("app.points_label.text"));

		points_text = new Text(composite_2, SWT.BORDER);
		points_text.setText("");
		GridData gd_points_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_points_text.widthHint = 60;
		points_text.setLayoutData(gd_points_text);

		Button btnNewButton_3 = new Button(composite, SWT.NONE);
		btnNewButton_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnNewButton_3.setText(messages.getString("app.btnNewButton_3.text"));

		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		RowLayout rl_composite_3 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_3.fill = true;
		composite_3.setLayout(rl_composite_3);

		max_points_label_out = new Text(composite_3, SWT.READ_ONLY);
		max_points_label_out.setText(messages.getString("app.max_points_label.text")); //$NON-NLS-1$

		points_label_out = new Text(composite_3, SWT.READ_ONLY);
		points_label_out.setText(messages.getString("app.points_label.text")); //$NON-NLS-1$

		Label note_label = new Label(composite, SWT.NONE);
		note_label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		note_label.setText(messages.getString("app.note_label.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Label lblNewLabel_5 = new Label(composite, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		lblNewLabel_5.setText(messages.getString("main.version.text") + ProjektVersion); //$NON-NLS-1$

		Menu menu = new Menu(shlNotenberechner, SWT.BAR);
		shlNotenberechner.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText(messages.getString("app.mntmNewSubmenu.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmSettings = new MenuItem(menu_1, SWT.NONE);
		mntmSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				settings settings = new settings(null);
				settings.open();
			}
		});
		mntmSettings.setText(messages.getString("settings.text")); //$NON-NLS-1$ //$NON-NLS-2$

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		        display.dispose();
			}
		});
		mntmExit.setText(messages.getString("app.mntmExit.text")); //$NON-NLS-1$ //$NON-NLS-2$

		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Convert String to int
				int max_points = 0;
				Double points = 0.0;
				int n_t_s = 0;

				if (g_RadioButton.getSelection()) {
					n_t_s = 1;
				}
				if (ea_RadioButton.getSelection()) {
					n_t_s = 2;
				}
				if (h_RadioButton.getSelection()) {
					n_t_s = 3;
				}

				System.out.println("Option 1 ausgewählt: " + g_RadioButton.getSelection());
				System.out.println("Option 2 ausgewählt: " + ea_RadioButton.getSelection());
				System.out.println("Option 3 ausgewählt: " + h_RadioButton.getSelection());

				if (g_RadioButton.getSelection() || ea_RadioButton.getSelection() || h_RadioButton.getSelection()) {
					try {
						max_points = Integer.parseInt(max_points_text.getText());
						String points_str = points_text.getText();

						if (points_str.contains(",")) {
							points_str = points_str.replace(",", ".");
						}
						points = Double.parseDouble(points_str);

						if (max_points >= points) {
							String note = note_brechnen(max_points, points, n_t_s);
							String points_out_str = Double.toString(points).replace(".0", "").replace(".", ",");

							max_points_label_out.setText(messages.getString("app.max_points_label.text") + max_points);
							points_label_out.setText(messages.getString("app.points_label.text") + points_out_str);
							note_label.setText(messages.getString("app.note_label.text") + note);

							points_text.setText("");
							points_text.setFocus();

							// Update with of the text
							shlNotenberechner.layout(true, true);
						} else {
							ErrorMessageBox(messages.getString("messagesBox.error.title"),
									messages.getString("messagesBox.error.message.intToHight"));
						}
					} catch (NumberFormatException e1) {
						ErrorMessageBox(messages.getString("messagesBox.error.title"),
								messages.getString("messagesBox.error.message.intConvert"));
					}
				} else {
					ErrorMessageBox(messages.getString("messagesBox.error.title"), "Select one course");
				}
			}
		});
	}

	public void restart() {
		// Aktuelles Display und Shell schließen
		display.dispose();

		// Programm neu starten
		main(null);
	}

	public void ErrorMessageBox(String title, String message) {
		MessageBox errorBox = new MessageBox(shlNotenberechner, SWT.ICON_ERROR | SWT.OK);
		errorBox.setText(title);
		errorBox.setMessage(message);
		errorBox.open();
	}

	private String note_brechnen(int max_points, Double points, int n_t_s) {
		try {
			FileInputStream fileInputStream = new FileInputStream(NOTEN_FILE);
			properties.load(fileInputStream);
			String n_t = null;
			if (n_t_s == 1) {
				n_t = properties.getProperty("germany.g-kurs", "default_value");
			} else if (n_t_s == 2) {
				n_t = properties.getProperty("germany.ea-kurs", "default_value");
			} else if (n_t_s == 3) {
				n_t = properties.getProperty("germany.oberstufe", "default_value");
			}

			String[] n_t_array = n_t.split(" ");
			int[] n_t_int = new int[n_t_array.length];
			for (int i = 0; i < n_t_array.length; i++) {
				n_t_int[i] = Integer.parseInt(n_t_array[i]);
			}

			double n_15 = max_points * n_t_int[0] / 100.0;
			double n_14 = max_points * n_t_int[1] / 100.0;
			double n_13 = max_points * n_t_int[2] / 100.0;
			double n_12 = max_points * n_t_int[3] / 100.0;
			double n_11 = max_points * n_t_int[4] / 100.0;
			double n_10 = max_points * n_t_int[5] / 100.0;
			double n_9 = max_points * n_t_int[6] / 100.0;
			double n_8 = max_points * n_t_int[7] / 100.0;
			double n_7 = max_points * n_t_int[8] / 100.0;
			double n_6 = max_points * n_t_int[9] / 100.0;
			double n_5 = max_points * n_t_int[10] / 100.0;
			double n_4 = max_points * n_t_int[11] / 100.0;
			double n_3 = max_points * n_t_int[12] / 100.0;
			double n_2 = max_points * n_t_int[13] / 100.0;
			double n_1 = max_points * n_t_int[14] / 100.0;

			int note = 0;

			if (n_15 <= points)
				note++;
			if (n_14 <= points)
				note++;
			if (n_13 <= points)
				note++;
			if (n_12 <= points)
				note++;
			if (n_11 <= points)
				note++;
			if (n_10 <= points)
				note++;
			if (n_9 <= points)
				note++;
			if (n_8 <= points)
				note++;
			if (n_7 <= points)
				note++;
			if (n_6 <= points)
				note++;
			if (n_5 <= points)
				note++;
			if (n_4 <= points)
				note++;
			if (n_3 <= points)
				note++;
			if (n_2 <= points)
				note++;
			if (n_1 <= points)
				note++;

			if (note > 9) {
				return (String.valueOf(note));
			} else {
				return ("0" + note);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ("Fehler");
		}
	}
}
