package domain.gui;

import domain.FontCache;
import gui.ScreenSwitcher;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Brent
 */
public class IconConfigTest {

    private Button button;
    private Label label;
    private StackPane pane;
    private Text text;

    private ScreenSwitcher switcher;

    @BeforeClass
    public static void preInit() {
	Thread t = new Thread(() -> Application.launch(TestApp.class, new String[0]));
	t.start();
    }

    @Before
    public void init() {
	button = new Button();
	label = new Label();
	pane = new StackPane();
	text = new Text();
	switcher = new ScreenSwitcher();

	//set styles
	label.getStyleClass().add("icon-voorwerpen-beheren");
	text.getStyleClass().add("icon-te-laat");
	button.getStyleClass().add("icon-excel-importeren");

	pane.getChildren().addAll(button, label, text);
    }

    @Test
    public void testLabelReceivesIcon() {
	label.setText("test");
	switcher.loadIcons(label);
	Assert.assertEquals("\uf1b3", label.getText());
    }

    @Test
    public void testFontIsSetCorrectly() {
	label.setText("test");
	label.setFont(new Font("Arial", 16));
	switcher.loadIcons(label);
	Assert.assertEquals(FontCache.getIconFont(16), label.getFont());
    }

    @Test
    public void testTextReceivesIcon() {
	text.setText("test");
	switcher.loadIcons(text);
	Assert.assertEquals("\uf071", text.getText());
    }

    @Test
    public void testButtonReceivesIcon() {
	button.setText("test");
	switcher.loadIcons(button);
	Assert.assertEquals("\uf0ce", button.getText());
    }

    @Test
    public void testRecursiveIconSetting() {
	switcher.getChildren().add(pane);
	switcher.loadIcons(switcher);
	Assert.assertEquals("\uf1b3", label.getText());
	Assert.assertEquals("\uf071", text.getText());
	Assert.assertEquals("\uf0ce", button.getText());
    }

}
