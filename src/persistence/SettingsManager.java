package persistence;

import domain.Setting;
import domain.Setting.SettingType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 * Manager used to load and write settings.
 *
 * @author Brent C.
 */
public enum SettingsManager {

    INSTANCE;

    private Properties properties = new Properties();
    private Set<Setting> settings = new HashSet<>();

    private SettingsManager() {
	try {
	    if (!new File("config.dat").exists()) {
		properties.put("host", "127.0.0.1");
		properties.put("port", "3306");
		properties.put("schema", "krekelschool");
		properties.put("username", "projecten");
		properties.put("password", "wachtwoord123");
		properties.put("pdfPath", System.getProperty("user.home") + "\\Krekelschool");
		properties.store(new BrabbledFileOutputStream("config.dat"), "");
	    }
	    properties.load(new BrabbledFileInputStream("config.dat"));

	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    public String getString(String key) {
	return String.valueOf(properties.getOrDefault(key, null));
    }

    public void setString(String key, String value) {
	properties.put(key, value);
    }

    public boolean save() {
	try {
	    saveSettingsToDb();
	    properties.store(new BrabbledFileOutputStream("config.dat"), "");
	} catch (IOException ex) {
	    return false;
	}
	return true;
    }

    public void setSetting(SettingType type, String value) {
	ensureLoadedSettings();
	
	try {
	    int intValue = Integer.parseInt(value);
	    settings.stream().filter(s -> s.getKey() == type).forEach(s -> s.setValue(intValue));
	} catch (NumberFormatException ex) {
	    int intValue = type.getDefaultValue();
	    settings.stream().filter(s -> s.getKey() == type).forEach(s -> s.setValue(intValue));
	}
    }
    
    private void ensureLoadedSettings() {
	if (settings.isEmpty()) {
	    List<Setting> settingsList = JPAUtil.getInstance().getEntityManager().createNamedQuery("Setting.findAll", Setting.class).getResultList();
	    settingsList.forEach(settings::add);
	}
    }

    private void saveSettingsToDb() {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	List<Setting> dbSettings = JPAUtil.getInstance().getEntityManager().createNamedQuery("Setting.findAll", Setting.class).getResultList();
	settings.stream().filter(dbSettings::contains).forEach(manager::merge);
	settings.stream().filter(s -> !dbSettings.contains(s)).forEach(manager::persist);
	manager.getTransaction().commit();
    }

    public int getSettingValue(SettingType type) {
	ensureLoadedSettings();
	Setting setting = settings.stream().filter(s -> s.getKey() == type).findAny().orElse(new Setting(type, type.getDefaultValue()));
	settings.add(setting);
	return setting.getValue();
    }

    private class BrabbledFileInputStream extends FileInputStream {

	public BrabbledFileInputStream(String name) throws FileNotFoundException {
	    super(name);
	}

	@Override
	public int read() throws IOException {
	    int b = super.read();
	    return b - 10;
	}

	@Override
	public int read(byte[] b) throws IOException {
	    byte[] bb = new byte[b.length];
	    int result = super.read(bb);

	    for (int i = 0; i < bb.length; i++) {
		b[i] = (byte) (bb[i] - 10);
	    }
	    return result;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
	    byte[] bb = new byte[b.length];
	    int result = super.read(bb, off, len);

	    for (int i = 0; i < bb.length; i++) {
		b[i] = (byte) (bb[i] - 10);
	    }
	    return result;
	}

    }

    private class BrabbledFileOutputStream extends FileOutputStream {

	public BrabbledFileOutputStream(String name) throws FileNotFoundException {
	    super(name);
	}

	@Override
	public void write(int b) throws IOException {
	    super.write(b + 10);
	}

	@Override
	public void write(byte[] b) throws IOException {
	    byte[] bb = new byte[b.length];
	    for (int i = 0; i < b.length; i++) {
		bb[i] = (byte) (b[i] + 10);
	    }
	    super.write(bb);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
	    byte[] bb = new byte[b.length];
	    for (int i = 0; i < b.length; i++) {
		bb[i] = (byte) (b[i] + 10);
	    }
	    super.write(bb, off, len);
	}

    }
}
