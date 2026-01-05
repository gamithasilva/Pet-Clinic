module lk.ijse.petclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;


    opens lk.ijse.petclinic to javafx.fxml;
    opens lk.ijse.petclinic.dto to javafx.base;
    
    exports lk.ijse.petclinic;
    exports lk.ijse.petclinic.controller;
    exports lk.ijse.petclinic.dto;
    
    opens lk.ijse.petclinic.controller to javafx.fxml;
}