package controller.admin;

import com.jfoenix.controls.JFXTextField;
import controller.employee.OrderController;
import dto.CartTM;
import dto.Item;
import dto.Order;
import dto.OrderDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.ItemService;
import service.custom.OrderDetailsService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderReportsController implements Initializable {
    OrderService orderService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    OrderDetailsService orderDetailsService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER_DETAILS);
    ItemService itemService= ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

    @FXML
    private TableView<OrderDetails> cartTbl;

    @FXML
    private ImageView cartViewImage;

    @FXML
    private TableColumn<?, ?> colCartId;

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
    private Label lblDateAndTime;

    @FXML
    private Label lblEmpId;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblSubTotal;

    @FXML
    private TableView<Order> ordersTbl;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtItemId;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtItemPrice;

    @FXML
    private JFXTextField txtItemQty;

    @FXML
    private JFXTextField txtItemSize;

    @FXML
    private JFXTextField txtQty;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadOrderTable();

        ordersTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCartTable(newValue);
            }
        });

        cartTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setItemViewFields(newValue);
            }
        });
    }

    private void loadOrderTable(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colOrderCusEmail.setCellValueFactory(new PropertyValueFactory<>("cusEmail"));
        colOrderDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        ordersTbl.setItems(orderService.getAll());
    }

    private void loadCartTable(Order order) {
        lblDateAndTime.setText(String.valueOf(order.getDateTime()));
        lblEmpId.setText(order.getEmpId());
        lblOrderId.setText(order.getOrderId());
        lblSubTotal.setText("Sub Total :- "+order.getTotal()+"/=");
        txtEmail.setText(order.getCusEmail());

        ObservableList<OrderDetails> allOrderDetails = orderDetailsService.getAll();
        System.out.println(allOrderDetails);

        ObservableList<OrderDetails> filteredOrderDetails = FXCollections.observableArrayList();

        for (OrderDetails orderDetails : allOrderDetails) {
            if (orderDetails.getOrderId().equals(order.getOrderId())) {
                filteredOrderDetails.add(orderDetails);
            }
        }

        lblItemCount.setText("Item Count :- "+filteredOrderDetails.size());

        colCartId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCartSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colCartTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cartTbl.setItems(filteredOrderDetails);

    }

    private void setItemViewFields(OrderDetails orderDetail) {
        txtItemId.setText(orderDetail.getItemId());
        txtItemPrice.setText(String.valueOf(orderDetail.getPrice()));
        txtItemQty.setText(String.valueOf(orderDetail.getQty()));
        txtItemSize.setText(orderDetail.getSize());

        Item item=itemService.searchItem(orderDetail.getItemId());
        txtItemName.setText(item.getName());
        if (item.getImage()!=null){
            cartViewImage.setImage(new Image(new ByteArrayInputStream(item.getImage())));
        }
    }

    @FXML
    void btnDeleteOrderOnAction(ActionEvent event) {
        Order selectedOrder = ordersTbl.getSelectionModel().getSelectedItem();
        if (selectedOrder==null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an Order to delete!");
            alert.show();
        }else {
            ObservableList<OrderDetails> allOrderDetails = orderDetailsService.getAll();

            for (OrderDetails orderDetails : allOrderDetails) {
                if (orderDetails.getOrderId().equals(selectedOrder.getOrderId())) {
                    orderDetailsService.deleteOrderDetails(orderDetails);
                }
            }

            orderService.deleteOrder(selectedOrder);

            txtItemId.setText("");
            txtItemPrice.setText("");
            txtItemQty.setText("");
            txtItemSize.setText("");
            txtItemName.setText("");

            cartViewImage.setImage(null);

            lblDateAndTime.setText("");
            lblEmpId.setText("EmpId :-");
            lblOrderId.setText("OrderId :-");
            lblSubTotal.setText("Sub Total :- ");
            txtEmail.setText("");
            lblItemCount.setText("Item Count :- ");

            cartTbl.getItems().clear();
            loadOrderTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order deleted successfully!");
            alert.show();
        }
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



