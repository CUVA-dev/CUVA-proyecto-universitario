package com.mycompany.cuvaproject.controller;

import com.mycompany.cuvaproject.data_base.Data_Manipulator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class BinnacleController implements Initializable {
    
    @FXML
    private GridPane gridCuerpo; // Vinculación con el FXML
    
    Data_Manipulator ObjDataM = new Data_Manipulator();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosBitacora();
    }    
    
    private void cargarDatosBitacora() {
        List<String> entries = ObjDataM.ExtractTableBitacora();
        
        if (entries == null || entries.isEmpty()) {
            System.out.println("No se recibieron registros de la bitácora.");
            return; 
        }

        int filaActual = 0;

        for (String entry : entries) {
            // El String de tu terminal tiene este formato:
            // "time: 2026-07-15 08:18:12.0 --- action: inicio de sesión --- Name: Piterson --- Lastname: Alvarado"
            // Por ende, el split correcto debe hacerse por el separador " --- "
            String[] datos = entry.split(" --- "); 
            
            if (datos.length >= 4) {
                // Extraemos los valores y les quitamos los prefijos ("time: ", "action: ", "Name: ", "Lastname: ")
                String fecha = datos[0].replace("time:", "").trim();
                String accion = datos[1].replace("action:", "").trim();
                String nombre = datos[2].replace("Name:", "").trim();
                String apellido = datos[3].replace("Lastname:", "").trim();

                // Si no hay nombre registrado (vienen vacíos), ponemos un guión
                if (nombre.isEmpty()) nombre = "-";
                if (apellido.isEmpty()) apellido = "-";

                // Creamos las etiquetas con el estilo CSS
                Label lblFecha = new Label(fecha);
                lblFecha.getStyleClass().add("label-celda");
                lblFecha.setMaxWidth(Double.MAX_VALUE);

                Label lblNombre = new Label(nombre);
                lblNombre.getStyleClass().add("label-celda");
                lblNombre.setMaxWidth(Double.MAX_VALUE);

                Label lblApellido = new Label(apellido);
                lblApellido.getStyleClass().add("label-celda");
                lblApellido.setMaxWidth(Double.MAX_VALUE);

                Label lblAccion = new Label(accion);
                lblAccion.getStyleClass().add("label-celda");
                lblAccion.setMaxWidth(Double.MAX_VALUE);


                gridCuerpo.add(lblFecha, 0, filaActual);
                gridCuerpo.add(lblNombre, 1, filaActual);
                gridCuerpo.add(lblApellido, 2, filaActual);
                gridCuerpo.add(lblAccion, 3, filaActual);

                filaActual++;
            } else {
                System.out.println("Registro omitido en la UI por formato incorrecto: " + entry);
            }
        }
    }
}
