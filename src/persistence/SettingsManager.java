package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Brent C.
 */
public enum SettingsManager {

    instance;

    private Properties properties = new Properties();

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
	    properties.store(new BrabbledFileOutputStream("config.dat"), "");
	} catch (IOException ex) {
	    return false;
	}
	return true;
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
