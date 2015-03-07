package persistence;

import domain.User;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

/**
 *
 * @author Brent
 */
public class UserRepository {

    private static UserRepository INSTANCE;

    private ObservableList<User> users = FXCollections.observableArrayList();

    private User authenticatedUser;

    private final List<User> deletedUsers = new ArrayList();

    private UserRepository() {
        sync();
    }

    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    public void add(User u) {
        if (users.stream().anyMatch(uu -> uu.getName().equalsIgnoreCase(u.getName()))) {
            return;
        }
        users.add(u);
    }

    public void remove(User u) {
        users.remove(u);
        deletedUsers.add(u);
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

        return null;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public boolean authenticate(String username, String password) {
        List<User> found = getUsersByPredicate((u) -> {
            return u.getName().equalsIgnoreCase(username.trim()) && u.getPasswordHash().equals(generatePasswordHash(password.trim()));
        });

        if (!found.isEmpty()) {
            authenticatedUser = found.get(0);
            return true;
        }
        return false;
    }

    public void sync() {
        users.clear();
        users.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("User.findAll", User.class).getResultList());
    }

    public ObservableList<User> getUsersByPredicate(Predicate<User> predicate) {
        ObservableList<User> filteredList = users.filtered(predicate);
        return filteredList;
    }

    public void saveChanges() {
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
        } catch (RollbackException ex) {
            System.err.println("Could not save changes for Users to the database. " + ex.getMessage());
            manager.getTransaction().rollback();
        }
        manager.close();
    }

}
