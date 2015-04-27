package domain;

import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import persistence.ItemRepository;
import persistence.JPAUtil;
import persistence.Repository;

/**
 *
 * @author Frederik
 */
public class ChangeRepository extends Repository<Change> {

    @Override
    public void add(Change c) {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	manager.persist(c);
	manager.getTransaction().commit();
	manager.close();
	sync();
    }

    @Override
    public void remove(Change c) {
	sync();
    }

    @Override
    public void saveChanges() {
	sync();
    }

    public void sync() {
	Thread t = new Thread(() -> {
	    EntityManager manager = JPAUtil.getInstance().getEntityManager();
	    manager.getTransaction().begin();
	    TypedQuery<Change> changeQuery = manager.createNamedQuery("Change.findOlderThan", Change.class);
	    changeQuery.setParameter("date", Calendar.getInstance());
	    List<Change> changes = changeQuery.getResultList();
	    
	    changes.forEach(c -> {
		
	    });
	    
	    manager.getTransaction().commit();
	    manager.close();
	});
	
	t.setName("Change sync thread");
	t.start();
    }
}
