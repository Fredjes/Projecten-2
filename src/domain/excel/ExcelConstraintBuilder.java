package domain.excel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brent C.
 */
public class ExcelConstraintBuilder {

    private final List<ExcelConstraint> constraints = new ArrayList<>();

    private float tolerance;

    public ExcelConstraintBuilder needsStrict(String text) {
	ExcelConstraint c = new ExcelConstraint(true, true, (sheet) -> {
	    List<String> headers = ExcelManager.getInstance().getColumnHeaders(sheet);
	    return headers.stream().anyMatch((s) -> (s.toLowerCase().equals(text.toLowerCase())));
	});
	constraints.add(c);
	return this;
    }

    public ExcelConstraintBuilder needsTolerant(String text) {
	ExcelConstraint c = new ExcelConstraint(false, true, (sheet) -> {
	    List<String> headers = ExcelManager.getInstance().getColumnHeaders(sheet);
	    return headers.stream().anyMatch((s) -> (s.toLowerCase().contains(text.toLowerCase())));
	});
	constraints.add(c);
	return this;
    }

    public ExcelConstraintBuilder allowTolerance(float f) {
	tolerance = f;
	return this;
    }

    public ExcelConstraintBuilder containsRegex(String regex) {
	ExcelConstraint c = new ExcelConstraint(true, true, (sheet) -> {
	    List<String> headers = ExcelManager.getInstance().getColumnHeaders(sheet);
	    return headers.stream().anyMatch((s) -> (s.toLowerCase().matches(regex)));
	});
	constraints.add(c);
	return this;
    }

    public ConstraintCollection build() {
	return new ConstraintCollection(constraints, tolerance);
    }
}
