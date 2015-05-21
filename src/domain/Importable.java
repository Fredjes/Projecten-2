package domain;

/**
 * An interface that will allow an entity to support excel imports. A
 * specialized {@link domain.Importer} must be returned that allows importing of
 * the entity.
 *
 * @author Frederik
 */
public interface Importable {

    Importer createImporter();
}
