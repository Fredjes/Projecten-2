package domain.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author Brent C.
 */
public class ExcelConstraint {

    private boolean strict;
    private boolean required;

    private Function<XSSFSheet, Boolean> requirement;

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
