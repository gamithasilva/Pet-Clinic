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
import lk.ijse.petclinic.dto.InvoiceDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InvoiceCardController {

    @FXML private Label invoiceId;
    @FXML private Label petNameLabel;
    @FXML private Label ownerNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label doctorNameLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private Button viewBtn;
    @FXML private Button printBtn;

    private InvoiceDTO invoice;

    public void setData(InvoiceDTO invoice) {
        this.invoice = invoice;

        invoiceId.setText(invoice.getInvoiceNumber());
        petNameLabel.setText(invoice.getPetName());
        ownerNameLabel.setText(invoice.getOwnerName());
        dateLabel.setText(invoice.getAppointmentDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ));
        doctorNameLabel.setText(invoice.getDoctorName());
        totalAmountLabel.setText(String.format("Rs. %.2f", invoice.getTotalAmount()));

        // Set payment status styling
        String status = invoice.getPaymentStatus();
        paymentStatusLabel.setText(status.substring(0, 1).toUpperCase() + status.substring(1));

        if ("completed".equalsIgnoreCase(status)) {
            paymentStatusLabel.setStyle(
                    "-fx-background-color: linear-gradient(to right, #00cc66, #00ff88); " +
                            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11; " +
                            "-fx-background-radius: 12; -fx-padding: 4 12 4 12; " +
                            "-fx-border-color: #00ff88; -fx-border-radius: 12; -fx-border-width: 1;"
            );
        } else if ("pending".equalsIgnoreCase(status)) {
            paymentStatusLabel.setStyle(
                    "-fx-background-color: linear-gradient(to right, #ff9900, #ffbb33); " +
                            "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11; " +
                            "-fx-background-radius: 12; -fx-padding: 4 12 4 12; " +
                            "-fx-border-color: #ffbb33; -fx-border-radius: 12; -fx-border-width: 1;"
            );
        }
    }

    @FXML
    private void viewInvoice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InvoiceDetailView.fxml"));
            Parent root = loader.load();

            InvoiceDetailViewController controller = loader.getController();
            controller.setInvoiceData(invoice);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Invoice Details - " + invoice.getInvoiceNumber());
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(viewBtn.getScene().getWindow());
            stage.showAndWait();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error opening invoice details: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void printInvoice() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Print Invoice");
        confirmAlert.setHeaderText("Print Invoice - " + invoice.getInvoiceNumber());
        confirmAlert.setContentText("Do you want to print this invoice?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                generateInvoiceReport();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,
                        "Error printing invoice: " + e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void generateInvoiceReport() throws Exception {
        // Load Jasper report template
        InputStream reportStream = getClass().getResourceAsStream("/reports/invoice_report.jrxml");

        if (reportStream == null) {
            throw new Exception("Invoice report template not found!");
        }

        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Prepare parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoiceNumber", invoice.getInvoiceNumber());
        parameters.put("invoiceDate", invoice.getInvoiceDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));
        parameters.put("petName", invoice.getPetName());
        parameters.put("ownerName", invoice.getOwnerName());
        parameters.put("doctorName", invoice.getDoctorName());
        parameters.put("appointmentDate", invoice.getAppointmentDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        ));
        parameters.put("consultationFee", invoice.getConsultationFee());
        parameters.put("medicineTotal", invoice.getMedicineTotal());
        parameters.put("subtotal", invoice.getSubtotal());
        parameters.put("discount", invoice.getDiscount());
        parameters.put("totalAmount", invoice.getTotalAmount());
        parameters.put("paymentStatus", invoice.getPaymentStatus());

        // Fill report with empty data source (since we're using parameters)
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                new JREmptyDataSource()
        );

        // View report
        JasperViewer viewer = new JasperViewer(jasperPrint, false);
        viewer.setTitle("Invoice - " + invoice.getInvoiceNumber());
        viewer.setVisible(true);
    }
}