package com.mycompany.cuvaproject.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene; 
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage; 

public class VentanaController implements Initializable {
    
    @FXML
    private BorderPane mainContainer; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeView("/com/mycompany/cuvaproject/estudiantes.fxml"); 
    }
    
    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleMinimizar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    
    @FXML
    private void showStudients(ActionEvent event) {
        changeView("/com/mycompany/cuvaproject/estudiantes.fxml");
    }
    
    @FXML
    private void showProtocol(ActionEvent event) {
        changeView("/com/mycompany/cuvaproject/protocols.fxml");
    }
    
    @FXML
    private void showBinnacle(ActionEvent event) {
        changeView("/com/mycompany/cuvaproject/Bitacora.fxml");
    }
    
    @FXML
    private void showCreateUser(ActionEvent event) {
        changeView("/com/mycompany/cuvaproject/register.fxml");
    }
    
    @FXML
    private void closeSesion(ActionEvent event) {
        try {
            System.out.println("[Navbar] Cerrando sesión por completo...");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/cuvaproject/login.fxml"));
            Parent mainView = loader.load();
            
            Stage currentStage = (Stage) mainContainer.getScene().getWindow();
            
            Scene newScene = new Scene(mainView);
            
            currentStage.setScene(newScene);
            currentStage.centerOnScreen(); 
            
            System.out.println("[Navbar] Sesión cerrada con éxito y menú destruido.");
            
        } catch (IOException e) {
            System.err.println("[Error] No se pudo cargar el archivo main.fxml al cerrar sesión.");
            e.printStackTrace();
        }
    }

    private void changeView(String fxmlPath) {
        try {
            System.out.println("[Navbar] Intentando cambiar vista interna a: " + fxmlPath);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subView = loader.load();
            
            if (subView instanceof javafx.scene.layout.Region) {
                javafx.scene.layout.Region region = (javafx.scene.layout.Region) subView;
                
                region.setMinWidth(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
                region.setMinHeight(javafx.scene.layout.Region.USE_COMPUTED_SIZE);
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
                
                region.setPrefWidth(mainContainer.getCenter() != null ? mainContainer.getCenter().getBoundsInLocal().getWidth() : 1000);
                region.setPrefHeight(mainContainer.getCenter() != null ? mainContainer.getCenter().getBoundsInLocal().getHeight() : 600);
            }

            mainContainer.setCenter(subView);
            System.out.println("[Navbar] ¡Vista interna cambiada con éxito!");
            
        } catch (IOException e) {
            System.err.println("[Error] No se pudo cargar el archivo FXML en la ruta: " + fxmlPath);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("[Error] La ruta proporcionada es nula o el archivo no existe en resources.");
        }
    }
}