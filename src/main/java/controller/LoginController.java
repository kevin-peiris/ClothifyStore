package controller;

import com.jfoenix.controls.JFXPasswordField;
import dto.Admin;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.AdminService;
import service.custom.UserService;
import util.ServiceType;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {
    UserService userService= ServiceFactory.getInstance().getServiceType(ServiceType.USER);
    AdminService adminService= ServiceFactory.getInstance().getServiceType(ServiceType.ADMIN);

    @FXML
    private TextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;


    public void btnRegisterOnAction(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/register.fxml"))));
            stage.setResizable(false);
            stage.show();
            Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        if (txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else if(!isValidEmail(txtEmail.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Email!");
            alert.show();
            txtEmail.selectAll();
        }else {

            User user = userService.loginUser(txtEmail.getText(), txtPassword.getText());
            Admin admin = adminService.loginAdmin(txtEmail.getText(), txtPassword.getText());
            if (user != null) {
                OrderController orderController = new OrderController();
                orderController.setEmpId(user.getEmpId());
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/order.fxml"))));
                    stage.setResizable(false);
                    stage.setOnCloseRequest(closeEvent -> {
                        Stage adminStage = new Stage();
                        try {
                            adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml"))));
                            adminStage.setResizable(false);
                            adminStage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (admin!=null){
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main.fxml"))));
                    stage.setResizable(false);
                    stage.setOnCloseRequest(closeEvent -> {
                        Stage adminStage = new Stage();
                        try {
                            adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml"))));
                            adminStage.setResizable(false);
                            adminStage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Email or Password");
                alert.show();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    public void btnPWForgotPageOnAction(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/forgot_password.fxml"))));
            stage.setResizable(false);
            stage.show();
            Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
