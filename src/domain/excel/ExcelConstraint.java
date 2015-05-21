package domain.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * A constraint of which several must pass in order to assign an Excel Worksheet
 * to a specific entity type.
 *
 * @author Brent C.
 */
public class ExcelConstraint {

    private final boolean strict;
    private final boolean required;

    private final Function<XSSFSheet, Boolean> requirement;

    public ExcelConstraint(boolean strict, boolean required, Function<XSSFSheet, Boolean> requirement) {
	this.strict = strict;
	this.required = required;
	this.requirement = requirement;
    }

    public boolean isStrict() {
	return strict;
    }

    public boolean isRequired() {
	return required;
    }

    public Function<XSSFSheet, Boolean> getRequirement() {
	return requirement;
    }

}
