package gui;

import javafx.fxml.FXMLLoader;

/**
 *
 * @author Frederik De Smedt
 */
public class FXUtil {

    public static void loadFXML(Object root, String url) {
	try {
	    FXMLLoader loader = new FXMLLoader(FXUtil.class.getResource("/resources/gui/" + url + ".fxml"));
	    loader.setRoot(root);
	    loader.setController(root);
	    loader.load();
	} catch (Exception ex) {
	    ex.printStackTrace();
//	    System.err.println("Couldn't load FXML: " + ex.getMessage());
	}
    }
}
