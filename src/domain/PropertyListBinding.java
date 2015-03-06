package domain;

import java.util.List;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Frederik
 */
public class PropertyListBinding<EProp, EList> {

    private ChangeListener<EProp> propertyListener = null;
    private ListChangeListener listListener = null;

    private Property<EProp> currentProperty;
    private ObservableList<EList> currentList;

    public void bind(Property<EProp> property, ObservableList<EList> list, PropertyListConverter<EProp, EList> converter) {
	ListChangeListener listListener = (ListChangeListener.Change c) -> {
	    // Avoid stackoverflow due to cyclic event triggering
	    property.removeListener(propertyListener);
	    property.setValue(converter.toProperty((List<EList>) c.getList()));
	    property.addListener(propertyListener);
	};

	propertyListener = (ObservableValue<? extends EProp> observable, EProp oldValue, EProp newValue) -> {
	    // Avoid stackoverflow due to cyclic event triggering
	    list.removeListener(listListener);
	    list.setAll(converter.toList(newValue));
	    list.addListener(listListener);
	};

	this.currentProperty = property;
	this.currentList = list;

	property.addListener(propertyListener);
	list.addListener(listListener);
    }

    public void unbind() {
	if (this.currentProperty == null) {
	    this.currentProperty.removeListener(propertyListener);
	}

	if (this.currentList == null) {
	    this.currentList.removeListener(listListener);
	}
    }
}
