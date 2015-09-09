package persistence;

import domain.PdfExporter;
import domain.User;
import gui.MainApp;
import gui.ScreenSwitcher;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

/**
 * Repository used to manage {@link User}s.
 *
 * @author Brent
 */
public class UserRepository extends Repository<User> {

    private static UserRepository INSTANCE;

    private ObservableList<User> users = FXCollections.observableArrayList();

    private final ObjectProperty<User> authenticatedUser = new SimpleObjectProperty<>();

    private final List<User> deletedUsers = new ArrayList();

    private final User ADMIN_USER = new User(Integer.MIN_VALUE, "Admin", "", "", "", User.UserType.TEACHER, "");

    private UserRepository() {
	super();
    }

    public static UserRepository getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new UserRepository();
	}
	return INSTANCE;
    }

    public boolean isAdminUser(User user) {
	return ADMIN_USER.equals(user);
    }

    public void add(User u) {
	if (users.stream().anyMatch(uu -> uu.getName().equalsIgnoreCase(u.getName()))) {
	    return;
	}

	users.add(u);
    }

    public void addOrUpdate(User u) {
	final BooleanProperty foundResults = new SimpleBooleanProperty(false);
	getUsers().stream().filter(user -> user != null && user.getRegisterNumber() != null && user.getRegisterNumber().equals(u.getRegisterNumber())).forEach(user -> {
	    foundResults.set(true);
	    user.setClassRoom(u.getClassRoom());
	    user.setEmail(u.getEmail());
	    user.setFirstName(u.getFirstName());
	    user.setLastName(u.getLastName());
	    user.setVisible(true);
	});

	if (!foundResults.get()) {
	    add(u);
	}
    }

    public void remove(User u) {
	u.setVisible(false);
    }

    public ObservableList<User> getUsers() {
	return users;
    }

    public User getUser(String name) {
	return users.stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String generatePasswordHash(String password) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA");
	    md.update(password.getBytes());
	    return new BigInteger(1, md.digest()).toString(16);
	} catch (NoSuchAlgorithmException ex) {
	    Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
	}

	throw new IllegalStateException();
    }

    public boolean validatePassword(String password) throws IllegalArgumentException {
	if (password.length() < 7) {
	    throw new IllegalArgumentException("Het wachtwoord moet minstens 7 tekens lang zijn.");
	}

	boolean[] characters = {
	    password.matches(".*[a-z]+.*"), // must contain at least one lower case character
	    password.matches(".*[A-Z]+.*"), // must contain at least one upper case character
	    password.matches(".*\\d+.*"), // must contain at least one digit
	    !password.matches("\\w*") // must contain at least one special character
	};

	if (!characters[0]) {
	    throw new IllegalArgumentException("Het wachtwoord moet een kleine letter bevatten.");
	} else if (!characters[1]) {
	    throw new IllegalArgumentException("Het wachtwoord moet een hoofdletter bevatten.");
	} else if (!characters[2]) {
	    throw new IllegalArgumentException("Het wachtwoord moet een cijfer bevatten.");
	} else if (!characters[3]) {
	    throw new IllegalArgumentException("Het wachtwoord moet een speciaal teken bevatten.");
	}

	return true;
    }

    public User getAuthenticatedUser() {
	return authenticatedUser.get();
    }

    public ObjectProperty<User> authenticatedUserProperty() {
	return authenticatedUser;
    }

    public void logout() {
	authenticatedUser.set(null);
    }

    public boolean authenticate(String username, String password) {
	return authenticate(username, password, true);
    }

    public boolean authenticate(String username, String password, boolean updateAuth) {
	if (MainApp.DEVELOPMENT_MODE) {
	    authenticatedUser.set(ADMIN_USER);
	    return true;
	}

	try {
	    if (username.trim().equalsIgnoreCase("Admin") && new String(MessageDigest.getInstance("SHA-512").digest(password.getBytes())).equals(MainApp.ADMIN_PASSWORD)) {
		ScreenSwitcher.LOCAL_ADMIN = true;
		authenticatedUser.set(ADMIN_USER);
		return true;
	    }
	} catch (NoSuchAlgorithmException ex) {
	    Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
	}

	final String fUsername = username == null ? "" : username;
	final String fPassword = password == null ? "" : password;
	final String hash = generatePasswordHash(fPassword);
	
	List<User> found = getUsersByPredicate((u) -> u.getName() != null && u.getName().trim().equalsIgnoreCase(fUsername.trim()) && hash.equals(u.getPasswordHash()));

	if (!found.isEmpty()) {
	    assert found.size() == 1;
	    if (updateAuth) {
		authenticatedUser.set(found.get(0));
	    }
	    return true;
	}
	return false;
    }

    public void sync() {
	Thread t = new Thread(() -> {
	    synchronized (this) {
		loaded.set(false);
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		long count = (long) manager.createQuery("SELECT COUNT(u) FROM User u").getSingleResult();
		users.clear();
		for (int i = 0; i < count; i += Repository.SYNC_BULK_STEP_COUNT) {
		    int size = (int) Math.min(Repository.SYNC_BULK_STEP_COUNT, count - i);
		    TypedQuery<User> query = manager.createNamedQuery("User.findAll", User.class);
		    query.setMaxResults(size);
		    query.setFirstResult(i);
		    users.addAll(query.getResultList());
		}
//		users.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("User.findAll", User.class).getResultList());
		users.stream().filter(u -> u.getName() == null || u.getName().isEmpty() || u.getName().equals("null")).forEach(this::remove);
		saveChangesAfterSync();
		Logger.getLogger("Notification").log(Level.INFO, "Synchronized user repository with database");
		super.triggerListeners();
		try {
		    PdfExporter.saveUsers();
		} catch (IOException ex) {
		}
	    }
	});

	t.setName("User repository sync thread");
	t.start();
    }

    public ObservableList<User> getUsersByPredicate(Predicate<User> predicate) {
	ObservableList<User> filteredList = users.filtered(predicate);
	return filteredList;
    }

    private void saveChangesAfterSync() {
	Runnable r = () -> {
	    synchronized (this) {
		loaded.set(false);
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		try {
		    manager.getTransaction().begin();

		    users.forEach(user -> {
			if (user.getId() == 0) {
			    manager.persist(user);
			} else {
			    manager.merge(user);
			}
		    });

		    deletedUsers.forEach(u -> {
			Object o = manager.merge(u);
			manager.remove(o);
		    });

		    manager.getTransaction().commit();
		    deletedUsers.clear();
		    loaded.set(true);
		} catch (RollbackException ex) {
		    System.err.println("Could not save changes for Users to the database. " + ex.getMessage());
		    manager.getTransaction().rollback();
		}
	    }
	};

	Thread t = new Thread(r);
	t.setName("User repository change thread");
	t.start();
    }

    public void saveChanges() {
	Runnable r = () -> {
	    synchronized (this) {
		loaded.set(false);
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		try {
		    manager.getTransaction().begin();

		    users.forEach(user -> {
			if (user.getName() == null || user.getName().trim().isEmpty()) {
			    return;
			}

			if (user.getId() == 0) {
			    manager.persist(user);
			} else {
			    manager.merge(user);
			}
		    });

		    deletedUsers.forEach(u -> {
			Object o = manager.merge(u);
			manager.remove(o);
		    });

		    manager.getTransaction().commit();
		    deletedUsers.clear();

		    UserRepository.getInstance().sync();
		} catch (RollbackException ex) {
		    System.err.println("Could not save changes for Users to the database. " + ex.getMessage());
		    manager.getTransaction().rollback();
		}
	    }
	};

	Thread t = new Thread(r);
	t.setName("User repository change thread");
	t.start();
    }

    public void saveUser(User user) {
	if (!getUsers().contains(user)) {
	    getUsers().add(user);
	}

	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();

	if (user.getId() == 0) {
	    manager.persist(user);
	} else {
	    manager.merge(user);
	}

	manager.getTransaction().commit();
    }

    public static void main(String[] args) {
	User frederik = new User("Frederik", "De Smedt", "2A", "frederik.de.smedt@hotmail.com", User.UserType.TEACHER, getInstance().generatePasswordHash("frederik"));
	User brent = new User("Brent", "Couck", "2A", "bla@bla.com", User.UserType.VOLUNTEER, getInstance().generatePasswordHash("brent"));
	User pieterjan = new User("Pieter-Jan", "Geeroms", "2A", "pieterjangeeroms@hotmail.com", User.UserType.STUDENT, getInstance().generatePasswordHash("pj"));
	getInstance().getUsers().addAll(frederik, brent, pieterjan);
	getInstance().saveChanges();
    }

    private BooleanProperty loaded = new SimpleBooleanProperty(false);
    
    @Override
    public BooleanProperty isLoaded() {
	return loaded;
    }
}
