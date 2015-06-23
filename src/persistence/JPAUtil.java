package persistence;

import gui.dialogs.PopupUtil;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton containing convenience-methods for JPA-handling.
 *
 * @author Brent
 */
public class JPAUtil {

    private EntityManagerFactory entityManagerFactory;
    private static JPAUtil INSTANCE;

    private JPAUtil() {

    }

    public static JPAUtil getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JPAUtil();
	}
	return INSTANCE;
    }

    /**
     * Factory-method returning an {@link EntityManager}. Depending on an efficient {@link EntityManagerFactory#createEntityManager() } implementation, for example object reuse.
     *
     * @return An {@link EntityManager} connected to the projects database connection.
     */
    public EntityManager getEntityManager() {
	return getEntityManagerFactory().createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
	if (entityManagerFactory == null) {
	    try {
		Map<String, String> map = new HashMap<>();
		map.put("javax.persistence.jdbc.url", "jdbc:mysql://" + SettingsManager.INSTANCE.getString("host") + ":" + SettingsManager.INSTANCE.getString("port") + "/" + SettingsManager.INSTANCE.getString("schema") + "?zeroDateTimeBehavior=convertToNull");
		map.put("javax.persistence.jdbc.user", SettingsManager.INSTANCE.getString("username"));
		map.put("javax.persistence.jdbc.password", SettingsManager.INSTANCE.getString("password"));

		entityManagerFactory = Persistence.createEntityManagerFactory("Projecten_2PU", map);
	    } catch (Exception e) {
		e.printStackTrace();
		Platform.runLater(() -> {
		    PopupUtil.showNotification("Geen database gevonden!", "Er kon geen verbinding worden gemaakt met de database.", PopupUtil.Notification.ERROR, Duration.INDEFINITE);
		});
	    }
	}
	return entityManagerFactory;
    }
}
