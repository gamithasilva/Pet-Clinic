package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

/**
 * Main controller for Appointment Management (with Appointment and Invoice tabs)
 */
public class AppointmentController {

    // Appointment Tab
    @FXML private TextField appointmentSearchBox;
    @FXML private Button addAppointmentBtn;
    @FXML private Button refreshAppointmentBtn;
    @FXML private AnchorPane appointmentContentPane;

    // Invoice Tab
    @FXML private TextField invoiceSearchBox;
    @FXML private Button addInvoiceBtn;
    @FXML private Button refreshInvoiceBtn;
    @FXML private AnchorPane invoiceContentPane;

    public void initialize() {
        try {
            // Load Appointment List View
            FXMLLoader appointmentLoader = new FXMLLoader(getClass().getResource("/view/appoinmentListView.fxml"));
            AnchorPane appointmentListView = appointmentLoader.load();

            // Set anchors to fill the parent
            AnchorPane.setTopAnchor(appointmentListView, 0.0);
            AnchorPane.setBottomAnchor(appointmentListView, 0.0);
            AnchorPane.setLeftAnchor(appointmentListView, 0.0);
            AnchorPane.setRightAnchor(appointmentListView, 0.0);

            appointmentContentPane.getChildren().add(appointmentListView);

            // Load Invoice List View
            FXMLLoader invoiceLoader = new FXMLLoader(getClass().getResource("/view/invoiceListView.fxml"));
            AnchorPane invoiceListView = invoiceLoader.load();

            // Set anchors to fill the parent
            AnchorPane.setTopAnchor(invoiceListView, 0.0);
            AnchorPane.setBottomAnchor(invoiceListView, 0.0);
            AnchorPane.setLeftAnchor(invoiceListView, 0.0);
            AnchorPane.setRightAnchor(invoiceListView, 0.0);

            invoiceContentPane.getChildren().add(invoiceListView);

            // Setup search actions
            appointmentSearchBox.setOnAction(e -> searchAppointments());
            invoiceSearchBox.setOnAction(e -> searchInvoices());

            // Setup button actions for Appointments
            addAppointmentBtn.setOnAction(e -> addAppointment());
            refreshAppointmentBtn.setOnAction(e -> refreshAppointments());

            // Setup button actions for Invoices
            addInvoiceBtn.setOnAction(e -> addInvoice());
            refreshInvoiceBtn.setOnAction(e -> refreshInvoices());

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading views: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    // ==================== APPOINTMENT METHODS ====================

    @FXML
    private void searchAppointments() {
        String keyword = appointmentSearchBox.getText();
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (Reference.appointmentListView != null) {
                Reference.appointmentListView.search(keyword);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a search term").showAndWait();
        }
    }

    @FXML
    private void addAppointment() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddAppoinmentForm.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Schedule New Appointment");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(addAppointmentBtn.getScene().getWindow());

            stage.showAndWait();

            // Refresh list after adding
            if (Reference.appointmentListView != null) {
                Reference.appointmentListView.initialize();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error opening add appointment form: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshAppointments() {
        if (Reference.appointmentListView != null) {
            Reference.appointmentListView.initialize();
            appointmentSearchBox.clear();
        }
    }

    // ==================== INVOICE METHODS ====================

    @FXML
    private void searchInvoices() {
        String keyword = invoiceSearchBox.getText();
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (Reference.invoiceListView != null) {
                Reference.invoiceListView.search(keyword);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a search term").showAndWait();
        }
    }

    @FXML
    private void addInvoice() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddInvoiceForm.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Create New Invoice");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(addInvoiceBtn.getScene().getWindow());

            stage.showAndWait();

            // Refresh invoice list after adding
            if (Reference.invoiceListView != null) {
                Reference.invoiceListView.initialize();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error opening add invoice form: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshInvoices() {
        if (Reference.invoiceListView != null) {
            Reference.invoiceListView.initialize();
            invoiceSearchBox.clear();
        }
    }
}