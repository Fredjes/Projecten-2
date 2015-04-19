package domain.excel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brent C.
 */
public class ExcelConstraintBuilder {

    private ConstraintCollection collection;

    private final List<ExcelConstraint> constraints = new ArrayList<>();

    private float tolerance;

    public ExcelConstraintBuilder needsStrict(String text) {
	ExcelConstraint c = new ExcelConstraint(true, true, (sheet) -> {
	    List<String> headers = ExcelManager.getInstance().getColumnHeaders(sheet);
	    for (String s : headers) {
		if (s.toLowerCase().equals(text.toLowerCase())) {
		    return true;
		}
	    }
	    return false;
	});
	constraints.add(c);
	return this;
    }

    public ExcelConstraintBuilder needsTolerant(String text) {
	ExcelConstraint c = new ExcelConstraint(false, true, (sheet) -> {
	    List<String> headers = ExcelManager.getInstance().getColumnHeaders(sheet);
	    for (String s : headers) {
		if (s.toLowerCase().contains(text.toLowerCase())) {
		    return true;
		}
	    }
	    return false;
	});
	constraints.add(c);
	return this;
    }

    public ExcelConstraintBuilder allowTolerance(float f) {
	tolerance = f;
	return this;
    }

    public ConstraintCollection build() {
	return new ConstraintCollection(constraints, tolerance);
    }
}
