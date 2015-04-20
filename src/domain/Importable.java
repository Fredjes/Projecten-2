package domain;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import persistence.Repository;

/**
 *
 * @author Frederik
 */
public interface Importable<E> {

    /**
     * This method is used to firstly, get a list of all possible properties
     * that can be assigned. Secondly to get a BiConsumer of a String,
     * containing the Excel-data and an instance of <code>E</code> that can put
     * the data in the instance. For example, an entry might contain the key
     * "name" and a BiConsumer that can put the data (the name) in a User
     * instance.
     *
     * @param <P>
     * @return
     */
    <P extends E> Map<String, BiConsumer<String, P>> createHeaderList();
    
    Map<String, Predicate<String>> createHeaderAssignmentList();

    /**
     * Something that can be imported must be able to be saved, this method
     * returns the repository in which it can be saved.
     *
     * @return
     */
    Repository getRepository();
}
