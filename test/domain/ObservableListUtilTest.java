package domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frederik De Smedt
 */
public class ObservableListUtilTest {

    private ObservableList<Integer> derived;
    private ObservableList<Object> base;

    @Before
    public void initialize() {
	base = FXCollections.observableArrayList();
	derived = ObservableListUtil.convertObservableList(base, (o) -> {
	    return o.hashCode();
	});

	for (int i = 0; i < 50; i++) {
	    base.add(new Object());
	}
    }

    @Test
    public void testDerivedReceivesConvertedObjects() {
	Assert.assertEquals(base.size(), derived.size());
	for (int i = 0; i < base.size(); i++) {
	    Assert.assertEquals(base.get(i).hashCode(), (int) derived.get(i));
	}
    }

    @Test
    public void testDerivedElementRemovesOnRemove() {
	Assert.assertEquals(base.size(), derived.size());
	base.remove(25);
	Assert.assertEquals(base.size(), derived.size());
	for (int i = 0; i < base.size(); i++) {
	    Assert.assertEquals(base.get(i).hashCode(), (int) derived.get(i));
	}
    }

    @Test
    public void testDerivedElementAddedOnAddition() {
	Assert.assertEquals(base.size(), derived.size());
	base.add(new Object());
	Assert.assertEquals(base.size(), derived.size());
	for (int i = 0; i < base.size(); i++) {
	    Assert.assertEquals(base.get(i).hashCode(), (int) derived.get(i));
	}
    }
}
