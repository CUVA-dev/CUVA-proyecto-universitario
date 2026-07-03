package com.mycompany.cuvaproject.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.mycompany.cuvaproject.models.Binnacle;


public class BinnacleController implements Initializable {

    @FXML private TableView<Binnacle> tblBitacora;
    @FXML private TableColumn<Binnacle, String> colCedula; 
    @FXML private TableColumn<Binnacle, String> colNombre;
    @FXML private TableColumn<Binnacle, String> colApellido;
    @FXML private TableColumn<Binnacle, String> colRol;
    @FXML private TableColumn<Binnacle, String> colFechaHora;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
}
