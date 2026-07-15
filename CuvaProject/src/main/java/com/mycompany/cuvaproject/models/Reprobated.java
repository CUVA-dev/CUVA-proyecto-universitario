package com.mycompany.cuvaproject.models;

import com.mycompany.cuvaproject.data_base.Data_Manipulator;

public class Reprobated {
        
        Data_Manipulator ObjDataM = new Data_Manipulator();

    private String nameStudent;
    private String nameSubject;
    private String IDStudent;
    private String codeSubject;
    private String period; // NUEVO: Para guardar el periodo (Ej: 1-2023)
    private String grade;  // NUEVO: String para soportar "05" o textos como "INASISTENCIA"
        

    public Reprobated(String nameStudent, String nameSubject, String IDStudent, String codeSubject, String period, String grade) {
        this.IDStudent = IDStudent;
        this.codeSubject = codeSubject;
        this.nameStudent = nameStudent;
        this.nameSubject = nameSubject;
        this.period = period;
        this.grade = grade;
        ObjDataM.InsertTableReprobated(this);
    }

    // Getters
    public String getIDStudent() { return IDStudent; }
    public String getNameSubject() { return nameSubject; }
    public String getNameStudent() { return nameStudent; }
    public String getCodeSubject() { return codeSubject; }
    public String getPeriod() { return period; }
    public String getGrade() { return grade; }

    // Setters
    public void setIDStudent(String IDStudent) { this.IDStudent = IDStudent; }
    public void setNameSubject(String nameSubject) { this.nameSubject = nameSubject; }
    public void setNameStudent(String nameStudent) { this.nameStudent = nameStudent; }
    public void setCodeSubject(String codeSubject) { this.codeSubject = codeSubject; }
    public void setPeriod(String period) { this.period = period; }
    public void setGrade(String grade) { this.grade = grade; }

    // --- Validaciones ---
    
    public void validate_IDStudent() {
        if(this.IDStudent.length() > 11 || this.IDStudent.length() < 7 ){
            throw new IllegalArgumentException("La cedula tiene una longitud invalida");
        }
    }
    
    public void validate_codeSubject(){
        if (this.codeSubject.isEmpty()){ throw new IllegalArgumentException("El codigo no puede estar vacio"); }
        if (this.codeSubject.contains(" ")){ throw new IllegalArgumentException("El codigo no puede tener espacios en blanco"); }
        if (this.codeSubject.length() > 12 || this.codeSubject.length() < 8){
            throw new IllegalArgumentException("El codigo debe tener entre 8 y 12 caracteres");
        }
    }
    
    public void validated_nameStudent(){
        if (nameStudent == null || nameStudent.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }
    
    public void validated_nameSubject(){
        if(this.nameSubject == null || this.nameSubject.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacio");
        }
    }

    public void validate_period() {
        if(this.period == null || this.period.trim().isEmpty()) {
            throw new IllegalArgumentException("El periodo no puede estar vacio");
        }
    }

    public void validate_grade() {
        if(this.grade == null || this.grade.trim().isEmpty()) {
            throw new IllegalArgumentException("La nota no puede estar vacia");
        }
    }
    
    // Validación general
    public void validated(){
        validated_nameSubject();
        validated_nameStudent();
        validate_codeSubject();
        validate_IDStudent();
        validate_period();
        validate_grade();
    }
}
