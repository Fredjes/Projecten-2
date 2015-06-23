package domain;

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
	
	EXTENSION_COUNT_BOOK(3), EXTENSION_COUNT_CD_DVD(2), EXTENSION_COUNT_GAME(2), EXTENSION_COUNT_STORYBAG(1), DAY_COUNT_LOAN(14), DAY_COUNT_LOAN_EXTENSION(7), LOAN_COUNT_BOOK(3), LOAN_COUNT_CD_DVD(2), LOAN_COUNT_GAME(1), LOAN_COUNT_STORYBAG(1);
	
	private int defaultValue;

	private SettingType(int defaultValue) {
	    this.defaultValue = defaultValue;
	}

	public int getDefaultValue() {
	    return defaultValue;
	}
    }

    @Id
    @Enumerated(EnumType.STRING)
    private SettingType id;
    
    private int settingValue;

    public Setting() {
    }

    public Setting(SettingType key, int value) {
	this.id = key;
	this.settingValue = value;
    }

    public SettingType getKey() {
	return id;
    }

    public void setKey(SettingType key) {
	this.id = key;
    }

    public int getValue() {
	return settingValue;
    }

    public void setValue(int value) {
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