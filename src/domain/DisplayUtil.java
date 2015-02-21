package domain;

import domain.annotations.Display;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * A Util-class used to get headers and data of Entity-classes by Display-annotation detection.
 *
 * @author Frederik
 */
public class DisplayUtil {

    public static List<TableColumn> getTableColumns(Class<?> clazz) {
	return Arrays.asList(clazz.getMethods()).stream()
		.filter(m -> m.isAnnotationPresent(Display.class) && m.getAnnotation(Display.class).single())
		.map((Method m) -> {
		    String propertyName = m.getName().substring(0, m.getName().indexOf("Property"));
		    TableColumn t = new TableColumn(m.getAnnotation(Display.class).name());
		    t.setCellValueFactory(new PropertyValueFactory(propertyName));
		    t.setCellFactory(createAssociatedTableCellCallback(m.getReturnType().asSubclass(Property.class)));
		    t.setEditable(true);
		    t.impl_setReorderable(false);
		    return t;
		}).sorted((o1, o2) -> {
		    if (o1.getText().equalsIgnoreCase("naam")) {
			return -1;
		    } else if (o2.getText().equalsIgnoreCase("naam")) {
			return 1;
		    } else {
			return 0;
		    }
	}).collect(Collectors.toList());
    }

    private static Callback createAssociatedTableCellCallback(Class<? extends Property> p) {
	if (isSubClass(p, StringProperty.class)) {
	    return TextFieldTableCell.forTableColumn();
	} else if (isSubClass(p, IntegerProperty.class)) {
	    return TextFieldTableCell.forTableColumn(new IntegerStringConverter());
	} else if (isSubClass(p, ObjectProperty.class)) {
	    // Has to be Damage-enum
	    return ComboBoxTableCell.forTableColumn(Damage.values());
	} else {
	    throw new IllegalArgumentException("Unsupported property");
	}
    }

    private static List<String> getDataContents(Class<?> clazz, Object instance) {
	return Arrays.asList(clazz.getMethods()).stream()
		.filter(m -> m.isAnnotationPresent(Display.class) && m.getAnnotation(Display.class).single())
		.map(m -> {
		    try {
			return ((Property) m.invoke(instance)).getValue().toString();
		    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("Couldn't get data contents of property: " + e.getMessage());
		    }
		    return "";
		}).collect(Collectors.toList());
    }

    private static boolean isSubClass(Class<?> superClass, Class<?> subClass) {
	return superClass.isAssignableFrom(subClass);
    }

    public static boolean isStoryBag(Class<?> clazz) {
	return clazz.equals(StoryBag.class);
    }

    public static boolean isItemCopy(Class<?> clazz) {
	return clazz.equals(ItemCopy.class);
    }

    public static Predicate createPredicateForSearch(String searchBy, Class<?> currentClazz) {
	if (searchBy.isEmpty()) {
	    return i -> true;
	} else {
	    return i -> getDataContents(currentClazz, i).stream().anyMatch(s -> s.toLowerCase().contains(searchBy.toLowerCase()));
	}
    }
}
