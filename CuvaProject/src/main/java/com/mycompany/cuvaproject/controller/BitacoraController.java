
package com.mycompany.cuvaproject.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class BitacoraController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}


for (String entry : ObjDataM.ExtractTableBitacora(ObjCMySQL)) {
            System.out.println(entry);
        }
