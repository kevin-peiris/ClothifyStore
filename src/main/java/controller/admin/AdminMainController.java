package controller.admin;

import dto.Employee;
import dto.Item;
import dto.Order;
import dto.Supplier;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.ItemService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {

    EmployeeService employeeService= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
    OrderService orderService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    ItemService itemService= ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

    @FXML
    private BarChart<String, Number> EmpBarChart;

    @FXML
    private Label lblEmpCount;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblOrderCount;

    @FXML
    private Label lblProfit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Employee> employees = employeeService.getAll();
        ObservableList<Item> items = itemService.getAll();
        ObservableList<Order> orders = orderService.getAll();

        Double profit = 0.0;
        for (Order order : orders) {
            profit+=order.getTotal();
        }

        lblEmpCount.setText(String.valueOf(employees.size()));
        lblItemCount.setText(String.valueOf(items.size()));
        lblOrderCount.setText(String.valueOf(orders.size()));
        lblProfit.setText(profit+"/=");

        XYChart.Series<String, Number> empSeries = new XYChart.Series<>();
        empSeries.setName("Employee Sales Performance");

        for (Employee employee : employees) {
            int orderCount = employee.getOrderCount();
            empSeries.getData().add(new XYChart.Data<>(employee.getName(), orderCount));
        }

        EmpBarChart.getData().add(empSeries);
    }

    @FXML
    void AdminMainPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main.fxml"))));
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
    void EmployeePageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Stage adminStage = new Stage();
                    try {
                        adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main.fxml"))));
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
    void ItemPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_item.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Stage adminStage = new Stage();
                    try {
                        adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main.fxml"))));
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
    void OrderReportsPageOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/order_reports.fxml"))));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You might have unsaved changes. Do you want to exit?");
                if (alert.showAndWait().get() == ButtonType.OK) {
                    Stage adminStage = new Stage();
                    try {
                        adminStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin_main.fxml"))));
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
