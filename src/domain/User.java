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

@Entity
@Access(AccessType.PROPERTY)
public class User implements Serializable {

    private int id;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty classRoom = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private ObjectProperty<UserType> userType = new SimpleObjectProperty<>();

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
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
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

    /* Property getters & setters */
    public StringProperty getNameProperty() {
        return name;
    }

    public void setNameProperty(StringProperty name) {
        this.name = name;
    }

    public StringProperty getClassRoomProperty() {
        return classRoom;
    }

    public void setClassRoomProperty(StringProperty classRoom) {
        this.classRoom = classRoom;
    }

    public ObjectProperty<UserType> getUserTypeProperty() {
        return userType;
    }

    public void setUserTypeProperty(ObjectProperty<UserType> userType) {
        this.userType = userType;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public void setEmailProperty(StringProperty email) {
        this.email = email;
    }

}
