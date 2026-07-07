package com.mycompany.cuvaproject.data_base;
import java.sql.Connection; // maneja la conexión a la base de datos
import java.sql.PreparedStatement;// permite ejecutar consultas SQL con parámetros
import java.sql.ResultSet; // maneja los resultados de las consultas SQL
import java.sql.SQLException; // maneja los errores relacionados con SQL
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mycompany.cuvaproject.models.User;
import com.mycompany.cuvaproject.models.Student;
import com.mycompany.cuvaproject.models.Subject;
import com.mycompany.cuvaproject.models.Reprobated;

public class Data_Manipulator {
    public static ArrayList<String> bitacora = new ArrayList<>();
    
    // Métodos de la tabla bitacora
    
    public void InsertTableBitacora(ConnectionMySQL CMySQL,String idValue,String action){

        String sql = "Insert into bitacora (time,IDUser,action) values (current_timestamp,'"+idValue+"','"+action+"')";

        try (Connection conn = CMySQL.conectarMySQL()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.executeUpdate();
            System.out.println("se guardo en la bitacora");
        } catch (SQLException e) {
            System.err.println("Error al insertar en la bitacora: " + e.getMessage());
        }
    }

    String sql = "select bita.time,u.name,u.lastname,bita.action from bitacora bita inner join user u on bita.IDUser = u.ID";
        try (Connection conn = CMySQL.conectarMySQL()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
    
                Timestamp time = rs.getTimestamp("time");
                String action = rs.getString("action");
                String name = rs.getString("name");
                String lastname = rs.getString("lastname");

                String b = "time: " + time + " --- action: " + action + " --- Name: " + name + " --- Lastname: " + lastname;
                bitacora.add(b);
            };
    
            System.out.println("se saco de la bitacora");
            for (String entry : bitacora) {
                System.out.println(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
    
        }
    
    }
    // Metodos de la tabla user

