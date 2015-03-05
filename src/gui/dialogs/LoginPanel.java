package gui.dialogs;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LoginPanel extends GridPane {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public LoginPanel() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/LoginPanel.fxml"));
        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

            Platform.runLater(() -> {
                username.requestFocus();
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

}
