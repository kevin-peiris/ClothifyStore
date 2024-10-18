package controller;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;

public class LoginController {
    UserService userService= ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/register.fxml"))));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        User user = userService.loginUser(txtEmail.getText(), txtPassword.getText());
        if (user!=null){
            Stage stage=new Stage();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/order.fxml"))));
                stage.setResizable(false);
                stage.setOnCloseRequest(closeEvent -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                    if (alert.showAndWait().get() == ButtonType.CANCEL) {
                        closeEvent.consume();
                    }
                });
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Email or Password");
            alert.show();
        }

    }

}