    public void InsertTableUser(ConnectionMySQL CMySQL,User user){

        // Consulta SQL en este caso Insertar
        String sql = "INSERT INTO user (Name,LastName,ID,Email,Password,IDPost) VALUES ('"+user.getName()+"','"+user.getLastName()+"','"+user.getID()+"','"+user.getEmail()+"','"+user.getPassword()+"','"+user.getPost()+"')";

        //conecta a la base de datos
        try (Connection conn = CMySQL.conectarMySQL()) {
            
            // prepara la consulta SQL
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // el "pstmt" ejecutar la inserción
              int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("se guardo el usuario");
                    InsertTableBitacora(CMySQL,user.getID(),"Insertó un nuevo usuario");
                }
        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
        }

    }

    public void DeleteInTableUser(ConnectionMySQL CMySQL,User user,String idValue){
        // Consulta SQL en este caso Borrar
        String sql = "DELETE FROM user WHERE ID = '"+user.getID()+"'";
        // conecta a la base de datos
        try (Connection conn = CMySQL.conectarMySQL()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
             InsertTableBitacora(CMySQL,idValue,"Eliminó un usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar datos: " + e.getMessage());
        }
    }

    public void modifyTableUser(ConnectionMySQL CMySQL,User user,String idValue){

        String sql = "UPDATE User SET Name = '"+user.getName()+"',LastName = '"+user.getLastName()+"',Email = '"+user.getEmail()+"', Password = '"+user.getPassword()+"', Post = '"+user.getPost()+"' WHERE ID = '"+user.getID()+"'";

        try (Connection conn = CMySQL.conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Modificó un usuario");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar los datos: " + e.getMessage());
        }
    }
    
    // Metodos de la tabla Subject

    public void InsertTableSubject(ConnectionMySQL CMySQL,Subject sub){
        // este metodo no guarda en la bitacora ya que es automatico con el scanner del pensum ()
        String sql = "INSERT INTO Subject (Code,Semester,Unit_Credit,Name) VALUES ('"+sub.getCode()+"','"+sub.getSemester()+"','"+sub.getUnit_credit()+"','"+sub.getName()+"')";
        
        try (Connection conn = CMySQL.conectarMySQL()) {
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar la materia: " + e.getMessage());
        }

    }

    public void DeleteInTableSubject(ConnectionMySQL CMySQL,Subject sub,String idValue){

        String sql = "DELETE FROM Subject WHERE code = '"+sub.getCode()+"'";

        try (Connection conn = CMySQL.conectarMySQL()){
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Eliminó una materia");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar datos de la materia: " + e.getMessage());
        }
    }

    public void modifyTableSubject(ConnectionMySQL CMySQL,Subject sub,String idValue){

        String sql = "UPDATE Subject SET Code='"+sub.getCode()+"',Semester='"+sub.getSemester()+"',Unit_Credit='"+sub.getUnit_credit()+"',Name='"+sub.getName()+"'";
        try (Connection conn = CMySQL.conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Modificó una materia");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar los datos de la materia: " + e.getMessage());
        }
    }

    // Metodos de la tabla Student

    public void InsertTableStudent(ConnectionMySQL CMySQL,Student stu,String idValue){

        String sql = "INSERT INTO Student (ID,Name,LastName,Career,Tuition) VALUES ('"+stu.getID()+"','"+stu.getName()+"','"+stu.getLastName()+"','"+stu.getCareer()+"','"+stu.getTuition()+"')";

        try (Connection conn = CMySQL.conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

             int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Insertó un nuevo estudiante");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el estudiante: " + e.getMessage());
        }
    }

    public void DeleteInTableStudent(ConnectionMySQL CMySQL,Student stu,String idValue){

        String sql = "DELETE FROM Student WHERE ID = '"+stu.getID()+"'";

        try (Connection conn = CMySQL.conectarMySQL())
         {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Eliminó un estudiante");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar datos del estudiante: " + e.getMessage());
        }
    }

    public void modifyTableStudent(ConnectionMySQL CMySQL,Student stu,String idValue){

        String sql = "UPDATE Student SET Name='"+stu.getName()+"',LastName='"+stu.getLastName()+"',Career='"+stu.getCareer()+"',Tuition='"+stu.getTuition()+"' WHERE ID = '"+stu.getID()+"'";
        try (Connection conn = CMySQL.conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                InsertTableBitacora(CMySQL,idValue,"Modificó un estudiante");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar los datos del estudiante: " + e.getMessage());
        }
    }
    
    
    // Metodos de la tabla Reprobated
    
    // este metodo no guarda en la bitacora, ya que es automatio con el scanner de reprobados
    public void InsertTableReprobated(ConnectionMySQL CMySQL,Reprobated rep){

        String sql = "INSERT INTO Reprobated (IDStudent,CodeSubject,grade,period) VALUES ('"+rep.getIDStudent()+"','"+rep.getCodeSubject()+"','"+rep.getGrade()+"','"+rep.getPeriod()+"')";

        try (Connection conn = CMySQL.conectarMySQL()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar el Reprovado: " + e.getMessage());
        }
    }
    
    public void DeleteInTableReprobated(ConnectionMySQL CMySQL,Reprobated Rep){

        String sql = "DELETE FROM reprobated WHERE ID = '"+Rep.getIDStudent()+"'";

        try (Connection conn = CMySQL.conectarMySQL()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Registro eliminado exitosamente.");
            } else {
                System.out.println("No se encontró ningún registro con ese ID.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar datos del reprovado: " + e.getMessage());
        }
    }

    public void modifyTableReprobated(ConnectionMySQL CMySQL,Reprobated rep){

        String sql = "UPDATE Reprobated SET ID='"+rep.getIDStudent()+"',CodeSubject='"+rep.getCodeSubject()+"' WHERE IDStudent = '"+rep.getIDStudent()+"'";
        try (Connection conn = CMySQL.conectarMySQL()){

            PreparedStatement pstmt = conn.prepareStatement(sql);

            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                System.out.println("¡Registro modificado exitosamente!");
            } else {
                System.out.println("No se encontró un registro con ese ID.");
            }

        } catch (SQLException e) {
            System.err.println("Error al modificar los datos: " + e.getMessage());
        }
    }
    





    




    
    //metodo para extraer datos de las tablas

    public void ExtractTableUser(ConnectionMySQL CMySQL,User user){

        String sql = "SELECT * FROM User WHERE ID = '"+user.getID()+"'";
        try (Connection conn = CMySQL.conectarMySQL();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Iterar sobre el ResultSet para extraer los datos
            while (rs.next()) {
                // Extrae datos por el nombre de la columna
                String id = rs.getString("ID");
                String name = rs.getString("Name");
                String lastname = rs.getString("Lastname");
                String email = rs.getString("email"); 

                System.out.println(id + name);
        }

        } catch (SQLException e) {
            System.err.println("Error al consultar los datos: " + e.getMessage());
        }

    }
    
    public void ExtractTableSubject(ConnectionMySQL CMySQL,Subject sub){

    String sql = "SELECT * FROM Subject WHERE Code = '"+sub.getCode()+"'";  

    try(Connection conn = CMySQL.conectarMySQL()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                String code = rs.getString("Code");
                String name = rs.getString("Name");
                int semester = rs.getInt("Semester");
                int unit_credit = rs.getInt("Unit_Credit");
            }
            }catch(SQLException e){
    System.err.println("Error al consultar los datos: " + e.getMessage());
   }
    }

    public void ExtractTableStudent(ConnectionMySQL CMySQL,Student stu){

    String sql = "SELECT * FROM Student WHERE ID= '"+stu.getID()+"'";

    try (Connection conn = CMySQL.conectarMySQL()){
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            
            String name = rs.getString("name");
            String lastname = rs.getString("lastname");
            String career = rs.getString("career");
            String tuition = rs.getString("tuition");
        }  
    } catch (SQLException e) {
        e.printStackTrace();
        
    }



    }
    
    public void ExtractTableReprobated(ConnectionMySQL CMySQL,Reprobated rep){
        String sql = "SELECT stu.ID, stu.Name, sub.Code, sub.name FROM reprobated rep "
        +" INNER JOIN Student stu ON rep.IDStudent = stu.ID "
        +" INNER JOIN Subject sub ON rep.CodeSubject = sub.Code "
        +" WHERE rep.IDStudent = '32066670';";

        try (Connection conn = CMySQL.conectarMySQL();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){
        
        while(rs.next()){
        int ID = rs.getInt("ID");
        int Code = rs.getInt("Code");
        String nameSubject = rs.getString("Name");
        String nameStudent = rs.getString("Name");

        System.out.println(ID + Code + nameStudent + nameSubject +"1");
        }    
        } catch (SQLException e) {
            System.out.println(e);
        }

        
    }



}
