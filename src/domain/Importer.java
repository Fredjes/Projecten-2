package domain;

import java.util.List;
import java.util.Set;

/**
 * An Importer is designed to allow imports of entities in a robust way,
 * encapsuling the prediction, property assignment and persisting.
 *
 * @author Frederik
 */
public interface Importer {

    /**
     * Get a list of all possible fields that can be predicted. These might be
     * different than the properties of the backing entity, for example: fields
     * might comprise of multiple properties or might be used to generate a
     * legal property value.
     *
     * @return The list of all possible prediction fields
     */
    public Set<String> getFields();

    /**
     * Give a list of all possible headers that will be asked to predict,
     * invoking this method should be optional but will optimize field
     * prediction.
     *
     * @param headers
     */
    public void initHeaders(List<String> headers);

    /**
     * Try to predict a possible field based on a column name. If a field could
     * not be predicted either an empty String or a default value can be
     * returned (yet never null). The returned results will, however, always be
     * an element of the set returned by {@link Importer#getFields() }.
     *
     * @param header The column name that shall be used for the prediction
     * @return The predicted field or default value
     */
    public String predictField(String header);

    /**
     * Bind the value, linked to a certain field, to a backing entity. The field
     * will be interpreted and the value will be parsed to make sure the data is
     * correctly stored in the entity. The given field must be an element of the
     * set returned by {@link Importer#getFields() }. If the binding didn't
     * work, no exception will be thrown and the value won't be bound.
     *
     * @param field The field linked to the value
     * @param value The value that must be stored in the entity
     */
    public void setField(String field, String value);

    /**
     * Will select a new entity that will be used in the current context. When
     * binding fields, using {@link Importer#setField(java.lang.String, java.lang.String)
     * }, this method must be invoked to bind the values to a new entity, you
     * will not overwrite the bound values of previous entities, meaning the
     * previous entities are stored permanently.
     */
    public void nextEntity();

    /**
     * Persist all entities in the designated Repository. All entities used in
     * the context (added each time {@link Importer#nextEntity() } is invoked)
     * will be stored in the repository with their respective data.
     */
    public void persistEntities();
    
    
}
