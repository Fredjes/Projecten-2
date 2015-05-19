package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import persistence.JPAUtil;

/**
 * 
 *
 * @author Frederik De Smedt
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Change.findNewerThan", query = "SELECT c FROM Change c WHERE c.date >= :date ORDER BY C.date ASC")
})
@Table(name = "CHANGE_TBL")
public class Change implements Serializable, Changeable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_date")
    private Date date;
    
    private boolean deleted;
    
    private int entityId;
    private int versionId;

    public Change() {
    }

    public Change(Date date, Changeable changeable, boolean deleted) {
	this.date = date;
	this.entityId = changeable.getID();
	this.versionId = changeable.getVersionID();
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public boolean isDeleted() {
	return deleted;
    }

    public void setDeleted(boolean deleted) {
	this.deleted = deleted;
    }

    public int getEntityId() {
	return entityId;
    }

    public void setEntityId(int entityId) {
	this.entityId = entityId;
    }

    public int getVersionId() {
	return versionId;
    }

    public void setVersionId(int versionId) {
	this.versionId = versionId;
    }
    
    public static void main(String[] args) {
	Change change = new Change();
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	manager.persist(change);
	manager.getTransaction().commit();
    }

    @Override
    public int getVersionID() {
	return 0;
    }

    @Override
    public int getID() {
	return -1;
    }
}
