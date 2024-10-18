package controller;

import dto.Employee;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeReportsController implements Initializable {
    EmployeeService employeeService= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private ImageView editImageView;

    @FXML
    private PieChart empChart;

    @FXML
    private TableView<Employee> tbl;

    @FXML
    private TextField viewEmail;

    @FXML
    private TextField viewId;

    @FXML
    private TextField viewName;

    @FXML
    private TextField viewPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        loadPieChart();

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
        if (employee.getImage()!=null){
            editImageView.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
        }
    }

    private void loadTable(){
        colId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tbl.setItems(employeeService.getAll());
    }

    private void loadPieChart() {
        empChart.getData().clear();
        ObservableList<Employee> employees = employeeService.getAll();

        for (Employee employee : employees) {
            //int orderCount = employeeService.getOrderCount(employee);
            //PieChart.Data slice = new PieChart.Data(employee.getName() + " - " + orderCount + " orders", orderCount);
            //empChart.getData().add(slice);
        }
    }



    @FXML
    void EmployeePageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee.fxml"))));
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
