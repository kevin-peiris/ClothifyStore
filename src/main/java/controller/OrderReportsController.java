package controller;

import dto.Employee;
import dto.Order;
import dto.OrderDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.EmployeeService;
import service.custom.OrderDetailsService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderReportsController implements Initializable {
    OrderService orderService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    EmployeeService employeeService= ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
    OrderDetailsService orderDetailsService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER_DETAILS);

    @FXML
    private TableView<OrderDetails> cartTbl;

    @FXML
    private ImageView cartViewImage;

    @FXML
    private TableColumn<?, ?> colCartId;

    @FXML
    private TableColumn<?, ?> colCartName;

    @FXML
    private TableColumn<?, ?> colCartPrice;

    @FXML
    private TableColumn<?, ?> colCartQty;

    @FXML
    private TableColumn<?, ?> colCartSize;

    @FXML
    private TableColumn<?, ?> colCartTotal;

    @FXML
    private TableColumn<?, ?> colOrderCusEmail;

    @FXML
    private TableColumn<?, ?> colOrderDateAndTime;

    @FXML
    private TableColumn<?, ?> colOrderEmpId;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private ImageView editImageView;

    @FXML
    private ComboBox<?> empList;

    @FXML
    private Label lblDateAndTime;

    @FXML
    private Label lblDiscount;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblSubTotal;

    @FXML
    private TableView<Order> ordersTbl;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField viewItemId;

    @FXML
    private TextField viewItemName;

    @FXML
    private TextField viewItemPrice;

    @FXML
    private TextField viewItemQty;

    @FXML
    private TextField viewName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrderTable();

        ordersTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setOrderViewFields(newValue);
            }
        });
    }

    private void setOrderViewFields(Order order) {
        txtOrderId.setText(order.getOrderId());
        txtEmpId.setText(order.getEmpId());
        txtEmail.setText(order.getCusEmail());
        /*
        Employee employee=employeeService.searchEmployee(order.getEmpId());
        viewName.setText(employee.getName());
        if (employee.getImage() != null) {
            editImageView.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
        }*/

    }

    private void loadOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colOrderEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colOrderCusEmail.setCellValueFactory(new PropertyValueFactory<>("cusEmail"));

        ordersTbl.setItems(orderService.getAll());
    }

    private void loadCartTable(String orderId) {
        ObservableList<OrderDetails> list = FXCollections.observableArrayList();

        for (OrderDetails orderDetails : orderDetailsService.getAll()) {
            //if (Objects.equals(orderId, orderDetails.getOrderId())){
               // list.add(orderDetails);
           // }
        }
/*
        colCartId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCartSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        cartTbl.setItems(list);*/
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateEmpOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) {

    }

    @FXML
    void EmployeePageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee.fxml"))));
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

    @FXML
    void ItemPageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/item.fxml"))));
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
