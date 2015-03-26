package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frederik De Smedt
 */
public class PropertyListBindingTest {

    private PropertyListConverter converter;
    private ObservableList<String> list;
    private StringProperty property;

    @Before
    public void initialize() {
	converter = new ThemeConverter();
	property = new SimpleStringProperty();
	list = FXCollections.observableArrayList();
	new PropertyListBinding().bind(property, list, converter);
    }

    @Test
    public void testConversionFromPropertyToListWorks() {
	property.set("test test2 test3");
	Assert.assertEquals(3, list.size());
	Assert.assertEquals("test", list.get(0));
	Assert.assertEquals("test2", list.get(1));
	Assert.assertEquals("test3", list.get(2));
    }
    
    @Test
    public void testConversionFromListToPropertyWorks() {
	list.addAll("test", "test2", "test3");
	Assert.assertEquals("test test2 test3 ", property.get());
    }
}
