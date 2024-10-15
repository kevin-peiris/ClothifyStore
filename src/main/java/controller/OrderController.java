package controller;

import dto.CartTM;
import dto.Item;
import dto.Order;
import dto.OrderDetails;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.ItemService;
import service.custom.OrderService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class OrderController implements Initializable {
    OrderService orderService= ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    ItemService itemService= ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

    private ObservableList<CartTM> cartTMObservableList= FXCollections.observableArrayList();

    @FXML
    private TableView<CartTM> cartTbl;

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
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colItemPrice;

    @FXML
    private TableColumn<?, ?> colItemQty;

    @FXML
    private TableColumn<?, ?> colItemSize;

    @FXML
    private TableView<Item> itemTbl;

    @FXML
    private Label lblDateAndTime;

    @FXML
    private Label lblDiscount;

    @FXML
    private Label lblItemCount;

    @FXML
    private Label lblSubTotal;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEmpId;

    @FXML
    private TextField txtItemId;

    @FXML
    private TextField txtItemName;

    @FXML
    private TextField txtItemPrice;

    @FXML
    private TextField txtItemQty;

    @FXML
    private TextField txtItemSize;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField viewItemId;

    @FXML
    private TextField viewItemName;

    @FXML
    private ImageView itemViewImage;

    @FXML
    private ImageView cartViewImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTimeandDate();
        generateOrderId();
        loadItemTable();

        itemTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setItemViewFields(newValue);
            }
        });

        cartTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDeleteBtn(newValue);
            }
        });
    }

    private void loadTimeandDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(date);


        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO , e ->{
            LocalTime now = LocalTime.now();
            lblDateAndTime.setText("Date :- "+s+"   Time :- "+now.getHour() + " : " + now.getMinute() + " : " + now.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    private void generateOrderId(){
        int lastOrderCount=0;

        ObservableList<Order> orderList = orderService.getAll();
        if (!orderList.isEmpty()) {
            Order lastOrder = orderList.getLast();

            String lastOrderId = lastOrder.getOrderId();

            String lastStringOrderCount = lastOrderId.substring(lastOrderId.length() - 3);

            lastOrderCount = Integer.parseInt(lastStringOrderCount);
        }

        String id=String.format("O%03d",lastOrderCount+1);
        txtOrderId.setText(id);
    }

    private void loadItemTable(){
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colItemSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        itemTbl.setItems(itemService.getAll());
    }

    private void setItemViewFields(Item item) {
        txtItemId.setText(item.getItemId());
        txtItemName.setText(item.getName());
        txtItemPrice.setText(String.valueOf(item.getPrice()));
        txtItemQty.setText(String.valueOf(item.getQty()));
        txtItemSize.setText(item.getSize());
        itemViewImage.setImage(new Image(new ByteArrayInputStream(item.getImage())));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        if (txtItemId.getText().isEmpty()){
            Alert alert=new Alert(Alert.AlertType.WARNING,"Select a Item to add");
            alert.show();
        } else if (txtQty.getText().isEmpty()) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Add a Quantity");
            alert.show();
            txtQty.requestFocus();
        } else if (!isNumeric(txtQty.getText()) || Integer.parseInt(txtQty.getText())<=0) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Quantity");
            alert.show();
            txtQty.selectAll();
        } else if (Integer.parseInt(txtItemQty.getText()) < Integer.parseInt(txtQty.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Cannot order more than Quantity On Hand");
            alert.show();
            txtQty.selectAll();
        } else {

            colCartId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
            colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCartQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colCartTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colCartSize.setCellValueFactory(new PropertyValueFactory<>("size"));

            String itemId = txtItemId.getText();
            String name = txtItemName.getText();
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtItemPrice.getText());
            String size = txtItemSize.getText();
            double total = qty * price;

            Item selectedItem = itemTbl.getSelectionModel().getSelectedItem();
            int availableQty = selectedItem.getQty() - qty;
            selectedItem.setQty(availableQty);
            itemTbl.refresh();

            cartTMObservableList.add(new CartTM(itemId,name,qty,price,size,total,selectedItem.getImage()));
            calcNetTotal();

            cartTbl.setItems(cartTMObservableList);
            lblItemCount.setText("Item Count :- "+cartTMObservableList.size());

            generateOrderId();

            txtItemId.setText("");
            txtItemName.setText("");
            txtItemQty.setText("");
            txtItemSize.setText("");
            txtItemPrice.setText("");
            txtQty.setText("");
            itemViewImage.setImage(null);
        }
    }

    private void calcNetTotal(){
        Double total=0.0;

        for (CartTM cartTM: cartTMObservableList){
            total+=cartTM.getTotal();
        }
        lblSubTotal.setText("Sub Total :- "+total.toString()+"/=");
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        if(cartTMObservableList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No items in the cart to place an order.");
            alert.show();
        }else if (txtEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter Customer Email");
            alert.show();
            txtEmail.requestFocus();
        }else if(!isValidEmail(txtEmail.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Email!");
            alert.show();
            txtEmail.selectAll();
        }else {
            for (CartTM cartItem : cartTMObservableList) {
                Item dbItem = itemService.searchItem(cartItem.getItemId());
                int newQty = dbItem.getQty() - cartItem.getQty();
                dbItem.setQty(newQty);
                itemService.updateItem(dbItem);
            }

            List<OrderDetails> orderDetailsList=new ArrayList<>();

            cartTMObservableList.forEach(obj->{
                orderDetailsList.add(new OrderDetails(txtOrderId.getText(),obj.getItemId(),obj.getQty(), obj.getPrice()));
            });

            Order order = new Order(txtOrderId.getText(), LocalDateTime.now(), txtEmpId.getText(),txtEmail.getText(), orderDetailsList);
            orderService.placeOrder(order);

            cartTMObservableList.clear();
            itemTbl.refresh();

            lblItemCount.setText("Item Count :- 0");
            lblSubTotal.setText("Sub Total :- 0.0/=");

            txtItemId.setText("");
            txtItemName.setText("");
            txtItemQty.setText("");
            txtItemSize.setText("");
            txtItemPrice.setText("");
            txtQty.setText("");

            viewItemId.setText("");
            viewItemName.setText("");
            cartViewImage.setImage(null);

            generateOrderId();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!");
            alert.show();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private void setDeleteBtn(CartTM cartItem) {
        viewItemId.setText(cartItem.getItemId());
        viewItemName.setText(cartItem.getName());
        cartViewImage.setImage(new Image(new ByteArrayInputStream(cartItem.getImage())));
    }

    @FXML
    void btnDeleteCartItemOnAction(ActionEvent event) {
        CartTM selectedItem = cartTbl.getSelectionModel().getSelectedItem();
        if (selectedItem!=null) {
            for (Item item : itemTbl.getItems()) {
                if (item.getItemId().equals(selectedItem.getItemId())) {
                    int updatedQty = item.getQty() + selectedItem.getQty();
                    item.setQty(updatedQty);
                    itemTbl.refresh();
                    break;
                }
            }
            cartTMObservableList.remove(selectedItem);
            viewItemId.setText("");
            viewItemName.setText("");
            cartViewImage.setImage(null);

            calcNetTotal();
            lblItemCount.setText("Item Count :- " + cartTMObservableList.size());
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING,"Select a table row to delete");
            alert.show();
        }
    }

    @FXML
    void EmployeePageOnAction(ActionEvent event) {
        Stage stage=new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/employee.fxml"))));
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
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
