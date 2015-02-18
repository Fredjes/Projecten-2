package domain;

import domain.annotations.Display;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}
