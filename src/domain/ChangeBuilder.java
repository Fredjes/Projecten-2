package domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Frederik
 * @param <E>
 */
public class ChangeBuilder<E extends Changeable> {

    private final Map<Integer, List<Entity>> updates = new HashMap<>();

    public ChangeBuilder() {
    }

    public void addChange(E changedEntity) {
	if (!updates.keySet().contains(changedEntity.getVersionID())) {
	    updates.put(changedEntity.getVersionID(), new ArrayList<>(Arrays.asList(new Entity(changedEntity, Change.INSERTION_UPDATE_CHANGE))));
	} else {
	    updates.get(changedEntity.getVersionID()).add(new Entity(changedEntity, Change.INSERTION_UPDATE_CHANGE));
	}
    }

    public void addChange(E... changedEntities) {
	Arrays.stream(changedEntities).forEach(this::addChange);
    }

    public void addDeletion(E removedEntity) {
	if (!isPresent(removedEntity)) {
	    updates.put(removedEntity.getVersionID(), new ArrayList<>(Arrays.asList(new Entity(removedEntity, Change.DELETION_CHANGE))));
	} else {
	    updates.get(removedEntity.getVersionID()).add(new Entity(removedEntity, Change.DELETION_CHANGE));
	}
    }

    public void addDeletion(E... removedEntities) {
	Arrays.stream(removedEntities).forEach(this::addDeletion);
    }

    public void remove(E entity) {
	updates.remove(entity.getVersionID(), entity);
    }

    public boolean isPresent(E changedEntity) {
	return updates.values().stream().anyMatch(pl -> pl.stream().anyMatch(p -> p.getEntity().equals(changedEntity))); // WARNING: Might cause bug
    }

    public Change build() {
	final byte[] data = new byte[updates.values().stream().mapToInt(l -> l.size() * 5).sum() + updates.keySet().size() * 8];

	final IntegerProperty pointer = new SimpleIntegerProperty();

	updates.entrySet().forEach(es -> {
	    writeInt(pointer, es.getKey(), data);
	    writeInt(pointer, es.getValue().size(), data);
	    es.getValue().forEach(p -> {
		writeInt(pointer, p.getEntity().getID(), data);
		writeByte(pointer, p.getFlag(), data);
	    });
	});

	return new Change(Date.from(Instant.now()), data);
    }

    private void writeInt(IntegerProperty pointerProperty, int data, byte[] dataArr) {
	writeInt(pointerProperty.get(), data, dataArr);
	pointerProperty.set(pointerProperty.get() + 4);
    }

    private void writeInt(int pointer, int data, byte[] dataArr) {
	dataArr[pointer] = (byte) (data >>> 24);
	dataArr[pointer + 1] = (byte) (data >>> 16);
	dataArr[pointer + 2] = (byte) (data >>> 8);
	dataArr[pointer + 3] = (byte) data;
    }

    private void writeByte(IntegerProperty pointerProperty, byte data, byte[] dataArr) {
	dataArr[pointerProperty.get()] = data;
	pointerProperty.set(pointerProperty.get() + 1);
    }

    private static class Entity {
	
	private Changeable entity;
	private byte flag;

	public Entity(Changeable entity, byte flag) {
	    this.entity = entity;
	    this.flag = flag;
	}

	public Changeable getEntity() {
	    return entity;
	}

	public void setEntity(Changeable entity) {
	    this.entity = entity;
	}

	public byte getFlag() {
	    return flag;
	}

	public void setFlag(byte flag) {
	    this.flag = flag;
	}
    }
}
