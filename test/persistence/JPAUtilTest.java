package persistence;

import javax.persistence.EntityManager;
import org.junit.Test;

/**
 *
 * @author Brent
 */
public class JPAUtilTest {

    public JPAUtilTest() {
    }

    @Test
    public void testGetEntityManager() {
	EntityManager em = JPAUtil.getInstance().getEntityManager();
	assert em != null;
    }

}
