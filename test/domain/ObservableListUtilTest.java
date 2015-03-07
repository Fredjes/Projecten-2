package domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;

/**
 *
 * @author Frederik
 */
public class ObservableListUtilTest {

    private ObservableList<String> base;

    @Before
    public void initialize() {
	base = FXCollections.observableArrayList();
	
    }
}
