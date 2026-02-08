package lk.ijse.petclinic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.AppointmentDTO;
import lk.ijse.petclinic.dto.AppointmentMedicineDTO;
import lk.ijse.petclinic.dto.InvoiceDTO;
import lk.ijse.petclinic.dto.PaymentDTO;
import lk.ijse.petclinic.model.AppointmentModel;
import lk.ijse.petclinic.model.InvoiceModel;
import lk.ijse.petclinic.model.PaymentModel;
import lk.ijse.petclinic.model.AppointmentMedicineModel;
import lk.ijse.petclinic.util.Reference;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddInvoiceFormController {

    @FXML private ComboBox<AppointmentDTO> appointmentComboBox;
    @FXML private TextField invoiceNumberField;
    @FXML private TextField petNameField;
    @FXML private TextField ownerNameField;
    @FXML private TextField doctorNameField;
    @FXML private TextField appointmentDateField;
    @FXML private TextField consultationFeeField;
    @FXML private TextField medicineTotalField;
    @FXML private TextField discountField;
    @FXML private TextField totalAmountField;
    @FXML private Button createBtn;
    @FXML private Button cancelBtn;

    private AppointmentModel appointmentModel = new AppointmentModel();
    private PaymentModel paymentModel = new PaymentModel();
    private InvoiceModel invoiceModel = new InvoiceModel();
    private AppointmentMedicineModel appointmentMedicineModel = new AppointmentMedicineModel();
    private AppointmentDTO selectedAppointment;
    private PaymentDTO selectedPayment;

    public void initialize() {
        loadCompletedAppointments();
        setupComboBoxListener();
        setupDiscountListener();
        generateInvoiceNumber();

        // Initialize fields with default values
        discountField.setText("0.00");
    }

    private void loadCompletedAppointments() {
        try {
            List<AppointmentDTO> completedAppointments = appointmentModel.getCompletedAppointments();

            if (completedAppointments != null && !completedAppointments.isEmpty()) {
                // Filter out appointments that already have invoices
                List<AppointmentDTO> eligibleAppointments = completedAppointments.stream()
                        .filter(apt -> !invoiceModel.invoiceExistsForAppointment(apt.getAppointmentId()))
                        .toList();

                ObservableList<AppointmentDTO> appointments = FXCollections.observableArrayList(eligibleAppointments);
                appointmentComboBox.setItems(appointments);

                // Custom display for combo box
                appointmentComboBox.setCellFactory(param -> new ListCell<AppointmentDTO>() {
                    @Override
                    protected void updateItem(AppointmentDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(String.format("%s - %s (Owner: %s) - %s",
                                    item.getPetName(),
                                    item.getPetSpecies(),
                                    item.getPetOwnerName(),
                                    item.getAppointmentDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            ));
                        }
                    }
                });

                appointmentComboBox.setButtonCell(new ListCell<AppointmentDTO>() {
                    @Override
                    protected void updateItem(AppointmentDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(String.format("%s - %s (Owner: %s)",
                                    item.getPetName(),
                                    item.getPetSpecies(),
                                    item.getPetOwnerName()
                            ));
                        }
                    }
                });
            } else {
                new Alert(Alert.AlertType.INFORMATION,
                        "No completed appointments available for invoicing.").showAndWait();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error loading appointments: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void setupComboBoxListener() {
        appointmentComboBox.setOnAction(event -> {
            selectedAppointment = appointmentComboBox.getValue();
            if (selectedAppointment != null) {
                loadAppointmentDetails();
            }
        });
    }

    private void loadAppointmentDetails() {
        try {
            // Load full appointment details
            selectedAppointment = appointmentModel.getAppointmentById(selectedAppointment.getAppointmentId());

            // Load payment details
            selectedPayment = paymentModel.getPaymentByAppointment(selectedAppointment.getAppointmentId());

            if (selectedPayment == null) {
                new Alert(Alert.AlertType.ERROR,
                        "No payment found for this appointment!").showAndWait();
                return;
            }

            // Populate fields
            petNameField.setText(selectedAppointment.getPetName());
            ownerNameField.setText(selectedAppointment.getPetOwnerName());
            doctorNameField.setText(selectedAppointment.getDoctorName());
            appointmentDateField.setText(
                    selectedAppointment.getAppointmentDatetime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );

            // Set consultation fee
            consultationFeeField.setText(String.format("%.2f", selectedAppointment.getConsultationFee()));

            // Calculate medicine total
            double medicineTotal = 0.0;
            List<AppointmentMedicineDTO> medicines = appointmentMedicineModel.getAppointmentMedicines(selectedAppointment.getAppointmentId());
            if (medicines != null && !medicines.isEmpty()) {
                selectedAppointment.setMedicines(medicines); // keep appointment in sync
                for (AppointmentMedicineDTO med : medicines) {
                    if (med.getSubtotal() != null) {
                        medicineTotal += med.getSubtotal();
                    } else if (med.getSellingPrice() != null && med.getQuantity() != null) {
                        medicineTotal += med.getSellingPrice() * med.getQuantity();
                    } else if (med.getUnitPrice() != null && med.getQuantity() != null) {
                        medicineTotal += med.getUnitPrice() * med.getQuantity();
                    }
                }
            }
            medicineTotalField.setText(String.format("%.2f", medicineTotal));

            // Calculate total
            calculateTotal();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error loading appointment details: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void setupDiscountListener() {
        discountField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateTotal();
        });
    }

    private void calculateTotal() {
        try {
            String consultationText = consultationFeeField.getText().trim();
            String medicineText = medicineTotalField.getText().trim();
            String discountText = discountField.getText().trim();

            double consultation = consultationText.isEmpty() ? 0.0 : Double.parseDouble(consultationText);
            double medicine = medicineText.isEmpty() ? 0.0 : Double.parseDouble(medicineText);
            double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);

            double total = consultation + medicine - discount;
            totalAmountField.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            // Handle invalid number format
            totalAmountField.setText("0.00");
        }
    }

    private void generateInvoiceNumber() {
        try {
            String invoiceNumber = invoiceModel.generateInvoiceNumber();
            invoiceNumberField.setText(invoiceNumber);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error generating invoice number: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleCreate() {
        if (!validateInputs()) {
            return;
        }

        try {
            // Parse values safely
            String consultationText = consultationFeeField.getText().trim();
            String medicineText = medicineTotalField.getText().trim();
            String discountText = discountField.getText().trim();

            double consultationFee = consultationText.isEmpty() ? 0.0 : Double.parseDouble(consultationText);
            double medicineTotal = medicineText.isEmpty() ? 0.0 : Double.parseDouble(medicineText);
            double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);

            // Create invoice DTO
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setAppointmentId(selectedAppointment.getAppointmentId());
            invoice.setPaymentId(selectedPayment.getPaymentId());
            invoice.setInvoiceNumber(invoiceNumberField.getText());
            invoice.setConsultationFee(consultationFee);
            invoice.setMedicineTotal(medicineTotal);  // Set medicine total
            invoice.setDiscount(discount);
            invoice.calculateTotals();  // This will now include the medicine total

            // Save invoice
            boolean success = invoiceModel.createInvoice(invoice);

            if (success) {
                new Alert(Alert.AlertType.INFORMATION,
                        "Invoice created successfully!").showAndWait();
                closeWindow();
            } else {
                new Alert(Alert.AlertType.ERROR,
                        "Failed to create invoice!").showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Invalid number format in one of the fields!").showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error creating invoice: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateInputs() {
        if (selectedAppointment == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select an appointment!").showAndWait();
            return false;
        }

        if (invoiceNumberField.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Invoice number is required!").showAndWait();
            return false;
        }

        if (!discountField.getText().trim().isEmpty()) {
            try {
                double discount = Double.parseDouble(discountField.getText().trim());
                if (discount < 0) {
                    new Alert(Alert.AlertType.WARNING,
                            "Discount cannot be negative!").showAndWait();
                    return false;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING,
                        "Invalid discount amount!").showAndWait();
                return false;
            }
        }

        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
        Reference.petView.initialize();
    }
}