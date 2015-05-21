package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import persistence.UserRepository;

@Entity
@Access(AccessType.PROPERTY)
@Table(name = "TBL_USER")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
})
public class User implements Serializable, Searchable, Importable {

    private int id;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty classRoom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty registerNumber = new SimpleStringProperty();
    private final ObjectProperty<UserType> userType = new SimpleObjectProperty<>();
    private final ObservableList<Loan> loans = FXCollections.observableArrayList();
    private final BooleanProperty visible = new SimpleBooleanProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
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

    public User() {
    }

    public User(UserType userType) {
	this.setUserType(userType);
    }

    public User(String name, String classroom, String email, UserType userType, String passwordHash) {
	this.name.set(name);
	this.classRoom.set(classroom);
	this.email.set(email);
	this.userType.set(userType);
	this.passwordHash = passwordHash;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<Loan> getLoans() {
	return this.loans;
    }

    public void setLoans(List<Loan> loans) {
	this.loans.setAll(loans);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
	return id;
    }

    protected void setId(int id) {
	this.id = id;
    }

    public String getPasswordHash() {
	return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }
    
    public String getAddress(){
	return this.address.get();
    }
    
    public void setAddress(String address){
	this.address.set(address);
    }

    public UserType getUserType() {
	return userType.get();
    }

    public void setUserType(UserType userType) {
	this.userType.set(userType);
    }

    public String getName() {
	return name.get() + (lastName.get() == null ? "" : " " + lastName.get());
    }

    public void setName(String name) {
	this.name.set(name);
    }

    public void setLastName(String name) {
	lastName.set(name);
    }

    public User(int id, String passwordHash) {
	this.id = id;
	this.passwordHash = passwordHash;
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

    public String getRegisterNumber() {
	return registerNumber.get();
    }

    public void setRegisterNumber(String number) {
	this.registerNumber.set(number);
    }

    public boolean getVisible() {
	return visible.get();
    }

    public void setVisible(boolean visible) {
	this.visible.set(visible);
    }

    /*
     * Property
     */
    public StringProperty nameProperty() {
	return name;
    }
    
    public StringProperty addressProperty(){
	return address;
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

    public StringProperty registerNumberProperty() {
	return registerNumber;
    }

    public BooleanProperty visibleProperty() {
	return visible;
    }

    @Override
    public String toString() {
	return this.getName();
    }

    @Override
    public boolean test(String query) {
	if (!getVisible()) {
	    return false;
	}

	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getClassRoom(), t)
		    || SearchPredicate.containsIgnoreCase(getEmail(), t)
		    || SearchPredicate.containsIgnoreCase(getName(), t)
		    || (getUserType() != null && SearchPredicate.containsIgnoreCase(getUserType().toString(), t));

	    if (!temp) {
		return false;
	    }
	}

	return true;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 89 * hash + this.id;
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
	final User other = (User) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }

    @Override
    public Importer createImporter() {
	return new UserImporter();
    }

    private class UserImporter implements Importer {

	private final Set<String> fieldSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new String[]{
	    "", "Klas", "E-mail", "Voornaam/Naam", "Achternaam", "Wachtwoord", "Stamboeknummer", "Adres"
	})));

	private List<String> headerList = new ArrayList<>();
	private List<User> entityList = new ArrayList<>();

	public UserImporter() {
	    nextEntity();
	}

	@Override
	public Set<String> getFields() {
	    return fieldSet;
	}

	@Override
	public void initHeaders(List<String> headers) {
	    headerList.clear();
	    headerList.addAll(headers);
	}

	@Override
	public String predictField(String t) {
	    if (SearchPredicate.containsAnyIgnoreCase(t, "klas", "lokaal")) {
		return "Klas";
	    } else if (SearchPredicate.containsIgnoreCase(t, "mail")) {
		return "E-mail";
	    } else if (SearchPredicate.containsIgnoreCase(t, "voornaam")) {
		return "Voornaam/Naam";
	    } else if (SearchPredicate.containsIgnoreCase(t, "achternaam")) {
		return "Achternaam";
	    } else if (SearchPredicate.containsIgnoreCase(t, "naam")) {
		if (headerList.stream().anyMatch(v -> SearchPredicate.containsIgnoreCase(v, "voornaam"))) {
		    return "Achternaam";
		} else {
		    return "Voornaam/Naam";
		}
	    } else if (SearchPredicate.containsAnyIgnoreCase(t, "wachtwoord", "paswoord", "passwoord", "password")) {
		return "Wachtwoord";
	    } else if (SearchPredicate.containsAnyIgnoreCase(t, "stamboeknummer", "register", "id", "nummer")) {
		return "Stamboeknummer";
	    } else if (SearchPredicate.containsAnyIgnoreCase(t, "adres", "locatie")){
		return "Adres";
	    } else {
		return "";
	    }
	}

	@Override
	public void setField(String field, String value) {
	    switch (field) {
		case "Klas":
		    getCurrentEntity().setClassRoom(value);
		    break;
		case "E-mail":
		    getCurrentEntity().setEmail(value);
		    break;
		case "Voornaam/Naam":
		    getCurrentEntity().setName(value);
		    break;
		case "Achternaam":
		    getCurrentEntity().setLastName(value);
		    break;
		case "Wachtwoord":
		    getCurrentEntity().setPasswordHash(UserRepository.getInstance().generatePasswordHash(value));
		    break;
		case "Adres":
		    getCurrentEntity().setAddress(value);
		    break;
		case "Stamboeknummer":
		    getCurrentEntity().setRegisterNumber(value);
		    break;
	    }
	}

	private User getCurrentEntity() {
	    return entityList.get(entityList.size() - 1);
	}

	@Override
	public void nextEntity() {
	    User user = new User(UserType.STUDENT);
	    user.setVisible(true);
	    entityList.add(user);
	}

	@Override
	public void persistEntities() {
	    entityList.stream().filter(u -> u.getName() != null && !u.getName().equals("null")).forEach(UserRepository.getInstance()::addOrUpdate);
	}
    }
}
