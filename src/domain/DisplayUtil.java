package domain;

import domain.annotations.Display;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
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

    /**
     * Method that will evaluate and return all fields of an Entity-class that can be used in a TableView, i.e. all properties that contain a single piece of data (for example no: lists or other Entity-classes).
     *
     * @param clazz Class from which the header fields should be generated.
     * @return Header of every property containing a single piece of data.
     */
    public static List<String> getSingleDataHeaderFields(Class<?> clazz) {
	return getHeaderFields(clazz, true).collect(Collectors.toList());
    }

    /**
     * Method that will evaluate and return all fields of an Entity-class that cannot be used in a TableView, i.e. all properties that do not contain a single piece of data (for example: lists or Entity-classes).
     *
     * @param clazz Class from which the header fields should be generated.
     * @return Header of every property containing a piece of data composed of multiple pieces.
     */
    public static List<String> getMultiDataHeaderFields(Class<?> clazz) {
	return getHeaderFields(clazz, false).map(s -> String.format("Beheer %s", s.toLowerCase())).collect(Collectors.toList());
    }

    private static Stream<String> getHeaderFields(Class<?> clazz, boolean single) {
	return Arrays.asList(clazz.getMethods()).stream()
		.filter(m -> m.isAnnotationPresent(Display.class) && m.getAnnotation(Display.class).single() == single)
		.map(m -> m.getAnnotation(Display.class).name());
    }

    public static List<TableColumn> getTableColumns(Class<?> clazz) {
	return Arrays.asList(clazz.getMethods()).stream()
		.filter(m -> m.isAnnotationPresent(Display.class) && m.getAnnotation(Display.class).single())
		.map((Method m) -> {
		    String propertyName = m.getName().substring(0, m.getName().indexOf("Property"));
		    TableColumn t = new TableColumn(m.getAnnotation(Display.class).name());
		    t.setCellValueFactory(new PropertyValueFactory(propertyName));
		    t.setCellFactory(createAssociatedTableCellCallback(m.getReturnType().asSubclass(Property.class)));
		    t.setEditable(true);
		    return t;
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

    private static boolean isSubClass(Class<?> superClass, Class<?> subClass) {
	return superClass.isAssignableFrom(subClass);
    }
}
