package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import persistence.JPAUtil;

/**
 * An entity designed for change tracking in the database.
 *
 * This class is used to allow efficient database selection, i.e. when an
 * application has to get all the recent changes of the database it doesn't have
 * to get all data. Instead if an application needs to be synchronized it can
 * look at the Change-entities and based on that, determine which records have
 * to be selected.
 *
 * In order to let this system work, one should always insert a new
 * Change-record during deletions, updates or additions to the database. The
 * advantages of such a system are based on the size and usage of a database,
 * yet it can be very helpful with a bigger database and limited resources. To
 * allow a variable length of changes to be persisted, a Blob column is used
 * (changeData). The format of this data is as follows:
 *
 * (versionID length (entityId[changeFlag])*)*
 *
 * <li>
 * <ul>versionID: This can be interpreted as a header of a piece of data, it
 * represents the versionID of a class (4 bytes), allowing both the persisting
 * of a class reference and automatic version control, for example: older
 * versions of an application containg outdated Entity classes cannot retrieve
 * new changes (if enforced by version change).</ul>
 * <ul>length: Used to determine how many entities are available in the entity
 * segment, the unit of length is the amount of entity references, not the
 * amount of bytes.
 * </ul>
 * <ul>entityId: Primary key of an entity instance, expressed with four bytes
 * (integer), of the specified entity class (versionID), the entity referenced
 * is either updated, added or removed.</ul>
 * <ul>changeFlag: A 1 bit flag (yet encapsuled in a byte), if the bit is 0 then
 * the refered entity is either updated or added (so a select-query will work),
 * if the bit is 1 then the entity is removed and should be removed in the
 * application rather then executing a select query to the record.</ul>
 * </li>
 *
 * The change detection system is usefull for quick and lightweight database
 * update checking i.e. to check whether records are changed in the database.
 * The size of a change can theoretically be of arbitrary size, yet it is
 * restricted to the maximum record size of the DBMS that is being used. Bigger
 * change-records should however, have no considerable performance impact on
 * checking for change updates, unless if a change does actually have to be
 * selected.
 *
 * The change records in the database should be used as immutable objects,
 * although this cannot be enforced. Instead, when an erroneous change is
 * persisted, it should be corrected using another change-record. This way the
 * internal implementation of the change entity can be certain that it's data is
 * up-to-date and thus avoiding outdated change records.
 *
 * @author Frederik De Smedt
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Change.findOlderThan", query = "SELECT c FROM Change c WHERE c.date >= :date ORDER BY C.date ASC")
})
@Table(name = "CHANGE_TBL")
public class Change implements Serializable {

    public static final byte INSERTION_UPDATE_CHANGE = 0;
    public static final byte DELETION_CHANGE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_date")
    private Date date;

    @Lob
    private byte[] changeData;

    public Change() {
    }

    public Change(Date date, byte[] changeData) {
	this.date = date;
	this.changeData = changeData;
    }

    /**
     * A map holding the index of the header of each entity. This should be
     * filled once with every change, it will allow more efficient lookups with
     * bigger changes by already providing the start index for each entity
     * segment.
     */
    @Transient
    private Map<Integer, Integer> entitySegmentIndices;

    // No corruption exception handling
    private void ensureSegmentIndexInitialization() {
	if (entitySegmentIndices == null) {
	    entitySegmentIndices = new HashMap<>();
	    for (int pointer = 0; pointer < changeData.length;) {
		int versionId = readInt(pointer);
		pointer += 4;
		entitySegmentIndices.put(versionId, pointer);
		int length = readInt(pointer);
		pointer += 4 + length * 5;
	    }
	}
    }

    public Map<Integer, List<Integer>> getRemovedEntities() {
	Map<Integer, List<Integer>> results = new HashMap<>();
	entitySegmentIndices.entrySet().forEach((Entry<Integer, Integer> e) -> results.put(e.getKey(), getRemovedEntities(e.getKey())));
	return results;
    }

    public List<Integer> getRemovedEntities(int versionID) {
	ensureSegmentIndexInitialization();
	if (!entitySegmentIndices.containsKey(versionID)) {
	    return Collections.EMPTY_LIST;
	}

	List<Integer> results = new ArrayList<>();

	int index = entitySegmentIndices.get(versionID);
	for (int length = changeData[index++] << 24 | changeData[index++] << 16 | changeData[index++] << 8 | changeData[index++]; length > 0; length--) {
	    int id = readInt(index);
	    index += 4;
	    if (changeData[index++] == DELETION_CHANGE) {
		results.add(id);
	    }
	}

	return results;
    }

    /**
     * This will return a List of entities of the specified entity type.
     *
     * @param <E> Entity that should be returned
     * @param versionId
     * @param clazz The class of the entity
     * @param manager An EntityManager from where the entities will be retrieved
     * @return A list of entities
     */
    public <E extends Changeable> List<E> getChangedEntities(int versionId, Class<E> clazz, EntityManager manager) {
	ensureSegmentIndexInitialization();
	if (!entitySegmentIndices.containsKey(versionId)) {
	    return Collections.EMPTY_LIST;
	}

	List<E> results = new ArrayList<>();

	int index = entitySegmentIndices.get(versionId);
	for (int length = changeData[index++] << 24 | changeData[index++] << 16 | changeData[index++] << 8 | changeData[index++]; length > 0; length--) {
	    int id = readInt(index);
	    index += 4;
	    if (changeData[index++] == INSERTION_UPDATE_CHANGE) {
		E foundData = manager.find(clazz, id);

		if (foundData != null) {
		    results.add(foundData);
		}
	    }
	}

	return results;
    }

    /**
     * Get a list of all the primary keys of entities with the specified
     * versionID.
     *
     * @param versionId VersionID that the returned entities should have
     * @return A list of all the primary keys (Integer) that the entities should
     * have
     */
    public List<Integer> getChangedEntities(int versionId) {
	ensureSegmentIndexInitialization();
	if (!entitySegmentIndices.containsKey(versionId)) {
	    return Collections.EMPTY_LIST;
	}

	List<Integer> results = new ArrayList<>();

	int index = entitySegmentIndices.get(versionId);
	for (int length = changeData[index++] << 24 | changeData[index++] << 16 | changeData[index++] << 8 | changeData[index++]; length > 0; length--) {
	    int id = readInt(index);
	    index += 4;
	    if (changeData[index++] == INSERTION_UPDATE_CHANGE) {
		results.add(id);
	    }
	}

	return results;
    }

    /**
     * A convenience method that will read an integer from the changeData.
     *
     * @param pointer The current pointer in the changeData byte[]
     * @return
     */
    private int readInt(int pointer) {
	return changeData[pointer++] << 24 | changeData[pointer++] << 16 | changeData[pointer++] << 8 | changeData[pointer++];
    }
    
    public static void main(String[] args) {
	Change change = new Change();
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	manager.persist(change);
	manager.getTransaction().commit();
    }
}
