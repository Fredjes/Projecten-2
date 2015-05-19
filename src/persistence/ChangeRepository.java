package persistence;

import domain.Change;
import domain.ChangeConfig;
import domain.Changeable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Frederik
 */
public class ChangeRepository extends Repository<Change> {

    private static ChangeRepository instance;
    private Date lastUpdateTime;

    private ChangeRepository() {
	lastUpdateTime = Date.from(Instant.now());
    }
    
    public static ChangeRepository getInstance() {
	if (instance == null) {
	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> instance.sync()));
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();
	    instance = new ChangeRepository();
	}
	
	return instance;
    }

    @Override
    public void add(Change c) {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	manager.persist(c);
	manager.getTransaction().commit();
	manager.close();
	lastUpdateTime = Date.from(Instant.now());
	sync();
    }

    @Override
    public void remove(Change c) {
	sync();
    }

    @Override
    public void remove(Predicate<Change> predicate) {
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
	    TypedQuery<Change> changeQuery = manager.createNamedQuery("Change.findNewerThan", Change.class);
	    changeQuery.setParameter("date", lastUpdateTime);
	    List<Change> changes = changeQuery.getResultList();

	    lastUpdateTime = Date.from(Instant.now());
	    changes.forEach(c -> {
		Repository<Changeable> repository = Repository.getRepositorySupportingVersion(c.getVersionId());
		if (repository != null) {
		    if (c.isDeleted()) {
			repository.remove(en -> en.getID() == c.getEntityId());
		    } else {
			Class<? extends Changeable> entityClass = ChangeConfig.createEntityClass(c.getVersionId());
			if (entityClass != null) {
			    repository.update(c.getEntityId());
			}
		    }
		}
	    });

	    manager.getTransaction().commit();
	    manager.close();
	});

	t.setName("Change sync thread");
	t.start();
    }

    @Override
    public void update(int pk) {
	sync();
    }
}
