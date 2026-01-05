package lk.ijse.petclinic.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lk.ijse.petclinic.dto.*;
import lk.ijse.petclinic.model.*;
import lk.ijse.petclinic.util.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Complete Appointment Form
 */
public class CompleteAppointmentController {

    // Header
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label petInfoLabel;
    @FXML private Label doctorInfoLabel;
    @FXML private Label feeLabel;

    // Left Panel - Medicine Selection
    @FXML private ComboBox<MedicineDTO> medicineComboBox;
    @FXML private VBox stockInfoCard;
    @FXML private Label stockInfoLabel;
    @FXML private TextField quantityField;
    @FXML private Button decreaseBtn;
    @FXML private Button increaseBtn;
    @FXML private Button addMedicineBtn;
    @FXML private TextArea medicalHistoryArea;

    // Right Panel - Table
    @FXML private TableView<MedicineRow> medicineTable;
    @FXML private TableColumn<MedicineRow, String> colMedicineName;
    @FXML private TableColumn<MedicineRow, Integer> colQuantity;
    @FXML private TableColumn<MedicineRow, Double> colPrice;
    @FXML private TableColumn<MedicineRow, Double> colSubtotal;
    @FXML private TableColumn<MedicineRow, Void> colAction;

    // Summary
    @FXML private Label consultationFeeLabel;
    @FXML private Label medicineTotalLabel;
    @FXML private Label totalAmountLabel;

    // Buttons
    @FXML private Button cancelBtn;
    @FXML private Button completeBtn;

    // Models
    private final MedicineModel medicineModel = new MedicineModel();
    private final AppointmentModel appointmentModel = new AppointmentModel();
    private final AppointmentMedicineModel appointmentMedicineModel = new AppointmentMedicineModel();
    private final MedicalHistoryModel medicalHistoryModel = new MedicalHistoryModel();
    private final PaymentModel paymentModel = new PaymentModel();

    // Data
    private AppointmentDTO appointmentDTO;
    private final ObservableList<MedicineDTO> allMedicines = FXCollections.observableArrayList();
    private final ObservableList<MedicineRow> medicineRows = FXCollections.observableArrayList();

    public void initialize() {
        setupMedicineComboBox();
        setupQuantityControls();
        setupMedicineTable();
        setupSummaryBindings();
        loadMedicines();
    }

