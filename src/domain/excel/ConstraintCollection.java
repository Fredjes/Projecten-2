package domain.excel;

import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author Brent C.
 */
public class ConstraintCollection {

    private final List<ExcelConstraint> constraints;

    private final float tolerance;

    private float result = 0.0f;

    public ConstraintCollection(List<ExcelConstraint> constraints, float tolerance) {
	this.constraints = constraints;
	this.tolerance = tolerance;
    }

    public float test(XSSFSheet sheet) {
	result = 0.0f;
	for (ExcelConstraint c : constraints) {
	    if (c.getRequirement().apply(sheet)) {
		result += 1f / constraints.size();
	    }
	}
	return (1f - tolerance) <= result ? result : 0.0f;
    }
}
