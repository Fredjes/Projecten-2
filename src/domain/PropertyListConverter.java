package domain;

import java.util.List;

/**
 * An interface used in {@link PropertyListBinding} that allows for conversion
 * between properties and lists.
 *
 * @author Frederik De Smedt
 */
public interface PropertyListConverter<EProp, EList> {

    List<EList> toList(EProp prop);

    EProp toProperty(List<EList> list);
}
