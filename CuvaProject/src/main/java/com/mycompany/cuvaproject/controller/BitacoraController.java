
package com.mycompany.cuvaproject.controller;

import com.mycompany.cuvaproject.data_base.ConnectionMySQL;
import com.mycompany.cuvaproject.data_base.Data_Manipulator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class BitacoraController implements Initializable {
    ConnectionMySQL CMySQL = new ConnectionMySQL();
    Data_Manipulator ObjDataM = new Data_Manipulator();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}


for (String entry : ObjDataM.ExtractTableBitacora(ObjCMySQL)) {
            System.out.println(entry);
        }
