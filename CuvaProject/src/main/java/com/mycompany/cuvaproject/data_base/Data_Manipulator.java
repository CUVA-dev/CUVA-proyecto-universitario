package com.mycompany.cuvaproject.data_base;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.security.SecureRandom;

import com.mycompany.cuvaproject.models.User;
import com.mycompany.cuvaproject.models.Student;
import com.mycompany.cuvaproject.models.Subject;
import com.mycompany.cuvaproject.models.Reprobated;

public class Data_Manipulator extends ConnectionMySQL{

    private static String IDvalue;
    public void SetIDvalue(String IDvalue) {this.IDvalue = IDvalue;}
    public String getIDvalue() {return IDvalue;}
    
     // Métodos de la tabla bitacora
    
    public void InsertTableBitacora(String idValue,String action){

        String sql = "Insert into bitacora (FKIDUser,action) values (?,?)";

        try (Connection conn = conectarMySQL()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, idValue);
            pstmt.setString(2, action);

            
            pstmt.executeUpdate();
            System.out.println("se guardo en la bitacora");

        } catch (SQLException e) {
            System.out.println("no se guardo en la bitacora");
            e.printStackTrace();
        }
    }
    public ArrayList<String> ExtractTableBitacora(){
        ArrayList<String> bitacora = new ArrayList<>();

        String sql = "select bita.timee,u.name,u.lastname,bita.action from bitacora bita inner join user u on bita.FKIDUser = u.ID order by bita.timee desc";
        try (Connection conn = conectarMySQL()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
    
                Timestamp timee = rs.getTimestamp("timee");
                String action = rs.getString("action");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");

                String b = "time: " + timee + " --- action: " + action + " --- Name: " + name + " --- Lastname: " + lastname;
                bitacora.add(b);
            };
            System.out.println("se saco de la bitacora");
    } catch (SQLException e) {
        System.out.println("no se saco de la bitacora");
        e.printStackTrace();
    }
    return bitacora;
    }



    // Metodos de la tabla user

    public boolean InsertTableUser(User user){


        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16]; // Tamaño estándar de 16 bytes
        sr.nextBytes(salt);
        String saltEncoded = Base64.getEncoder().encodeToString(salt);

        // Consulta SQL en este caso Insertar
        String sql ="INSERT INTO user (Name,LastName,ID,Email,Password,salt,FKIDPost,rangee) values (?,?,?, ?,SHA2(CONCAT(?, ?), 512),?,?,?)";

        //conecta a la base de datos
        try (Connection conn = conectarMySQL()) {
            
            // prepara la consulta SQL
            PreparedStatement pstmt = conn.prepareStatement(sql);
        
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getID());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, saltEncoded);
            pstmt.setString(7, saltEncoded);
            pstmt.setString(8, user.getPost());
            pstmt.setString(9, user.getRange());

            // el "pstmt" ejecutar la inserción
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("se inserto un nuevo usuario");
                InsertTableBitacora(getIDvalue(),"registro un nuevo usuario :"+user.getID());
            }
            if(user.getRange().equalsIgnoreCase("invitado")){
                sql = "insert into userduration(FKIDUser) values ('"+user.getID()+"')";
                // pstmt.setString(2, user.getID());
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
                System.out.println("se guardo el usuario temporal");
            }

            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }return false;

    }

    // Metodos de la tabla Student

    public void InsertTableStudent(Student stu){

        String sql = "insert into Student (ID,Name,LastName,Career,Tuition) values (?,?,?,?,?)";

        try (Connection conn = conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, stu.getID());
            pstmt.setString(2, stu.getName());
            pstmt.setString(3, stu.getLastName());
            pstmt.setString(4, stu.getCareer());
            pstmt.setString(5, stu.getTuition());

             int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                InsertTableBitacora(getIDvalue(),"Insertó un nuevo estudiante");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodos de la tabla Reprobated
    
    // este metodo no guarda en la bitacora, ya que es automatio con el scanner de reprobados
    public void InsertTableReprobated(Reprobated rep){

        String sql = "insert into Reprobated (FKIDStudent,FKCodeSubject,grade,period) values (?,?,?,?)";

        try (Connection conn = conectarMySQL()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, rep.getIDStudent());
            pstmt.setString(2, rep.getCodeSubject());
            pstmt.setString(3, rep.getGrade());
            pstmt.setString(4, rep.getPeriod());

            pstmt.executeUpdate();
            System.out.println("se guardo el reprobado");

        } catch (SQLException e) {
            System.out.println("no se guardo el reprobado");
            e.printStackTrace();
        }
    }




















    // validaciones

    // por terminar el hash salt y la validacion de login
    public String ValidationLogin(String idValue,String passwordValue){

        String sql = "select ID,SHA2(CONCAT(password,salt), 512) as hashed_password from User where ID = ? and password = SHA2(CONCAT(?, salt), 512)";
        try (Connection conn = conectarMySQL();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

                    pstmt.setString(1, idValue);
                    pstmt.setString(2, passwordValue);

                    ResultSet rs = pstmt.executeQuery();
                 if (rs.next()) {
                    SetIDvalue(idValue);
                    sql = "true";
                    InsertTableBitacora( idValue, "inicio de sesión");
                    }else{
                    sql ="false";
                 }
                 System.out.println( sql);
        }catch (SQLException e) {
            System.err.println("Error al consultar los datos: " + e.getMessage());
        }      
    return sql;          
    
}



 public boolean ValidationRegister(String idValue,String emailValue){

    String sql= "select ID,email from User where ID = ? or Email = ?";
    try(Connection conn = conectarMySQL();
        PreparedStatement pstmt = conn.prepareStatement(sql);){
        pstmt.setString(1, idValue);
        pstmt.setString(2, emailValue);
        ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
        System.out.println("usuario ya existe");
        return true;
             }
        }catch (SQLException e) {
            System.err.println("Error al consultar los datos: " + e.getMessage());
        }
        System.out.println("usuario no existente");
            return false;
    }
}
