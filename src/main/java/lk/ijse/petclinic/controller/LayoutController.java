package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LayoutController {

    @FXML
    private Button appointmentBtn;

    @FXML
    private BorderPane bpane;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button doctorBtn;

    @FXML
    private Button medicineBtn;

    @FXML
    private Button petBtn;

    @FXML
    private Button reportBtn;

    public void initialize(){
        try{
            loadDashBoard();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    private void loadDashBoard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoard.fxml"));
        AnchorPane root = loader.load();
        bpane.setCenter(root);
        setActiveButton(dashboardBtn);
    }

    @FXML
    private void loadAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appopintment.fxml"));
        AnchorPane root = loader.load();
        bpane.setCenter(root);
        setActiveButton(appointmentBtn);
    }

    @FXML
    private void loadDoctor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctor.fxml"));
        AnchorPane root = loader.load();
        bpane.setCenter(root);
        setActiveButton(doctorBtn);
    }

    @FXML
    private void loadPet() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/pet.fxml"));
        AnchorPane root = loader.load();
        bpane.setCenter(root);
        setActiveButton(petBtn);
    }

    @FXML
    private void loadMedicine() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/medicine.fxml"));
        AnchorPane root = loader.load();
        bpane.setCenter(root);
        setActiveButton(medicineBtn);
    }

    private void setActiveButton(Button activeButton) {
        // Remove active class from all buttons
        dashboardBtn.getStyleClass().remove("nav-button-active");
        petBtn.getStyleClass().remove("nav-button-active");
        doctorBtn.getStyleClass().remove("nav-button-active");
        appointmentBtn.getStyleClass().remove("nav-button-active");
        medicineBtn.getStyleClass().remove("nav-button-active");
        reportBtn.getStyleClass().remove("nav-button-active");

        // Add active class to the clicked button
        if (!activeButton.getStyleClass().contains("nav-button-active")) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }

}
