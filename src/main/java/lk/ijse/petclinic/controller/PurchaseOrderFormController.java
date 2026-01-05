package lk.ijse.petclinic.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.dto.PurchaseOrderDTO;
import lk.ijse.petclinic.dto.PurchaseOrderItemDTO;
import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.model.MedicineModel;
import lk.ijse.petclinic.model.PurchaseOrderModel;
import lk.ijse.petclinic.model.SupplierModel;
import lk.ijse.petclinic.util.Reference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Purchase Order Form (MVC Architecture)
 */
public class PurchaseOrderFormController {

    // FXML Components - Top Section
    @FXML private ComboBox<SupplierDTO> supplierComboBox;
    @FXML private DatePicker orderDatePicker;

    // FXML Components - Left Panel
    @FXML private TextField searchField;
    @FXML private ListView<MedicineDTO> medicineListView;
    @FXML private TextField quantityField;
    @FXML private Button increaseBtn;
    @FXML private Button decreaseBtn;
    @FXML private Label unitLabel;
    @FXML private Button addMedicineBtn;
    @FXML private Label stockInfoLabel;

    // FXML Components - Right Panel (Table)
    @FXML private TableView<OrderItemRow> orderItemsTable;
    @FXML private TableColumn<OrderItemRow, String> colMedicineName;
    @FXML private TableColumn<OrderItemRow, String> colGenericName;
    @FXML private TableColumn<OrderItemRow, Integer> colQuantity;
    @FXML private TableColumn<OrderItemRow, Double> colUnitPrice;
    @FXML private TableColumn<OrderItemRow, Double> colSubtotal;
    @FXML private TableColumn<OrderItemRow, Void> colAction;

    // FXML Components - Summary
    @FXML private Label totalItemsLabel;
    @FXML private Label totalAmountLabel;

    // FXML Components - Action Buttons
    @FXML private Button clearBtn;
    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    // Models
    private final PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
    private final MedicineModel medicineModel = new MedicineModel();
    private final SupplierModel supplierModel = new SupplierModel();

