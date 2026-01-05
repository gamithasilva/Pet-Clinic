package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.AppointmentMedicineDTO;
import lk.ijse.petclinic.dto.InvoiceDTO;
import lk.ijse.petclinic.model.InvoiceModel;

import java.time.format.DateTimeFormatter;

public class InvoiceDetailViewController {

    @FXML private Label invoiceNumberLabel;
    @FXML private Label invoiceDateLabel;
    @FXML private Label petNameLabel;
    @FXML private Label ownerNameLabel;
    @FXML private Label doctorNameLabel;
    @FXML private Label appointmentDateLabel;
    @FXML private Label consultationFeeLabel;
    @FXML private Label medicineTotalLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label paymentStatusLabel;
    @FXML private VBox medicineList;
    @FXML private VBox medicineDetailsBox;
    @FXML private Button closeBtn;

    private InvoiceDTO invoice;
    private InvoiceModel invoiceModel = new InvoiceModel();

    public void setInvoiceData(InvoiceDTO invoice) {
        this.invoice = invoice;

        // Load full invoice details with medicines
        InvoiceDTO fullInvoice = invoiceModel.getInvoiceById(invoice.getInvoiceId());
        if (fullInvoice != null) {
            this.invoice = fullInvoice;
        }

        displayInvoiceDetails();
    }

    private void displayInvoiceDetails() {
        // Header info
        invoiceNumberLabel.setText(invoice.getInvoiceNumber());
        invoiceDateLabel.setText(invoice.getInvoiceDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")
        ));

        // Patient info
        petNameLabel.setText(invoice.getPetName());
        ownerNameLabel.setText(invoice.getOwnerName());

        // Appointment info
        doctorNameLabel.setText(invoice.getDoctorName());
        appointmentDateLabel.setText(invoice.getAppointmentDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")
        ));

        // Financial info
        consultationFeeLabel.setText(String.format("Rs. %.2f", invoice.getConsultationFee()));
        medicineTotalLabel.setText(String.format("Rs. %.2f", invoice.getMedicineTotal()));
        subtotalLabel.setText(String.format("Rs. %.2f", invoice.getSubtotal()));
        discountLabel.setText(String.format("Rs. %.2f", invoice.getDiscount()));
        totalAmountLabel.setText(String.format("Rs. %.2f", invoice.getTotalAmount()));

        // Payment status
        String status = invoice.getPaymentStatus();
        paymentStatusLabel.setText(status.substring(0, 1).toUpperCase() + status.substring(1));

        if ("completed".equalsIgnoreCase(status)) {
            paymentStatusLabel.setStyle(
                    "-fx-background-color: #00ff88; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-padding: 5 15; -fx-background-radius: 12;"
            );
        } else if ("pending".equalsIgnoreCase(status)) {
            paymentStatusLabel.setStyle(
                    "-fx-background-color: #ff9900; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-padding: 5 15; -fx-background-radius: 12;"
            );
        }

        // Display medicines
        displayMedicines();
    }

    private void displayMedicines() {
        medicineList.getChildren().clear();

        if (invoice.getMedicines() == null || invoice.getMedicines().isEmpty()) {
            medicineDetailsBox.setVisible(false);
            medicineDetailsBox.setManaged(false);
            return;
        }

        for (AppointmentMedicineDTO medicine : invoice.getMedicines()) {
            HBox medicineRow = createMedicineRow(medicine);
            medicineList.getChildren().add(medicineRow);
        }
    }

    private HBox createMedicineRow(AppointmentMedicineDTO medicine) {
        HBox row = new HBox(10);
        row.setStyle(
                "-fx-background-color: #1a1a1a; -fx-background-radius: 8; " +
                        "-fx-border-color: #333333; -fx-border-radius: 8; -fx-border-width: 1; -fx-padding: 10;"
        );

        // Medicine name
        Label nameLabel = new Label(medicine.getMedicineName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 180;");

        // Quantity
        Label qtyLabel = new Label("Qty: " + medicine.getQuantity());
        qtyLabel.setStyle("-fx-text-fill: #999999; -fx-min-width: 80;");

        // Unit price
        Label priceLabel = new Label(String.format("@ Rs. %.2f", medicine.getUnitPrice()));
        priceLabel.setStyle("-fx-text-fill: #999999; -fx-min-width: 100;");

        // Total
        Label totalLabel = new Label(String.format("Rs. %.2f", medicine.getTotal()));
        totalLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
        HBox.setHgrow(totalLabel, javafx.scene.layout.Priority.ALWAYS);

        row.getChildren().addAll(nameLabel, qtyLabel, priceLabel, totalLabel);
        return row;
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }
}