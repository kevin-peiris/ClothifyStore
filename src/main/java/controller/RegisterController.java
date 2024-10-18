package controller;

import dto.Admin;
import dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import service.ServiceFactory;
import service.custom.UserService;
import util.RoleType;
import util.ServiceType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    UserService userService= ServiceFactory.getInstance().getServiceType(ServiceType.USER);

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtOTP;

    @FXML
    private TextField txtPassword;

    @FXML
    private ComboBox<String> roleList;

    private String generatedOTP;

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String email = txtEmail.getText();
        String enteredOTP = txtOTP.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || enteredOTP.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields are required!");
            alert.show();
            return;
        }else if (!enteredOTP.equals(generatedOTP)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid OTP. Please try again.");
            alert.show();
            return;
        }else if (password.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Password must be at least 8 characters long.");
            alert.show();
            return;
        }

        new Admin(txtEmail.getText(),txtPassword.getText(),roleList.getSelectionModel().getSelectedItem());

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful!");
        alert.show();

        txtEmail.clear();
        txtOTP.clear();
        txtPassword.clear();
    }

    @FXML
    void btnSendOTPOnAction(ActionEvent event) {
        String email = txtEmail.getText();

        if (email.isEmpty() || !email.contains("@")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid email.");
            alert.show();
            return;
        }

        generatedOTP = generateOTP();

        try {
            sendOTPViaEmail(email, generatedOTP);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "OTP has been sent to your email.");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to send OTP. Please try again.");
            alert.show();
        }
    }

    private String generateOTP() {
        int otp = (int) (Math.random() * 9000) + 1000;
        System.out.println("OTP : "+otp);
        return String.valueOf(otp);
    }

    private void sendOTPViaEmail(String recipientEmail, String otp) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String senderEmail = "#";
        String senderPassword = "#";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        Transport.send(message);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> roles= FXCollections.observableArrayList();
        roles.add("Owner");
        roles.add("Manager");

        roleList.setItems(roles);
    }
}
