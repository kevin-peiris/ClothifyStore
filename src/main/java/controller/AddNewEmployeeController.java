package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import dto.Employee;
import javafx.collections.ObservableList;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.cell.PropertyValueFactory;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddNewEmployeeController implements Initializable {
    EmployeeService service= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

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

            Employee employee = new Employee(txtId.getText(), txtName.getText(), txtEmail.getText(), txtPassword.getText());
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
        viewPassword.setText(employee.getPassword());
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

            Employee employee = new Employee(viewId.getText(), viewName.getText(), viewEmail.getText(), viewPassword.getText());
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
        }

    }
}
