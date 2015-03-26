package domain;

import java.util.List;

/**
 *
 * @author Frederik De Smedt
 */
public interface PropertyListConverter<EProp, EList> {

    List<EList> toList(EProp prop);

    EProp toProperty(List<EList> list);
}
