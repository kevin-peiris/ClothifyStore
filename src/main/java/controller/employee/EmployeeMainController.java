package controller.employee;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.Employee;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.UserService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EmployeeMainController implements Initializable {
    static EmployeeService employeeService= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
    UserService userService= ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    private ImageView imageView;

    @FXML
    private Label lblEmpName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXPasswordField txtPassword;

    private byte[] imageByte=null;

    public static Employee loginEmployee;

    public static void setEmpId(String empId) {
        for (Employee employee : employeeService.getAll()) {
            if (Objects.equals(employee.getEmpId(), empId)){
                loginEmployee=employee;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setEmployeeDetails();
    }

    private void setEmployeeDetails(){
        txtId.setText(loginEmployee.getEmpId());
        txtName.setText(loginEmployee.getName());
        lblEmpName.setText(loginEmployee.getName());
        txtEmail.setText(loginEmployee.getEmail());
        if (loginEmployee.getImage()!=null){
            imageView.setImage(new Image(new ByteArrayInputStream(loginEmployee.getImage())));
        }
    }

    @FXML
    void btnUpdateEmpOnAction(ActionEvent event) {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else if(!isValidEmail(txtEmail.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Email!");
            alert.show();
            txtEmail.selectAll();
        } else if(!isValidPassword(txtPassword.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Password!");
            alert.show();
            txtPassword.selectAll();
        }else {
            Employee employee = null;

            if (imageByte != null) {
                employee = new Employee(txtId.getText(), txtName.getText(), loginEmployee.getEmail(), loginEmployee.getOrderCount(), imageByte);
            }else {
                employee=loginEmployee;
            }

            User user = userService.getUserByEmail(loginEmployee.getEmail());

            if (!Objects.equals(loginEmployee.getEmail(), txtEmail.getText())){
                user.setEmail(txtEmail.getText());
                employee.setEmail(txtEmail.getText());
                userService.updateUserEmail(user);
            } else if (!txtPassword.getText().isEmpty()) {
                user.setPassword(txtPassword.getText());
                userService.updateUserPassword(user);
            }

            if (employeeService.updateEmployee(employee)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Updated Successfully");
                alert.show();
            }

            txtPassword.setText("");
            setEmpId(employee.getEmpId());
            setEmployeeDetails();
        }
    }

    private boolean isValidPassword(String password) {
        if (txtPassword.getText().isEmpty()){
            return true;
        }
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+]).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    @FXML
    void btnUpdateEmpImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Edit Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
                imageByte = Files.readAllBytes(selectedFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void EmployeeMainPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            EmployeeMainController.setEmpId(loginEmployee.getEmpId());

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_main.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.CANCEL) {
                    closeEvent.consume();
                }
            });
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ItemPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            ItemController.setEmpId(loginEmployee.getEmpId());

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/item.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Stage adminStage = new Stage();
                    try {
                        adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_main.fxml"))));
                        adminStage.setResizable(false);
                        adminStage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    closeEvent.consume();
                }
            });
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OrderPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            OrderController.setEmpId(loginEmployee.getEmpId());

            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/order.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Stage adminStage = new Stage();
                    try {
                        adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_main.fxml"))));
                        adminStage.setResizable(false);
                        adminStage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    closeEvent.consume();
                }
            });
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
