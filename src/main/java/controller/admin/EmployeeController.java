package controller.admin;

import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import dto.Employee;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;

import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EmployeeController implements Initializable {
    EmployeeService service= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
    UserService userService= ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableView<Employee> tbl;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField viewId;

    @FXML
    private TextField viewName;

    @FXML
    private TextField viewEmail;

    @FXML
    private TextField viewPassword;

    @FXML
    private ImageView inpImageView;

    @FXML
    private ImageView editImageView;

    private byte[] inpImageByte=null;
    private byte[] editImageByte=null;

    @FXML
    void btnAddItemImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                inpImageView.setImage(image);
                inpImageByte = Files.readAllBytes(selectedFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void btnAddEmpOnAction(ActionEvent event) {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty() ) {
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
        }else{

            Employee employee = new Employee(txtId.getText(), txtName.getText(), txtEmail.getText(),0,inpImageByte);

            userService.registerUser(new User(employee.getEmpId(),employee.getEmail(), txtPassword.getText(), "Employee"));

            System.out.println(employee);

            if (service.addEmployee(employee)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Added Successfully");
                alert.show();
            }

            generateEmpId();
            loadTable();

            txtName.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            inpImageView.setImage(null);
            inpImageByte=null;

            txtName.requestFocus();
        }
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+]).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateEmpId();
        loadTable();
        txtName.requestFocus();

        tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setViewFields(newValue);
            }
        });
    }

    private void setViewFields(Employee employee) {
        viewId.setText(employee.getEmpId());
        viewName.setText(employee.getName());
        viewEmail.setText(employee.getEmail());
        if (employee.getImage()!=null){
            editImageView.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
        }
    }

    private void generateEmpId(){
        int lastEmpCount=0;

        ObservableList<Employee> employeeList = service.getAll();
        if (!employeeList.isEmpty()) {
            Employee lastEmployee = employeeList.getLast();

            String lastEmpId = lastEmployee.getEmpId();

            String lastStringEmpCount = lastEmpId.substring(lastEmpId.length() - 3);

            lastEmpCount = Integer.parseInt(lastStringEmpCount);
        }

        String id=String.format("E%03d",lastEmpCount+1);
        txtId.setText(id);
    }

    private void loadTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tbl.setItems(service.getAll());
    }

    @FXML
    void btnDeleteEmpOnAction(ActionEvent event) {
        Employee selectedEmployee = tbl.getSelectionModel().getSelectedItem();
        User user = userService.getUserByEmail(selectedEmployee.getEmail());
        userService.deleteUser(user);

        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an employee to delete.");
            alert.show();
            return;
        }

        if (service.deleteEmployee(selectedEmployee)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Deleted Successfully");
            alert.show();
        }

        loadTable();

        viewId.setText("");
        viewName.setText("");
        viewEmail.setText("");
        viewPassword.setText("");
        editImageView.setImage(null);
    }

    @FXML
    void btnUpdateItemImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Edit Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                editImageView.setImage(image);
                editImageByte = Files.readAllBytes(selectedFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    @FXML
    void btnUpdateEmpOnAction(ActionEvent event) {
        Employee selectedEmployee = tbl.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an employee to update.");
            alert.show();
            return;
        }

        if (viewName.getText().isEmpty() || viewEmail.getText().isEmpty() || viewPassword.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else if(!isValidEmail(viewEmail.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Email!");
            alert.show();
            txtEmail.selectAll();
        } else if(!isValidPassword(viewPassword.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Password!");
            alert.show();
            txtPassword.selectAll();
        }else{
            Employee employee =null;

            if (editImageByte==null){
                employee = new Employee(viewId.getText(), viewName.getText(), viewEmail.getText(),selectedEmployee.getOrderCount(),selectedEmployee.getImage());
            }else{
                employee = new Employee(viewId.getText(), viewName.getText(), viewEmail.getText(),selectedEmployee.getOrderCount(),editImageByte);
            }

            System.out.println(employee);

            if (service.updateEmployee(employee)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Employee Updated Successfully");
                alert.show();
            }

            loadTable();

            viewId.setText("");
            viewName.setText("");
            viewEmail.setText("");
            viewPassword.setText("");
            editImageView.setImage(null);
            editImageByte=null;
        }

    }

    @FXML
    void EmployeeReportsPageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee_reports.fxml"))));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ItemPageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/item.fxml"))));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void OrderPageOnAction(ActionEvent event) {
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
    }
}








