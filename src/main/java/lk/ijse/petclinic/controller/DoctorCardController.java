package lk.ijse.petclinic.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.Application;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.sql.SQLException;



public class DoctorCardController {
    @FXML
    private ComboBox<String> availableBox;

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private Label email;

    @FXML
    private Label id;

    @FXML
    private Label name;

    private DoctorDTO doctorDTO;
    private DoctorModel doctorModel = new DoctorModel();

    public void initialize(){
        availableBox.getItems().addAll("Available","Not Available");

        availableBox.setOnAction(event -> {
            String selectedStatus = availableBox.getValue();
            if("Available".equals(selectedStatus)){
                availableBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #00cc66, #00ff88); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 12; " +
                                "-fx-background-radius: 16; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 16; " +
                                "-fx-border-width: 2; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.5), 10, 0, 0, 0); " +
                                "-fx-cursor: hand;"
                );
            }else{
                availableBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #ff3333, #ff6666); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 12; " +
                                "-fx-background-radius: 16; " +
                                "-fx-border-color: #ff6666; " +
                                "-fx-border-radius: 16; " +
                                "-fx-border-width: 2; " +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 102, 102, 0.5), 10, 0, 0, 0); " +
                                "-fx-cursor: hand;"
                );
            }

            if (doctorDTO != null){
                doctorDTO.setStatus(selectedStatus);
                try {
                    doctorModel.updateDoctorStatus(doctorDTO.getDoctor_id(), selectedStatus);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void setDoctorDTO(DoctorDTO doctorDTO){
        this.doctorDTO = doctorDTO;
        name.setText(doctorDTO.getName());
        email.setText(doctorDTO.getEmail());
        id.setText(String.valueOf(doctorDTO.getDoctor_id()));
        availableBox.setValue(doctorDTO.getStatus());

        if ("Available".equals(doctorDTO.getStatus())){
            availableBox.setStyle(
                    "-fx-background-color: linear-gradient(to right, #00cc66, #00ff88); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 12; " +
                            "-fx-background-radius: 16; " +
                            "-fx-border-color: #00ff88; " +
                            "-fx-border-radius: 16; " +
                            "-fx-border-width: 2; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.5), 10, 0, 0, 0); " +
                            "-fx-cursor: hand;"
            );
        }else{
            availableBox.setStyle(
                    "-fx-background-color: linear-gradient(to right, #ff3333, #ff6666); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 12; " +
                            "-fx-background-radius: 16; " +
                            "-fx-border-color: #ff6666; " +
                            "-fx-border-radius: 16; " +
                            "-fx-border-width: 2; " +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 102, 102, 0.5), 10, 0, 0, 0); " +
                            "-fx-cursor: hand;"
            );
        }
    }

    @FXML
    private void deleteDoctor(){
        String doctorId = id.getText();
        doctorModel.deleteDoctor(doctorId);
        Reference.listview.initialize();

    }
    @FXML
    private void editDoctor() throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/editDoctor.fxml"));
        Parent root = fxmlLoader.load();  // ← Controller is created and @FXML fields injected HERE

        EditDoctorController controller = fxmlLoader.getController();  // ← Now safe to get
        controller.setDoctorDTO(doctorDTO);  // ← Now @FXML fields are NOT null

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Doctor");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(edit.getScene().getWindow());  // recommended

        stage.showAndWait();

        // Refresh the list after edit
        if (Reference.listview != null) {
            Reference.listview.initialize();
        }
    }




}
