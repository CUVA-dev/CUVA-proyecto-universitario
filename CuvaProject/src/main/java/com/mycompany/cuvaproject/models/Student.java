package com.mycompany.cuvaproject.models;

import com.mycompany.cuvaproject.data_base.Data_Manipulator;
import com.mycompany.cuvaproject.data_base.ConnectionMySQL;

    public class Student {
        
        ConnectionMySQL ObjCMySQL = new ConnectionMySQL();
        Data_Manipulator ObjDataM = new Data_Manipulator();
        
     //aa
        private String name;
        private String lastName;
        private String Career;
        private int ID;
        private String Tuition;
        private int UC;

        public Student(String name, String lastName, String Career, int ID, String Tuition, int UC) {
            this.name = name;
            this.lastName = lastName;
            this.Career = Career;
            this.ID = ID;
            this.Tuition = Tuition;

             ObjDataM.InsertTableStudent(ObjCMySQL,this,"pruebaidparabitacora");
        }

        // getters
        public String getName() {
            return this.name;
        }

        public String getLastName() {
            return this.lastName;
        }

        public String getCareer() {
            return this.Career;
        }

        public int getID() {
            return this.ID;
        }

        public String getTuition() {
            return this.Tuition;
        }

        // setters
        public void setName(String name) {
            this.name = name;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setCareer(String Career) {
            this.Career = Career;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setTuition(String Tuition) {
            this.Tuition = Tuition;
        }

        // validacion nombre
        public void validateName(String name) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (!name.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("El nombre solo puede contener letras");
            }
            if (name.length() < 2 || name.length() > 50) {
                throw new IllegalArgumentException("El nombre debe tener entre 2 y 50 caracteres");
            }
            if (name.matches(".*\\d.*")) {
                throw new IllegalArgumentException("El nombre no puede contener números");
            }
            if (name.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new IllegalArgumentException("El nombre no puede contener caracteres especiales");
            }
        }

        // validacion apellido
        public void validateLastName(String lastName) {

            if (lastName == null || lastName.isEmpty()) {
                throw new IllegalArgumentException("El apellido no puede estar vacío");
            }
            if (!lastName.matches("[a-zA-Z]+")) {
                throw new IllegalArgumentException("El apellido solo puede contener letras");
            }
            if (lastName.length() < 2 || lastName.length() > 50) {
                throw new IllegalArgumentException("El apellido debe tener entre 2 y 50 caracteres");
            }
            if (lastName.matches(".*\\d.*")) {
                throw new IllegalArgumentException("El apellido no puede contener números");
            }
            if (lastName.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new IllegalArgumentException("El apellido no puede contener caracteres especiales");
            }
        }

        // validacion carrera
        public void validateCareer(String Career) {
            if (Career == null || Career.isEmpty()) {
                throw new IllegalArgumentException("La carrera no puede estar vacía");
            }
            if (Career.length() < 2 || Career.length() > 30) {
                throw new IllegalArgumentException("La carrera debe tener entre 2 y 30 caracteres");
            }
            if (Career.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                throw new IllegalArgumentException("La carrera no puede contener caracteres especiales");
            }
            if (Career.matches(".*\\d.*")) {
                throw new IllegalArgumentException("La carrera no puede contener números");
            }
            if (Career.matches(".*\\s{2,}.*")) {
                throw new IllegalArgumentException("La carrera no puede contener espacios consecutivos");
            }
        }

        // validacion ID
        public void validateID(int ID) {

            if (ID <= 0) {
                throw new IllegalArgumentException("El ID debe ser un número positivo");
            }
            if (String.valueOf(ID).length() < 5 || String.valueOf(ID).length() > 10) {
                throw new IllegalArgumentException("El ID debe tener entre 5 y 10 dígitos");
            }

            // if(existsID(ID)) {throw new IllegalArgumentException("El ID ya existe");}

        }

        // validacion matricula
        public void validateTuition(String Tuition) {
            if (Tuition == null || Tuition.isEmpty()) {
                throw new IllegalArgumentException("La matrícula no puede estar vacía");
            }
            if (Tuition.length() < 5 || Tuition.length() > 20) {
                throw new IllegalArgumentException("La matrícula debe tener entre 5 y 20 caracteres");
            }
            if (!Tuition.matches("[a-zA-Z0-9]+")) {
                throw new IllegalArgumentException("La matrícula solo puede contener letras y números");
            }
        }


        public void validated1() {

            validateName(this.name);
            validateLastName(this.lastName);
            validateCareer(this.Career);
            validateID(this.ID);
            validateTuition(this.Tuition);
        }

    }
