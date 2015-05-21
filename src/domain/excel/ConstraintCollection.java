package domain.excel;

import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * A collection of constraints that will be checked to decide whether an Excel
 * Worksheet should be assigned to an entity type.
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
	constraints.stream().filter((c) -> (c.getRequirement().apply(sheet))).forEach(item -> result += 1f / constraints.size());
	return (1f - tolerance) <= result ? result : 0.0f;
    }
}
