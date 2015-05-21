package gui;

import javafx.fxml.FXMLLoader;

/**
 * Util class that holds very commonly used FXML features.
 *
 * @author Frederik De Smedt
 */
public class FXUtil {

    /**
     * Convenience method that loads an FXML in the object given.
     *
     * The resource format must simply be the name of the FXML-file without the
     * extension.
     *
     * Note: Make sure you use the fx:root construct in FXML, otherwise an
     * exception will be thrown.
     *
     * @param root The root in which the loaded data should end up
     * @param resourceName The name of the FXML-file that is used
     */
    public static void loadFXML(Object root, String resourceName) {
	try {
	    FXMLLoader loader = new FXMLLoader(FXUtil.class.getResource("/resources/gui/" + resourceName + ".fxml"));
	    loader.setRoot(root);
	    loader.setController(root);
	    loader.load();
	} catch (Exception ex) {
	    ex.printStackTrace();
//	    System.err.println("Couldn't load FXML: " + ex.getMessage());
	}
    }
}
