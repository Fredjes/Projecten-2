package domain;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 *
 * @author Frederik
 */
@Entity
@NamedQuery(name = "Setting.findAll", query = "SELECT s FROM Setting s")
public class Setting {

    public enum SettingType {

	EXTENSION_COUNT_BOOK(3), EXTENSION_COUNT_CD_DVD(2), EXTENSION_COUNT_GAME(2),
	EXTENSION_COUNT_STORYBAG(1), DAY_COUNT_LOAN(14), DAY_COUNT_LOAN_EXTENSION(7),
	LOAN_COUNT_BOOK(3), LOAN_COUNT_CD_DVD(2), LOAN_COUNT_GAME(1), LOAN_COUNT_STORYBAG(1),
	ADMIN_PASSWORD("admin");

	private String defaultValue;

	private SettingType(String defaultValue) {
	    try {
		this.defaultValue = new String(MessageDigest.getInstance("SHA-512").digest(defaultValue.getBytes(Charset.forName("UTF-8"))));
	    } catch (NoSuchAlgorithmException ex) {
	    }
	}

	private SettingType(int defaultValue) {
	    this.defaultValue = String.valueOf(defaultValue);
	}

	public int getDefaultValue() {
	    return Integer.parseInt(defaultValue);
	}

	public String getDefaultValueString() {
	    return defaultValue;
	}
    }

    @Id
    @Enumerated(EnumType.STRING)
    private SettingType id;

    private String settingValue;

    public Setting() {
    }

    public Setting(SettingType id, String settingValue) {
	this.id = id;
	this.settingValue = settingValue;
    }
    
    public Setting(SettingType key, int value) {
	this.id = key;
	this.settingValue = String.valueOf(value);
    }

    public SettingType getKey() {
	return id;
    }

    public void setKey(SettingType key) {
	this.id = key;
    }

    public int getValue() {
	return Integer.parseInt(settingValue);
    }

    public void setValue(int value) {
	this.settingValue = String.valueOf(value);
    }

    public String getValueString() {
	return settingValue;
    }

    public void setValue(String value) {
	this.settingValue = value;
    }

    @Override
    public String toString() {
	return getKey() + ": " + getValue();
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 89 * hash + Objects.hashCode(this.id);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Setting other = (Setting) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }
}
