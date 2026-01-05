package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.AppointmentDTO;
import lk.ijse.petclinic.model.AppointmentModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Appointment List View (GridPane with cards)
 */
public class AppointmentListViewController {

    @FXML
    private GridPane gridPane;

    private AppointmentModel appointmentModel = new AppointmentModel();

    public void initialize() {
        Reference.appointmentListView = this;
        try {
            if (!gridPane.getChildren().isEmpty()) {
                gridPane.getChildren().clear();
            }

            List<AppointmentDTO> appointments = appointmentModel.getAllAppointments();
            if (appointments != null) {
                includeInGridPane(appointments);
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading appointments: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void includeInGridPane(List<AppointmentDTO> appointmentList) {
        int row = 0;

        for (AppointmentDTO appointment : appointmentList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppoinmentCard.fxml"));
                AnchorPane card = loader.load();

                AppointmentCardController cardController = loader.getController();
                cardController.setAppointmentDTO(appointment);

                GridPane.setFillWidth(card, true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(card, 0, row);

                row++;
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error loading appointment card: " + e.getMessage()).showAndWait();
            }
        }
    }

    public void removeElement() {
        try {
            gridPane.getChildren().clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error clearing view").showAndWait();
        }
    }

    public void search(String keyword) {
        try {
            removeElement();

            List<AppointmentDTO> appointments = appointmentModel.searchAppointments(keyword);
            if (appointments != null) {
                includeInGridPane(appointments);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No appointments found").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search error: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }
}