    public void setAppointmentDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDTO = appointmentDTO;
        displayAppointmentInfo();
    }

    private void displayAppointmentInfo() {
        titleLabel.setText("Complete Appointment #A" + String.format("%03d", appointmentDTO.getAppointmentId()));
        subtitleLabel.setText("For: " + appointmentDTO.getPetName() + " - " + appointmentDTO.getPetOwnerName());
        petInfoLabel.setText(appointmentDTO.getPetName() + " (" + appointmentDTO.getPetSpecies() + ")");
        doctorInfoLabel.setText(appointmentDTO.getDoctorName());
        feeLabel.setText(String.format("Rs. %.2f", appointmentDTO.getConsultationFee()));
        consultationFeeLabel.setText(String.format("Rs. %.2f", appointmentDTO.getConsultationFee()));
        updateTotals();
    }

    private void setupMedicineComboBox() {
        medicineComboBox.setItems(allMedicines);

        medicineComboBox.setCellFactory(param -> new ListCell<MedicineDTO>() {
            @Override
            protected void updateItem(MedicineDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null :
                        item.getName() + " - Rs. " + String.format("%.2f", item.getSellingPrice()));
            }
        });

        medicineComboBox.setButtonCell(new ListCell<MedicineDTO>() {
            @Override
            protected void updateItem(MedicineDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.getName());
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });

        // Show stock info when medicine selected
        medicineComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showStockInfo(newVal);
                medicineComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            } else {
                stockInfoCard.setVisible(false);
                stockInfoCard.setManaged(false);
            }
        });
    }

    private void showStockInfo(MedicineDTO medicine) {
        stockInfoLabel.setText(String.format(
                "Available: %d %s | Price: Rs. %.2f",
                medicine.getCurrentStock(),
                medicine.getUnit(),
                medicine.getSellingPrice()
        ));
        stockInfoCard.setVisible(true);
        stockInfoCard.setManaged(true);
    }

    private void setupQuantityControls() {
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(oldVal);
            }
            if (newVal.isEmpty()) {
                quantityField.setText("1");
            }
        });
    }

    @FXML
    private void handleIncreaseQuantity() {
        try {
            int current = Integer.parseInt(quantityField.getText());
            quantityField.setText(String.valueOf(current + 1));
        } catch (NumberFormatException e) {
            quantityField.setText("1");
        }
    }

    @FXML
    private void handleDecreaseQuantity() {
        try {
            int current = Integer.parseInt(quantityField.getText());
            if (current > 1) {
                quantityField.setText(String.valueOf(current - 1));
            }
        } catch (NumberFormatException e) {
            quantityField.setText("1");
        }
    }

    private void setupMedicineTable() {
        medicineTable.setItems(medicineRows);

        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Format price columns
        colPrice.setCellFactory(column -> new TableCell<MedicineRow, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("Rs. %.2f", item));
                setStyle("-fx-text-fill: #cccccc;");
            }
        });

        colSubtotal.setCellFactory(column -> new TableCell<MedicineRow, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("Rs. %.2f", item));
                setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
            }
        });

        // Action column - Remove button
        colAction.setCellFactory(param -> new TableCell<MedicineRow, Void>() {
            private final Button removeBtn = new Button("✕");

            {
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 15; " +
                        "-fx-font-size: 14;");
                removeBtn.setOnAction(event -> {
                    MedicineRow row = getTableView().getItems().get(getIndex());
                    handleRemoveMedicine(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });
    }

    private void setupSummaryBindings() {
        medicineRows.addListener((javafx.collections.ListChangeListener.Change<? extends MedicineRow> c) -> {
            updateTotals();
        });
    }

    @FXML
    private void handleAddMedicine() {
        MedicineDTO selected = medicineComboBox.getValue();
        if (selected == null) {
            showAlert("No Selection", "Please select a medicine.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Invalid Quantity", "Quantity must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Quantity", "Please enter a valid number.");
            return;
        }

        // Check stock availability
        if (quantity > selected.getCurrentStock()) {
            showAlert("Insufficient Stock",
                    String.format("Only %d units available in stock.", selected.getCurrentStock()));
            return;
        }

        // Check if already added
        Optional<MedicineRow> existing = medicineRows.stream()
                .filter(row -> row.getMedicineId() == selected.getMedicineId())
                .findFirst();

        if (existing.isPresent()) {
            MedicineRow row = existing.get();
            int newQty = row.getQuantity() + quantity;
            if (newQty > selected.getCurrentStock()) {
                showAlert("Insufficient Stock", "Cannot add more. Stock limit reached.");
                return;
            }
            row.setQuantity(newQty);
        } else {
            MedicineRow newRow = new MedicineRow(
                    selected.getMedicineId(),
                    selected.getName(),
                    quantity,
                    selected.getSellingPrice()
            );
            medicineRows.add(newRow);
        }

        quantityField.setText("1");
        medicineTable.refresh();
    }

    private void handleRemoveMedicine(MedicineRow row) {
        medicineRows.remove(row);
    }

    private void updateTotals() {
        double medicineTotal = medicineRows.stream()
                .mapToDouble(MedicineRow::getSubtotal)
                .sum();

        medicineTotalLabel.setText(String.format("Rs. %.2f", medicineTotal));

        double total = appointmentDTO.getConsultationFee() + medicineTotal;
        totalAmountLabel.setText(String.format("Rs. %.2f", total));
    }

    @FXML
    private void handleComplete() {
        // Validation
        String medicalHistory = medicalHistoryArea.getText().trim();
        if (medicalHistory.isEmpty()) {
            showAlert("Validation Error", "Please enter medical history/diagnosis.");
            return;
        }

        try {
            // 1. Update appointment status to completed
            boolean appointmentUpdated = appointmentModel.updateAppointmentStatus(
                    appointmentDTO.getAppointmentId(),
                    "completed"
            );

            if (!appointmentUpdated) {
                showAlert("Error", "Failed to update appointment status.");
                return;
            }

            // 2. Save medicines (if any)
            for (MedicineRow row : medicineRows) {
                AppointmentMedicineDTO medicineDTO = new AppointmentMedicineDTO(
                        appointmentDTO.getAppointmentId(),
                        row.getMedicineId(),
                        row.getQuantity()
                );

                boolean medicineSaved = appointmentMedicineModel.addMedicineToAppointment(medicineDTO);
                if (!medicineSaved) {
                    showAlert("Error", "Failed to save medicine: " + row.getMedicineName());
                    return;
                }

                // Update medicine stock
                boolean stockUpdated = appointmentMedicineModel.updateMedicineStock(
                        row.getMedicineId(),
                        row.getQuantity()
                );
                if (!stockUpdated) {
                    showAlert("Warning", "Failed to update stock for: " + row.getMedicineName());
                }
            }

            // 3. Save medical history
            MedicalHistoryDTO historyDTO = new MedicalHistoryDTO(
                    appointmentDTO.getPetId(),
                    appointmentDTO.getAppointmentId(),
                    medicalHistory
            );

            boolean historySaved = medicalHistoryModel.addMedicalHistory(historyDTO);
            if (!historySaved) {
                showAlert("Warning", "Failed to save medical history.");
            }

            // 4. Create payment record
            double totalAmount = appointmentDTO.getConsultationFee() +
                    medicineRows.stream().mapToDouble(MedicineRow::getSubtotal).sum();

            PaymentDTO paymentDTO = new PaymentDTO(
                    appointmentDTO.getAppointmentId(),
                    totalAmount,
                    "completed"
            );

            boolean paymentCreated = paymentModel.createPayment(paymentDTO);
            if (!paymentCreated) {
                showAlert("Warning", "Failed to create payment record.");
            }

            // Success
            showAlert("Success", "Appointment completed successfully!", Alert.AlertType.INFORMATION);

            // Refresh appointment list
            if (Reference.appointmentListView != null) {
                Reference.appointmentListView.initialize();
            }

            // Close window
            completeBtn.getScene().getWindow().hide();

        } catch (Exception e) {
            showAlert("Error", "Error completing appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        cancelBtn.getScene().getWindow().hide();
    }

    private void loadMedicines() {
        try {
            List<MedicineDTO> medicineList = medicineModel.getAllMedicines();
            if (medicineList != null) {
                allMedicines.setAll(medicineList);
            }
        } catch (Exception e) {
            showAlert("Error", "Error loading medicines: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        showAlert(title, content, Alert.AlertType.WARNING);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // ==================== INNER CLASS FOR TABLE ROW ====================

    public static class MedicineRow {
        private final IntegerProperty medicineId;
        private final StringProperty medicineName;
        private final IntegerProperty quantity;
        private final DoubleProperty price;
        private final DoubleProperty subtotal;

        public MedicineRow(int medicineId, String medicineName, int quantity, double price) {
            this.medicineId = new SimpleIntegerProperty(medicineId);
            this.medicineName = new SimpleStringProperty(medicineName);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = new SimpleDoubleProperty(price);
            this.subtotal = new SimpleDoubleProperty();

            this.quantity.addListener((obs, old, newVal) -> updateSubtotal());
            updateSubtotal();
        }

        private void updateSubtotal() {
            subtotal.set(quantity.get() * price.get());
        }

        public int getMedicineId() { return medicineId.get(); }
        public String getMedicineName() { return medicineName.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getPrice() { return price.get(); }
        public double getSubtotal() { return subtotal.get(); }

        public void setQuantity(int value) {
            quantity.set(value);
            updateSubtotal();
        }

        public IntegerProperty quantityProperty() { return quantity; }
        public DoubleProperty subtotalProperty() { return subtotal; }
    }
}