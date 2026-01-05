package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.AppointmentDTO;
import lk.ijse.petclinic.model.AppointmentModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Controller for Appointment Card
 */
public class AppointmentCardController {

    @FXML private Label appointmentId;
    @FXML private Label petNameLabel;
    @FXML private Label ownerNameLabel;
    @FXML private Label doctorNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label feeLabel;
    @FXML private Label statusLabel;
    @FXML private Button completeBtn;
    @FXML private Button cancelBtn;
    @FXML private Button deleteBtn;

    private AppointmentDTO appointmentDTO;
    private AppointmentModel appointmentModel = new AppointmentModel();

    public void setAppointmentDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDTO = appointmentDTO;

        // Set appointment ID
        appointmentId.setText("A" + String.format("%03d", appointmentDTO.getAppointmentId()));

        // Set pet and owner info
        petNameLabel.setText(appointmentDTO.getPetName());
        ownerNameLabel.setText(appointmentDTO.getPetOwnerName());

        // Set doctor info
        doctorNameLabel.setText(appointmentDTO.getDoctorName());

        // Set date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        dateLabel.setText(appointmentDTO.getAppointmentDatetime().format(dateFormatter));
        timeLabel.setText(appointmentDTO.getAppointmentDatetime().format(timeFormatter));

        // Set fee
        feeLabel.setText(String.format("Rs. %.2f", appointmentDTO.getConsultationFee()));

        // Set status
        statusLabel.setText(appointmentDTO.getStatus());
        updateStatusStyle(appointmentDTO.getStatus());

        // Show/hide buttons based on status
        updateButtonVisibility(appointmentDTO.getStatus());
    }

    private void updateStatusStyle(String status) {
        switch (status.toLowerCase()) {
            case "scheduled":
                statusLabel.setStyle(
                        "-fx-background-color: linear-gradient(to right, #3399ff, #66b3ff); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 11; " +
                                "-fx-background-radius: 12; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-border-color: #66b3ff; " +
                                "-fx-border-radius: 12; " +
                                "-fx-border-width: 1;"
                );
                break;
            case "completed":
                statusLabel.setStyle(
                        "-fx-background-color: linear-gradient(to right, #00cc66, #00ff88); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 11; " +
                                "-fx-background-radius: 12; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 12; " +
                                "-fx-border-width: 1;"
                );
                break;
            case "cancelled":
                statusLabel.setStyle(
                        "-fx-background-color: linear-gradient(to right, #ff4444, #ff6666); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 11; " +
                                "-fx-background-radius: 12; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-border-color: #ff6666; " +
                                "-fx-border-radius: 12; " +
                                "-fx-border-width: 1;"
                );
                break;
            case "no-show":
                statusLabel.setStyle(
                        "-fx-background-color: linear-gradient(to right, #999999, #bbbbbb); " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 11; " +
                                "-fx-background-radius: 12; " +
                                "-fx-padding: 4 12 4 12; " +
                                "-fx-border-color: #bbbbbb; " +
                                "-fx-border-radius: 12; " +
                                "-fx-border-width: 1;"
                );
                break;
        }
    }

    private void updateButtonVisibility(String status) {
        switch (status.toLowerCase()) {
            case "scheduled":
                // Show complete and cancel buttons
                completeBtn.setVisible(true);
                completeBtn.setManaged(true);
                cancelBtn.setVisible(true);
                cancelBtn.setManaged(true);
                break;
            case "completed":
            case "cancelled":
            case "no-show":
                // Only show delete button
                completeBtn.setVisible(false);
                completeBtn.setManaged(false);
                cancelBtn.setVisible(false);
                cancelBtn.setManaged(false);
                break;
        }
    }

    @FXML
    private void completeAppointment() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/completeAppointment.fxml"));
            Parent root = fxmlLoader.load();

            CompleteAppointmentController controller = fxmlLoader.getController();
            controller.setAppointmentDTO(appointmentDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Complete Appointment - A" + appointmentDTO.getAppointmentId());
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(completeBtn.getScene().getWindow());

            stage.showAndWait();

            // Refresh the list after completion
            if (Reference.appointmentListView != null) {
                Reference.appointmentListView.initialize();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error opening complete appointment form: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAppointment() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Appointment");
        confirm.setHeaderText("Cancel this appointment?");
        confirm.setContentText("Appointment for " + appointmentDTO.getPetName() + " will be cancelled.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = appointmentModel.updateAppointmentStatus(
                            appointmentDTO.getAppointmentId(),
                            "cancelled"
                    );

                    if (success) {
                        new Alert(Alert.AlertType.INFORMATION, "Appointment cancelled successfully").showAndWait();

                        // Refresh the list
                        if (Reference.appointmentListView != null) {
                            Reference.appointmentListView.initialize();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to cancel appointment").showAndWait();
                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void deleteAppointment() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Appointment");
        confirm.setHeaderText("Delete this appointment?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = appointmentModel.deleteAppointment(appointmentDTO.getAppointmentId());

                    if (success) {
                        new Alert(Alert.AlertType.INFORMATION, "Appointment deleted successfully").showAndWait();

                        // Refresh the list
                        if (Reference.appointmentListView != null) {
                            Reference.appointmentListView.initialize();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete appointment").showAndWait();
                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }
}
