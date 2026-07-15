package com.mycompany.cuvaproject.controller;

import com.mycompany.cuvaproject.data_base.Data_Manipulator;

import java.io.IOException;  
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
    Data_Manipulator ObjDataM = new Data_Manipulator();
        
    @FXML
    private Button btnIngresar;
    @FXML
    private Button BtnRegresar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    
    @FXML
    private TextField id;
    
    @FXML
    private TextField password;
    
    @FXML
    private void handleUser(ActionEvent event){
    
        String idValue = id.getText();
        String passwordValue = password.getText();
    
    
        if (idValue.isEmpty() || passwordValue.isEmpty()) {
            throw new IllegalArgumentException("los campos no pueden estar vacios");
        }
        if(ObjDataM.ValidationLogin(idValue, passwordValue).equalsIgnoreCase("true")){accionBoton();}
    }
    
    @FXML
    private void accionBoton() {
        try {
            
            
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/cuvaproject/Ventana.fxml"));
            Parent root = loader.load();

            Scene currentScene = btnIngresar.getScene();
            currentScene.setRoot(root);
            
        } catch (IOException e) {
            System.err.println("Error al cargar la siguiente ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void accionRegresar(ActionEvent event){
        
        try{
             
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/cuvaproject/main.fxml"));
            Parent root = loader.load();
            
            Scene currentScene = BtnRegresar.getScene();
            currentScene.setRoot(root);
            
        
        }catch(IOException e){
            
            System.err.println("Error al cargar la siguiente ventana: " + e.getMessage());
            e.printStackTrace();
        }
    
    
    }
}
