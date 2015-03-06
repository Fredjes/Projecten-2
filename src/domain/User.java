package domain;

import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Access (AccessType.PROPERTY)
@Table (name = "TBL_USER")
@NamedQueries ({
    @NamedQuery (name = "User.findAll", query = "SELECT u FROM User u")
})
public class User implements Serializable, Searchable {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Access (AccessType.FIELD)
    private int id;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty classRoom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final ObjectProperty<UserType> userType = new SimpleObjectProperty<>();
    private String passwordHash;

    public static enum UserType {

	TEACHER("Leerkracht"), VOLUNTEER("Vrijwilliger"), STUDENT("Leerling");

	private final String translation;

	private UserType(String translation) {
	    this.translation = translation;
	}

	@Override
	public String toString() {
	    return translation;
	}
    }

    public int getId() {
	return id;
    }

    public String getPasswordHash() {
	return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }

    public UserType getUserType() {
	return userType.get();
    }

    public void setUserType(UserType userType) {
	this.userType.set(userType);
    }

    public String getName() {
	return name.get();
    }

    public void setName(String name) {
	this.name.set(name);
    }

    public String getClassRoom() {
	return classRoom.get();
    }

    public void setClassRoom(String classRoom) {
	this.classRoom.set(classRoom);
    }

    public String getEmail() {
	return email.get();
    }

    public void setEmail(String email) {
	this.email.set(email);
    }

    /*
     * Property
     */
    public StringProperty nameProperty() {
	return name;
    }

    public StringProperty classRoomProperty() {
	return classRoom;
    }

    public ObjectProperty<UserType> userTypeProperty() {
	return userType;
    }

    public StringProperty emailProperty() {
	return email;
    }

    @Override
    public boolean test(String query) {
	for (String t : query.split("\\s*")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getClassRoom(), t)
		    || SearchPredicate.containsIgnoreCase(getEmail(), t)
		    || SearchPredicate.containsIgnoreCase(getName(), t)
		    || SearchPredicate.containsIgnoreCase(getUserType().toString(), t);

	    if (temp) {
		return true;
	    }
	}

	return false;
    }
}
