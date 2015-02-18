package domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that should be used with a getter returning a type supported by the GUI.
 *
 * @author Frederik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Display {

    /**
     * Used to get the name of a property that should be used in the GUI.
     *
     * @return Name of the property
     */
    String value();
}
