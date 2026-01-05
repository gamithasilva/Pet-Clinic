package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.util.Reference;

public class AddDoctorController {
    @FXML
    private TextField ademail;

    @FXML
    private TextField adfee;

    @FXML
    private TextField adname;

    @FXML
    private TextField adpassword;

    @FXML
    private TextField adphone;

    @FXML
    private Button saveDoc;

    @FXML
    private Button cancel;

    private DoctorModel doctorModel = new DoctorModel();

    @FXML
    public void addDoctor(ActionEvent event) {
        try {
            // Validate inputs
            if (adname.getText().trim().isEmpty() ||
                    ademail.getText().trim().isEmpty() ||
                    adphone.getText().trim().isEmpty() ||
                    adfee.getText().trim().isEmpty() ||
                    adpassword.getText().trim().isEmpty()) {

                new Alert(Alert.AlertType.WARNING, "Please fill all fields").showAndWait();
                return;
            }

            // Validate email format
            if (!ademail.getText().contains("@")) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid email address").showAndWait();
                return;
            }

            // Create DTO
            DoctorDTO doctorDTO = new DoctorDTO(
                    Double.parseDouble(adfee.getText()),
                    adname.getText().trim(),
                    ademail.getText().trim(),
                    adphone.getText().trim(),
                    adpassword.getText().trim()
            );

            // Try to add doctor
            if (doctorModel.addDoctor(doctorDTO)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Doctor Added Successfully!").showAndWait();

                // Refresh the list
                if (Reference.listview != null) {
                    Reference.listview.initialize();
                }

                // Close the window
                closeWindow();

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add doctor. Please try again.").showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid fee amount. Please enter a valid number.").showAndWait();

        } catch (RuntimeException e) {
            // This catches the user-friendly errors from DoctorModel (like duplicate email)
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        closeWindow();
    }

    // Helper method to close the window
    private void closeWindow() {
        Stage stage = (Stage) saveDoc.getScene().getWindow();
        stage.close();
    }
}