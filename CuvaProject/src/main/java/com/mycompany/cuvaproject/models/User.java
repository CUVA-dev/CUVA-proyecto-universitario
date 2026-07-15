//alskjdlaksjd

package com.mycompany.cuvaproject.models;

import com.mycompany.cuvaproject.data_base.Data_Manipulator;

public class User {
    
    Data_Manipulator ObjDataM = new Data_Manipulator();
    
    private String name;
    private String lastName;
    private String ID;
    private String password;
    private String email;
    private String post;
    private String range;

    // Constructor
    public User(String name, String lastName, String id, String password, String email, String post, String range) {
        this.name = name;
        this.lastName = lastName;
        this.ID = id;
        this.password = password;
        this.email = email;
        this.post = post;
        this.range = range;
        

        validated(); 
        System.out.println("Se creo el objeto usuario con exito.");
    }

    // Getters
    public String getName() { return this.name; }
    public String getLastName() { return this.lastName; }
    public String getID() { return this.ID; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public String getPost() { return this.post; }
     public String getRange() { return this.range; }

    // Setters con validación integrada para evitar datos corruptos después
    public void setName(String name) {
        this.name = name;
        validated_name();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        validated_lastName();
    }

    public void setID(String id) {
        this.ID = id;
        validated_ID();
    }

    public void setPassword(String password) {
        this.password = password;
        validated_password();
    }

    public void setEmail(String email) {
        this.email = email;
        validated_email();
    }

    public void setPost(String post) {
        this.post = post;
    }
    
    public void setRange(String range) {
        this.range = range;
    }

    // --- VALIDACIONES CORREGIDAS ---

    public void validated_name() {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (this.name.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener un minimo de 3 caracteres");
        }
        if (this.name.length() > 20) {
            throw new IllegalArgumentException("El nombre puede tener un maximo de 20 caracteres");
        }
        for (int i = 0; i < this.name.length(); i++) {
            char c = this.name.charAt(i);
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("El nombre no puede tener numeros, caracteres especiales ni espacios");
            }
        }
    }

    public void validated_lastName() {

        if (this.lastName == null || this.lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio");
        }
        if (this.lastName.length() < 3) {
            throw new IllegalArgumentException("El apellido debe tener un minimo de 3 caracteres");
        }
        if (this.lastName.length() > 20) {
            throw new IllegalArgumentException("El apellido puede tener un maximo de 20 caracteres");
        }
        for (int j = 0; j < this.lastName.length(); j++) {
            char a = this.lastName.charAt(j);
            if (!Character.isLetter(a)) {
                throw new IllegalArgumentException("El apellido no puede tener numeros, caracteres especiales ni espacios");
            }
        }
    }

    public void validated_ID() {
        if (this.ID == null || this.ID.trim().isEmpty()) {
            throw new IllegalArgumentException("la cedula no puede esta vacia");
        }
        if (this.ID.length() < 7) {
            throw new IllegalArgumentException("La cedula debe de tener un minimo de 7 caracteres");
        }
        if (this.ID.length() > 11) {
            throw new IllegalArgumentException("La cedula solo puede tener un maximo de 11 caracteres");
        }
        
        for(int i=0; i <= this.ID.length()- 1; i++){
            
            char c = this.ID.charAt(i);
            
            if(!Character.isDigit(c) ){
                throw new IllegalArgumentException(" La cedula no puede tener caracteres especiales, espacios y tampoco puede ser letras");}
            
        }
        
    }

    public void validated_email() {
        if (this.email == null || this.email.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electronico no puede estar vacio");
        }
        if (!this.email.contains("@") || !this.email.contains(".")) {
            throw new IllegalArgumentException("El formato de correo es invalido. Debe contener un punto (.) y un arroba (@)");
        }
    }

    public void validated_password() {
        // CORREGIDO: Validar vacío al principio
        if (this.password == null || this.password.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia");
        }
        if (this.password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener como mínimo 8 caracteres");
        }
        if (this.password.equals(this.ID)) {
            throw new IllegalArgumentException("La contraseña no puede ser igual al nombre de usuario");
        }

        boolean upper = false;
        boolean digit = false;
        boolean characterSpecial = false;

        for (int i = 0; i < this.password.length(); i++) {
            char b = this.password.charAt(i);

            if (Character.isSpaceChar(b)) {
                throw new IllegalArgumentException("La contraseña no puede tener espacios en blanco");
            }
            if (Character.isDigit(b)) {
                digit = true;
            }
            if (Character.isUpperCase(b)) {
                upper = true;
            }
            if (!Character.isLetter(b) && !Character.isDigit(b)) {
                characterSpecial = true;
            }
        }

        if (!characterSpecial || !digit || !upper) {
            throw new IllegalArgumentException("La contraseña debe tener como minimo una mayuscula, un caracter especial y un numero");
        }
    }

    // Agrupador de validaciones
    public void validated() {
        validated_ID();
        validated_name();
        validated_lastName();
        validated_email();
        validated_password();

        if(ObjDataM.ValidationRegister(ID, email)== false){
        ObjDataM.InsertTableUser(this);
        }
    }
}
