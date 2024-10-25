package controller.admin;

import dto.Item;
import dto.Supplier;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ServiceFactory;
import service.custom.ItemService;
import service.custom.SupplierService;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AdminItemController implements Initializable {
    SupplierService supplierService= ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
    ItemService itemService= ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colItemPrice;

    @FXML
    private TableColumn<?, ?> colSupCompany;

    @FXML
    private TableColumn<?, ?> colSupID;

    @FXML
    private TableColumn<?, ?> colSupName;

    @FXML
    private TableView<Item> itemTbl;

    @FXML
    private ComboBox<String> sizeList;

    @FXML
    private ComboBox<String> supList;

    @FXML
    private TableView<Supplier> supplierTbl;

    @FXML
    private TextField txtItemId;

    @FXML
    private TextField txtItemQty;

    @FXML
    private TextField txtItemName;

    @FXML
    private TextField txtItemPrice;

    @FXML
    private TextField txtSupCompany;

    @FXML
    private TextField txtSupEmail;

    @FXML
    private TextField txtSupId;

    @FXML
    private TextField txtSupName;

    @FXML
    private TextField viewItemId;

    @FXML
    private TextField viewItemName;

    @FXML
    private TextField viewItemPrice;

    @FXML
    private TextField viewItemQty;

    @FXML
    private ComboBox<String> viewSizeList;

    @FXML
    private TextField viewSupCompany;

    @FXML
    private TextField viewSupEmail;

    @FXML
    private TextField viewSupId;

    @FXML
    private ComboBox<String> viewSupList;

    @FXML
    private TextField viewSupName;

    @FXML
    private ImageView inpImageView;

    @FXML
    private ImageView editImageView;

    private byte[] inpImageByte=null;
    private byte[] editImageByte=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateSupId();
        loadSupplierTable();

        generateItemId();
        loadItemTable();

        loadComboBox();

        supplierTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setSupViewFields(newValue);
            }
        });

        itemTbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setItemViewFields(newValue);
            }
        });

    }

    private void loadComboBox() {
        ObservableList<String> supplierList= FXCollections.observableArrayList();
        ObservableList<Supplier> supplierAll = supplierService.getAll();
        if(!supplierAll.isEmpty()){
            for (Supplier supplier:supplierService.getAll()){
                supplierList.add(supplier.getName());
            }
        }

        ObservableList<String> sizesList= FXCollections.observableArrayList();
        sizesList.add("XS");
        sizesList.add("S");
        sizesList.add("M");
        sizesList.add("XL");
        sizesList.add("XXL");

        viewSupList.setItems(supplierList);
        viewSizeList.setItems(sizesList);
        supList.setItems(supplierList);
        sizeList.setItems(sizesList);
    }

    private void setSupViewFields(Supplier supplier) {
        viewSupId.setText(supplier.getSupId());
        viewSupName.setText(supplier.getName());
        viewSupEmail.setText(supplier.getEmail());
        viewSupCompany.setText(supplier.getCompany());
    }

    private void setItemViewFields(Item item) {
        viewItemId.setText(item.getItemId());
        viewItemName.setText(item.getName());
        viewItemPrice.setText(String.valueOf(item.getPrice()));
        viewItemQty.setText(String.valueOf(item.getQty()));
        viewSizeList.setValue(item.getSize());
        viewSupList.setValue(item.getSupId());
        if (item.getImage() != null) {
            editImageView.setImage(new Image(new ByteArrayInputStream(item.getImage())));
        }
    }

    private void generateSupId(){
        int lastSupCount=0;

        ObservableList<Supplier> supplierList = supplierService.getAll();
        if (!supplierList.isEmpty()) {
            Supplier lastSupplier = supplierList.getLast();

            String lastSupId = lastSupplier.getSupId();

            String lastStringSupCount = lastSupId.substring(lastSupId.length() - 3);

            lastSupCount = Integer.parseInt(lastStringSupCount);
        }

        String id=String.format("S%03d",lastSupCount+1);
        txtSupId.setText(id);
    }

    private void loadSupplierTable(){
        colSupID.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupCompany.setCellValueFactory(new PropertyValueFactory<>("company"));

        supplierTbl.setItems(supplierService.getAll());
    }

    private void generateItemId(){
        int lastItemCount=0;

        ObservableList<Item> itemList = itemService.getAll();
        if (!itemList.isEmpty()) {
            Item lastItem = itemList.getLast();
            String lastItemId = lastItem.getItemId();
            String lastStringItemCount = lastItemId.substring(lastItemId.length() - 3);
            lastItemCount = Integer.parseInt(lastStringItemCount);
        }

        String id=String.format("I%03d",lastItemCount+1);
        txtItemId.setText(id);
    }

    private void loadItemTable(){
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        itemTbl.setItems(itemService.getAll());
    }

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
    void btnAddItemOnAction(ActionEvent event) {
        if (txtItemName.getText().isEmpty() || txtItemPrice.getText().isEmpty() || txtItemQty.getText().isEmpty() || supList.getValue()==null || sizeList.getValue()==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        } else if (!isNumeric(txtItemPrice.getText()) || !isNumeric(txtItemQty.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Numeric Input");
            alert.show();
        } else{

            Item item = new Item(txtItemId.getText(), txtItemName.getText(),supList.getValue(), Double.parseDouble(txtItemPrice.getText()), Integer.parseInt(txtItemQty.getText()),sizeList.getValue(),inpImageByte);
            System.out.println(item);

            if (itemService.addItem(item)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Added Successfully");
                alert.show();
            }

            generateItemId();
            loadItemTable();

            txtItemName.setText("");
            txtItemQty.setText("");
            txtItemPrice.setText("");
            supList.setValue(null);
            sizeList.setValue(null);
            inpImageView.setImage(null);
            inpImageByte=null;

            txtItemName.requestFocus();
        }
    }

    @FXML
    void btnAddSupOnAction(ActionEvent event) {
        if (txtSupName.getText().isEmpty() || txtSupCompany.getText().isEmpty() || txtSupEmail.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else if(!isValidEmail(txtSupEmail.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Email!");
            alert.show();
            txtSupEmail.selectAll();
        }else{

            Supplier supplier = new Supplier(txtSupId.getText(),txtSupName.getText(),txtSupCompany.getText(),txtSupEmail.getText());
            System.out.println(supplier);

            if (supplierService.addSupplier(supplier)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Supplier Added Successfully");
                alert.show();
            }

            generateSupId();
            loadSupplierTable();

            loadComboBox();

            txtSupName.setText("");
            txtSupCompany.setText("");
            txtSupEmail.setText("");

            txtSupName.requestFocus();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {
        Item selectedItem = itemTbl.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an item to delete.");
            alert.show();
            return;
        }

        if (itemService.deleteItem(selectedItem)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully");
            alert.show();
        }

        loadItemTable();

        viewItemId.setText("");
        viewItemName.setText("");
        viewItemPrice.setText("");
        viewItemQty.setText("");
        viewSupList.setValue(null);
        viewSizeList.setValue(null);
        editImageView.setImage(null);

    }

    @FXML
    void btnDeleteSupOnAction(ActionEvent event) {
        Supplier selectedSupplier = supplierTbl.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an supplier to delete.");
            alert.show();
            return;
        }

        if (supplierService.deleteSupplier(selectedSupplier)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully");
            alert.show();
        }

        loadSupplierTable();

        loadComboBox();

        viewSupId.setText("");
        viewSupName.setText("");
        viewSupCompany.setText("");
        viewSupEmail.setText("");

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
    void btnUpdateItemOnAction(ActionEvent event) {
        Item selectedItem = itemTbl.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an Item to update.");
            alert.show();
            return;
        }

        if (viewItemName.getText().isEmpty() || viewItemPrice.getText().isEmpty() || viewItemQty.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else{
            Item item=null;

            if (editImageByte==null){
                item = new Item(viewItemId.getText(), viewItemName.getText(),viewSupList.getValue(), Double.parseDouble(viewItemPrice.getText()), Integer.parseInt(viewItemQty.getText()),viewSizeList.getValue(),selectedItem.getImage());
            }else{
                item = new Item(viewItemId.getText(), viewItemName.getText(),viewSupList.getValue(), Double.parseDouble(viewItemPrice.getText()), Integer.parseInt(viewItemQty.getText()),viewSizeList.getValue(),editImageByte);
            }

            System.out.println(item);

            if (itemService.updateItem(item)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item Updated Successfully");
                alert.show();
            }

            loadItemTable();

            viewItemId.setText("");
            viewItemName.setText("");
            viewItemPrice.setText("");
            viewItemQty.setText("");
            viewSupList.setValue(null);
            viewSizeList.setValue(null);
            editImageView.setImage(null);
            editImageByte=null;
        }
    }

    @FXML
    void btnUpdateSupOnAction(ActionEvent event) {
        Supplier selectedSupplier = supplierTbl.getSelectionModel().getSelectedItem();

        if (selectedSupplier == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an Supplier to update.");
            alert.show();
            return;
        }

        if (viewSupName.getText().isEmpty() || viewSupEmail.getText().isEmpty() || viewSupCompany.getText().isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Empty Field or Fields");
            alert.show();
        }else if(!isValidEmail(txtSupEmail.getText())) {
            Alert alert=new Alert(Alert.AlertType.WARNING,"Invalid Email!");
            alert.show();
            txtSupEmail.selectAll();
        }else{

            Supplier supplier = new Supplier(viewSupId.getText(),viewSupName.getText(),viewSupCompany.getText(),viewSupEmail.getText());
            System.out.println(supplier);

            if (supplierService.updateSupplier(supplier)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully");
                alert.show();
            }

            loadSupplierTable();

            loadComboBox();

            viewSupId.setText("");
            viewSupName.setText("");
            viewSupCompany.setText("");
            viewSupEmail.setText("");
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