    // Observable Lists
    private final ObservableList<MedicineDTO> allMedicines = FXCollections.observableArrayList();
    private final ObservableList<MedicineDTO> filteredMedicines = FXCollections.observableArrayList();
    private final ObservableList<OrderItemRow> orderItems = FXCollections.observableArrayList();
    private final ObservableList<SupplierDTO> suppliers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSupplierComboBox();
        setupDatePicker();
        setupMedicineSearch();
        setupQuantityControls();
        setupOrderItemsTable();
        setupSummaryBindings();
        loadData();
    }

    private void setupSupplierComboBox() {
        supplierComboBox.setItems(suppliers);

        supplierComboBox.setCellFactory(param -> new ListCell<SupplierDTO>() {
            @Override
            protected void updateItem(SupplierDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        supplierComboBox.setButtonCell(new ListCell<SupplierDTO>() {
            @Override
            protected void updateItem(SupplierDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
                if (!empty && item != null) {
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                }
            }
        });

        // Change style when supplier is selected
        supplierComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                supplierComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            }
        });
    }

    private void setupDatePicker() {
        orderDatePicker.setValue(LocalDate.now());
    }

    private void setupMedicineSearch() {
        medicineListView.setItems(filteredMedicines);

        medicineListView.setCellFactory(param -> new ListCell<MedicineDTO>() {
            @Override
            protected void updateItem(MedicineDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%s (%s) - Stock: %d %s",
                            item.getName(),
                            item.getGenericName(),
                            item.getCurrentStock(),
                            item.getUnit()));

                    // Default style
                    setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13; -fx-padding: 10; -fx-background-color: transparent;");
                }
            }
        });

        // Change background color when item is selected
        medicineListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // Refresh all cells to update colors
            medicineListView.refresh();
        });

        // Override the cell selection color
        medicineListView.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-border-color: #333333; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10; " +
                        "-fx-selection-bar: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                        "-fx-selection-bar-non-focused: linear-gradient(to right, #1a4d2e, #2d5f3f);"
        );

        // Search filter
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterMedicines(newVal);
        });

        // Show stock info and unit on selection
        medicineListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                stockInfoLabel.setText(String.format(
                        "Stock: %d %s | Buy: Rs. %.2f | Sell: Rs. %.2f",
                        newVal.getCurrentStock(),
                        newVal.getUnit(),
                        newVal.getBuyingPrice(),
                        newVal.getSellingPrice()
                ));
                stockInfoLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12; -fx-font-weight: bold;");
                unitLabel.setText(newVal.getUnit());
            } else {
                stockInfoLabel.setText("Select a medicine to see details");
                stockInfoLabel.setStyle("-fx-text-fill: #999999; -fx-font-size: 12;");
                unitLabel.setText("units");
            }
        });

        // Double-click to add
        medicineListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleAddMedicine();
            }
        });
    }

    private void setupQuantityControls() {
        // Only allow numbers in quantity field
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
            int currentQty = Integer.parseInt(quantityField.getText());
            quantityField.setText(String.valueOf(currentQty + 1));
        } catch (NumberFormatException e) {
            quantityField.setText("1");
        }
    }

    @FXML
    private void handleDecreaseQuantity() {
        try {
            int currentQty = Integer.parseInt(quantityField.getText());
            if (currentQty > 1) {
                quantityField.setText(String.valueOf(currentQty - 1));
            }
        } catch (NumberFormatException e) {
            quantityField.setText("1");
        }
    }

    private void setupOrderItemsTable() {
        orderItemsTable.setItems(orderItems);
        orderItemsTable.setEditable(true);

        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colGenericName.setCellValueFactory(new PropertyValueFactory<>("genericName"));

        // Quantity Column (Editable)
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colQuantity.setOnEditCommit(event -> {
            OrderItemRow item = event.getRowValue();
            int newQty = event.getNewValue();
            if (newQty > 0) {
                item.setQuantity(newQty);
            } else {
                showAlert("Invalid Quantity", "Quantity must be greater than 0.");
                orderItemsTable.refresh();
            }
        });

        // Unit Price Column (Editable)
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colUnitPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colUnitPrice.setOnEditCommit(event -> {
            OrderItemRow item = event.getRowValue();
            double newPrice = event.getNewValue();
            if (newPrice > 0) {
                item.setUnitPrice(newPrice);
            } else {
                showAlert("Invalid Price", "Price must be greater than 0.");
                orderItemsTable.refresh();
            }
        });

        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Action Column (Remove Button)
        colAction.setCellFactory(param -> new TableCell<OrderItemRow, Void>() {
            private final Button removeBtn = new Button("✕");

            {
                removeBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 15; " +
                        "-fx-font-size: 16;");
                removeBtn.setOnAction(event -> {
                    OrderItemRow item = getTableView().getItems().get(getIndex());
                    handleRemoveItem(item);
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
        orderItems.addListener((javafx.collections.ListChangeListener.Change<? extends OrderItemRow> c) -> {
            updateSummary();
        });
    }

    private void filterMedicines(String searchText) {
        filteredMedicines.clear();
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredMedicines.addAll(allMedicines);
        } else {
            String lowerSearch = searchText.toLowerCase();
            allMedicines.stream()
                    .filter(med -> med.getName().toLowerCase().contains(lowerSearch) ||
                            med.getGenericName().toLowerCase().contains(lowerSearch))
                    .forEach(filteredMedicines::add);
        }
    }

    @FXML
    private void handleAddMedicine() {
        MedicineDTO selected = medicineListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a medicine to add.");
            return;
        }

        // Get quantity from field
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert("Invalid Quantity", "Please enter a quantity greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Quantity", "Please enter a valid number.");
            return;
        }

        // Check if already in order
        Optional<OrderItemRow> existing = orderItems.stream()
                .filter(item -> item.getMedicineId() == selected.getMedicineId())
                .findFirst();

        if (existing.isPresent()) {
            OrderItemRow item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            orderItemsTable.getSelectionModel().select(item);
            orderItemsTable.scrollTo(item);
        } else {
            OrderItemRow newItem = new OrderItemRow(
                    selected.getMedicineId(),
                    selected.getName(),
                    selected.getGenericName(),
                    quantity,
                    selected.getBuyingPrice()
            );
            orderItems.add(newItem);
            orderItemsTable.getSelectionModel().select(newItem);
            orderItemsTable.scrollTo(newItem);
        }

        // Reset quantity to 1
        quantityField.setText("1");
    }

    private void handleRemoveItem(OrderItemRow item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Item");
        confirm.setHeaderText("Remove " + item.getMedicineName() + "?");
        confirm.setContentText("Are you sure you want to remove this item from the order?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                orderItems.remove(item);
            }
        });
    }

    @FXML
    private void handleClearAll() {
        if (orderItems.isEmpty()) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear All Items");
        confirm.setHeaderText("Clear all items from this order?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                orderItems.clear();
            }
        });
    }

    @FXML
    private void handleCancel() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Order");
        confirm.setHeaderText("Cancel this purchase order?");
        confirm.setContentText("All unsaved changes will be lost.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cancelBtn.getScene().getWindow().hide();
            }
        });
    }

    @FXML
    private void handleSaveOrder() {
        // Validation
        if (supplierComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a supplier.");
            return;
        }

        if (orderItems.isEmpty()) {
            showAlert("Validation Error", "Please add at least one item to the order.");
            return;
        }

        try {
            // Prepare DTO
            PurchaseOrderDTO orderDTO = new PurchaseOrderDTO();
            orderDTO.setSupplierId(supplierComboBox.getValue().getSupplierId());
            orderDTO.setOrderDate(orderDatePicker.getValue());
            orderDTO.setTotalAmount(calculateTotalAmount());
            orderDTO.setStatus("Pending");

            // Convert OrderItemRow to PurchaseOrderItemDTO
            List<PurchaseOrderItemDTO> items = new ArrayList<>();
            for (OrderItemRow row : orderItems) {
                PurchaseOrderItemDTO itemDTO = new PurchaseOrderItemDTO(
                        row.getMedicineId(),
                        row.getQuantity(),
                        row.getUnitPrice()
                );
                items.add(itemDTO);
            }
            orderDTO.setItems(items);

            // Save via Model
            boolean saved = purchaseOrderModel.savePurchaseOrder(orderDTO);

            if (saved) {
                showAlert("Success", "Purchase order saved successfully!", Alert.AlertType.INFORMATION);

//                // Refresh the list if it's loaded in Medicine tab
//                if (Reference.purchaseOrderListView != null) {
//                    Reference.purchaseOrderListView.initialize();
//                }

                // Close the window
                saveBtn.getScene().getWindow().hide();
            } else {
                showAlert("Error", "Failed to save purchase order.");
            }

        } catch (Exception e) {
            showAlert("Error", "Error saving order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSummary() {
        totalItemsLabel.setText(String.valueOf(orderItems.size()));
        totalAmountLabel.setText(String.format("Rs. %.2f", calculateTotalAmount()));
    }

    private double calculateTotalAmount() {
        return orderItems.stream()
                .mapToDouble(OrderItemRow::getSubtotal)
                .sum();
    }

    private void loadData() {
        try {
            // Load medicines
            List<MedicineDTO> medicineList = medicineModel.getAllMedicines();
            if (medicineList != null) {
                allMedicines.setAll(medicineList);
                filteredMedicines.setAll(medicineList);
            }

            // Load suppliers
            List<SupplierDTO> supplierList = supplierModel.getAllSuppliers();
            if (supplierList != null) {
                suppliers.setAll(supplierList);
            }

        } catch (Exception e) {
            showAlert("Error", "Error loading data: " + e.getMessage());
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

    public static class OrderItemRow {
        private final IntegerProperty medicineId;
        private final StringProperty medicineName;
        private final StringProperty genericName;
        private final IntegerProperty quantity;
        private final DoubleProperty unitPrice;
        private final DoubleProperty subtotal;

        public OrderItemRow(int medicineId, String medicineName, String genericName,
                            int quantity, double unitPrice) {
            this.medicineId = new SimpleIntegerProperty(medicineId);
            this.medicineName = new SimpleStringProperty(medicineName);
            this.genericName = new SimpleStringProperty(genericName);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.unitPrice = new SimpleDoubleProperty(unitPrice);
            this.subtotal = new SimpleDoubleProperty();

            this.quantity.addListener((obs, old, newVal) -> updateSubtotal());
            this.unitPrice.addListener((obs, old, newVal) -> updateSubtotal());
            updateSubtotal();
        }

        private void updateSubtotal() {
            subtotal.set(quantity.get() * unitPrice.get());
        }

        public int getMedicineId() { return medicineId.get(); }
        public String getMedicineName() { return medicineName.get(); }
        public String getGenericName() { return genericName.get(); }
        public int getQuantity() { return quantity.get(); }
        public double getUnitPrice() { return unitPrice.get(); }
        public double getSubtotal() { return subtotal.get(); }

        public void setQuantity(int value) { quantity.set(value); }
        public void setUnitPrice(double value) { unitPrice.set(value); }

        public IntegerProperty quantityProperty() { return quantity; }
        public DoubleProperty unitPriceProperty() { return unitPrice; }
        public DoubleProperty subtotalProperty() { return subtotal; }
    }
}