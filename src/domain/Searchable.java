package domain;

import java.util.function.Predicate;

/**
 * An interface that should be used by class that has a representation in the
 * GUI which can be filtered. Extending of <code>Predicate&lt;String&gt;</code>
 * allows the Searchable to always accept the search query (inserted by the
 * user). It is possible to directly implement the extended interface in the
 * domain classes, meaning this interface is mainly used as a "convenience
 * interface", avoiding possible future mistakes or inconveniences.
 *
 * @author Frederik
 */
public interface Searchable extends Predicate<String> {
}
