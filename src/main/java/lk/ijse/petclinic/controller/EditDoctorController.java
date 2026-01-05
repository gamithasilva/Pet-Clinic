package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.util.Reference;

import java.sql.SQLException;

public class EditDoctorController {
    @FXML
    private TextField dname;
    @FXML
    private TextField demail;
    @FXML
    private TextField dphone;
    @FXML
    private TextField dfee;
    @FXML
    private ComboBox<String> dstatus;

    private DoctorModel doctorModel = new DoctorModel();
    private DoctorDTO doctorDTO;

    public void initialize(){
        dstatus.getItems().addAll("Available","Not Available");
        // REMOVE the setDoctorDTO call from here!
        // The doctorDTO is null at this point
    }

    public void setDoctorDTO(DoctorDTO doctorDTO){
        this.doctorDTO = doctorDTO;

        // Add null check for safety
        if (doctorDTO != null) {
            dname.setText(doctorDTO.getName());
            demail.setText(doctorDTO.getEmail());
            dphone.setText(String.valueOf(doctorDTO.getPhone()));
            dfee.setText(String.valueOf(doctorDTO.getConsultation_fee()));
            dstatus.setValue(doctorDTO.getStatus());
        }
    }

    @FXML
    public void doSave(){
        if (doctorDTO == null) {
            new Alert(Alert.AlertType.ERROR, "No doctor data available").showAndWait();
            return;
        }

        doctorDTO.setName(dname.getText());
        doctorDTO.setEmail(demail.getText());
        doctorDTO.setPhone(dphone.getText());
        doctorDTO.setConsultation_fee(Double.parseDouble(dfee.getText()));
        doctorDTO.setStatus(dstatus.getValue());

        try {
            boolean isSave = doctorModel.updateDoctor(doctorDTO);
            if (isSave){
                new Alert(Alert.AlertType.CONFIRMATION, "Saved").showAndWait();
                Reference.listview.initialize();
                // Close the window
                dname.getScene().getWindow().hide();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    public void doCancel(){
        // Close the window without saving
        dname.getScene().getWindow().hide();
    }
}