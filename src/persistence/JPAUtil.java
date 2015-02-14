package persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Brent
 */
public class JPAUtil {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Projecten_2PU");
    private static JPAUtil INSTANCE;

    private JPAUtil() {

    }

    public static JPAUtil getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JPAUtil();
	}
	return INSTANCE;
    }

    public EntityManager getEntityManager() {
	return getEntityManagerFactory().createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
	return entityManagerFactory;
    }
}
