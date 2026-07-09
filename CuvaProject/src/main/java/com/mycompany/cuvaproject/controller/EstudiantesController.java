package com.mycompany.cuvaproject.controller;

import com.mycompany.cuvaproject.services.ProcesadorRecord;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class EstudiantesController implements Initializable {
    @FXML
    private Label lblSuccess;
    
    @FXML
    private Button btnEscanear;

    @FXML // Anotación obligatoria para enlazar con el FXML
    private void actionScann(ActionEvent event) {
        
        try {
            System.out.println("[EstudiantesController] Presionado botón escanear...");

            // 1. Conseguimos el Stage actual usando el botón
            Stage currentStage = (Stage) btnEscanear.getScene().getWindow();

            // 2. BUSQUEDA JERÁRQUICA: Escalamos en la vista para encontrar al mainContainer
            BorderPane mainContainer = null;
            javafx.scene.Parent padre = btnEscanear.getParent();
            while (padre != null) {
                if (padre instanceof BorderPane) {
                    mainContainer = (BorderPane) padre;
                    break; 
                }
                padre = padre.getParent();
            }

            if (mainContainer != null) {
                System.out.println("[EstudiantesController] mainContainer detectado con éxito.");
            }

            // 3. Configuramos el FileChooser para seleccionar el Récord Académico
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar Récord Académico (PDF)");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf")
            );

            // 4. Abrimos el selector de archivos
            File archivoSeleccionado = fileChooser.showOpenDialog(currentStage);

            if (archivoSeleccionado != null) {

                ProcesadorRecord procesador = new ProcesadorRecord();
                
                // NUEVO: Obtener la ruta del directorio "Downloads" del usuario actual de manera dinámica
                String userHome = System.getProperty("user.home");
                String rutaDescargas = userHome + File.separator + "Downloads" + File.separator;
                
                // 6. Ejecutamos tu método maestro pasándole la ruta del archivo y el directorio de descargas
                procesador.resultado(archivoSeleccionado.getAbsolutePath(), rutaDescargas);
                
                System.out.println("[EstudiantesController] ¡Proceso de análisis finalizado!");
                lblSuccess.setText("Proceso finalizado con éxito. Reporte guardado en Descargas.");
                lblSuccess.setVisible(true);
            } else {
                System.out.println("[EstudiantesController] Selección de archivo cancelada por el usuario.");
            }

        } catch (Exception e) {
            System.err.println("[Error] No se pudo ejecutar el proceso de escaneo.");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializador limpio
    }    

}