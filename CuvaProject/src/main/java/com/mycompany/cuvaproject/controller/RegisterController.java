package com.mycompany.cuvaproject.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException; 

import javafx.animation.PauseTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; 
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import com.mycompany.cuvaproject.services.serviceUser;
import com.mycompany.cuvaproject.models.User;
import com.mycompany.cuvaproject.data_base.Data_Manipulator;

public class RegisterController implements Initializable {
    
    @FXML private Button btnRegister;
    @FXML private Button btnRegresar;
    
    @FXML private TextField name;
    @FXML private TextField lastname;
    @FXML private TextField id;
    @FXML private TextField password;
    @FXML private TextField password_two;
    @FXML private TextField email;
    @FXML private TextField post;
    @FXML private ComboBox<String> cmbRol;

    @FXML private Label lblErrorId;
    @FXML private Label lblErrorName;
    @FXML private Label lblErrorLastName;
    @FXML private Label lblErrorPassword;
    @FXML private Label lblErrorPasswordTwo;
    @FXML private Label lblErrorEmail;
    
    private Label lblSuccess;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbRol.getSelectionModel().select("Admin");
        
        lblSuccess = new Label();
        lblSuccess.setTextFill(Color.GREEN);
        lblSuccess.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblSuccess.setVisible(false);
        lblSuccess.setWrapText(true);
        
        if (btnRegister != null && btnRegister.getParent() instanceof VBox) {
            VBox contenedorTarjeta = (VBox) btnRegister.getParent();
            contenedorTarjeta.getChildren().add(lblSuccess);
        }
        
        setupFocusListener(id, lblErrorId, "ID");
        setupFocusListener(name, lblErrorName, "NAME");
        setupFocusListener(lastname, lblErrorLastName, "LASTNAME");
        setupFocusListener(password, lblErrorPassword, "PASSWORD");
        setupFocusListener(password_two, lblErrorPasswordTwo, "PASSWORD_TWO");
        setupFocusListener(email, lblErrorEmail, "EMAIL");
    }

    private void setupFocusListener(TextField txtField, Label lblError, String type) {
        txtField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                validateField(txtField.getText(), lblError, type);
            }
        });
    }

    private boolean validateField(String value, Label lblError, String type) {
        try {
            switch (type) {
                case "ID":
                    if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("La cédula no puede estar vacía");
                    if (value.length() < 7) throw new IllegalArgumentException("La cédula debe tener un mínimo de 7 caracteres");
                    if (value.length() > 11) throw new IllegalArgumentException("La cédula puede tener un máximo de 11 caracteres");
                    for (int i = 0; i < value.length(); i++) {
                        if (!Character.isDigit(value.charAt(i))) throw new IllegalArgumentException("La cédula solo puede contener números");
                    }
                    break;

                case "NAME":
                    if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
                    if (value.length() < 3) throw new IllegalArgumentException("Mínimo 3 caracteres");
                    if (value.length() > 20) throw new IllegalArgumentException("Máximo 20 caracteres");
                    for (int i = 0; i < value.length(); i++) {
                        if (!Character.isLetter(value.charAt(i))) throw new IllegalArgumentException("No se permiten números ni caracteres especiales");
                    }
                    break;

                case "LASTNAME":
                    if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("El apellido no puede estar vacío");
                    if (value.length() < 3) throw new IllegalArgumentException("Mínimo 3 caracteres");
                    if (value.length() > 20) throw new IllegalArgumentException("Máximo 20 caracteres");
                    for (int i = 0; i < value.length(); i++) {
                        if (!Character.isLetter(value.charAt(i))) throw new IllegalArgumentException("No se permiten números ni caracteres especiales");
                    }
                    break;

                case "PASSWORD":
                    if (value == null || value.isEmpty()) throw new IllegalArgumentException("La contraseña no puede estar vacía");
                    if (value.length() < 8) throw new IllegalArgumentException("Mínimo 8 caracteres");
                    if (value.equals(id.getText())) throw new IllegalArgumentException("No puede ser igual a la cédula");
                    
                    boolean upper = false, digit = false, special = false;
                    for (int i = 0; i < value.length(); i++) {
                        char b = value.charAt(i);
                        if (Character.isSpaceChar(b)) throw new IllegalArgumentException("No se permiten espacios");
                        if (Character.isDigit(b)) digit = true;
                        if (Character.isUpperCase(b)) upper = true;
                        if (!Character.isLetter(b) && !Character.isDigit(b)) special = true;
                    }
                    if (!special || !digit || !upper) {
                        throw new IllegalArgumentException("Debe incluir mayúscula, número y carácter especial");
                    }
                    break;

                case "PASSWORD_TWO":
                    if (!value.equals(password.getText())) {
                        throw new IllegalArgumentException("Las contraseñas no coinciden");
                    }
                    break;

                case "EMAIL":
                    if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("El correo no puede estar vacío");
                    if (!value.contains("@") || !value.contains(".")) {
                        throw new IllegalArgumentException("Formato inválido (falta '@' o '.')");
                    }
                    break;
            }
            
            lblError.setText("");
            lblError.setVisible(false);
            return true;
            
        } catch (IllegalArgumentException e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            if (lblSuccess != null) lblSuccess.setVisible(false);
            return false;
        }
    }

    @FXML
    private void handleUser(ActionEvent event) {
        lblSuccess.setVisible(false);

        boolean isValid = true;
        isValid &= validateField(id.getText(), lblErrorId, "ID");
        isValid &= validateField(name.getText(), lblErrorName, "NAME");
        isValid &= validateField(lastname.getText(), lblErrorLastName, "LASTNAME");
        isValid &= validateField(password.getText(), lblErrorPassword, "PASSWORD");
        isValid &= validateField(password_two.getText(), lblErrorPasswordTwo, "PASSWORD_TWO");
        isValid &= validateField(email.getText(), lblErrorEmail, "EMAIL");

        if (!isValid) {
            System.out.println("Por favor, corrige los errores antes de registrar.");
            return;
        }

        String nameValue = name.getText();
        String lastNameValue = lastname.getText();
        String idValue = id.getText();
        String passwordValue = password.getText();
        String emailValue = email.getText();
        String postValue = post.getText();
        String rolValue = cmbRol.getValue();
        
        if (rolValue == null) {
            System.out.println("Por favor, selecciona un rol.");
            return;
        }
        
        if (create(nameValue, lastNameValue, idValue, passwordValue, emailValue, postValue, rolValue)) { 
            lblSuccess.setText("¡Usuario creado exitosamente!");
            lblSuccess.setTextFill(Color.GREEN);
            lblSuccess.setVisible(true);
        } else {
            lblSuccess.setText("¡error al crear al usuario!");
            lblSuccess.setTextFill(Color.RED);
            lblSuccess.setVisible(true);
        }
    }
    
    private boolean create(String nameValue, String lastNameValue, String idValue, String passwordValue, String emailValue, String postValue, String rolValue) {
        try {
            serviceUser service = new serviceUser();
            User nuevoUsuario = service.create(nameValue, lastNameValue, idValue, passwordValue, emailValue, postValue, rolValue);
            
            Data_Manipulator manipulator = new Data_Manipulator();
            
            boolean seGuardo = manipulator.InsertTableUser(nuevoUsuario);
            return seGuardo;
            
        } catch (Exception e) {
            System.err.println("Error durante la creación: " + e.getMessage());
            return false;
        }
    }
}
