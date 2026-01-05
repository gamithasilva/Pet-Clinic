package lk.ijse.petclinic.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lk.ijse.petclinic.dto.PurchaseOrderDTO;
import lk.ijse.petclinic.dto.PurchaseOrderItemDTO;
import lk.ijse.petclinic.model.PurchaseOrderModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for viewing Purchase Order details
 */
public class ViewPurchaseOrderController {

    @FXML
    private Label orderTitle;

    @FXML
    private Label orderStatus;

    @FXML
    private Label supplierNameLabel;

    @FXML
    private Label orderDateLabel;

    @FXML
    private VBox receivedDateCard;

    @FXML
    private Label receivedDateLabel;

    @FXML
    private TableView<PurchaseOrderItemDTO> itemsTable;

    @FXML
    private TableColumn<PurchaseOrderItemDTO, String> colMedicineName;

    @FXML
    private TableColumn<PurchaseOrderItemDTO, String> colGenericName;

    @FXML
    private TableColumn<PurchaseOrderItemDTO, Integer> colQuantity;

    @FXML
    private TableColumn<PurchaseOrderItemDTO, Double> colUnitPrice;

    @FXML
    private TableColumn<PurchaseOrderItemDTO, Double> colSubtotal;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Button closeBtn;

    private PurchaseOrderDTO purchaseOrderDTO;
    private PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
    private ObservableList<PurchaseOrderItemDTO> orderItems = FXCollections.observableArrayList();

    public void initialize() {
        setupItemsTable();
    }

    private void setupItemsTable() {
        itemsTable.setItems(orderItems);

        // Set cell value factories
        colMedicineName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colGenericName.setCellValueFactory(new PropertyValueFactory<>("genericName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitBuyingPrice"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Format currency columns
        colUnitPrice.setCellFactory(column -> new TableCell<PurchaseOrderItemDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Rs. %.2f", item));
                    setStyle("-fx-text-fill: #cccccc;");
                }
            }
        });

        colSubtotal.setCellFactory(column -> new TableCell<PurchaseOrderItemDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Rs. %.2f", item));
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                }
            }
        });

        // Center align quantity
        colQuantity.setCellFactory(column -> new TableCell<PurchaseOrderItemDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                    setStyle("-fx-alignment: CENTER; -fx-text-fill: white; -fx-font-weight: bold;");
                }
            }
        });
    }

    public void setPurchaseOrderDTO(PurchaseOrderDTO orderDTO) {
        this.purchaseOrderDTO = orderDTO;

        try {
            // Load full order details with items from database
            PurchaseOrderDTO fullOrder = purchaseOrderModel.getOrderById(orderDTO.getOrderId());

            if (fullOrder != null) {
                displayOrderDetails(fullOrder);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to load order details").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading order: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    private void displayOrderDetails(PurchaseOrderDTO order) {
        // Set header info
        orderTitle.setText("Purchase Order #PO" + String.format("%03d", order.getOrderId()));
        orderStatus.setText(order.getStatus());
        updateStatusStyle(order.getStatus());

        // Set basic info
        supplierNameLabel.setText(order.getSupplierName());
        orderDateLabel.setText(order.getOrderDate().toString());

        // Show received date if order is received
        if ("Received".equals(order.getStatus()) && order.getReceivedDate() != null) {
            receivedDateCard.setVisible(true);
            receivedDateCard.setManaged(true);
            receivedDateLabel.setText(order.getReceivedDate().toString());
        }

        // Load order items
        if (order.getItems() != null) {
            orderItems.setAll(order.getItems());
            totalItemsLabel.setText(String.valueOf(order.getItems().size()));
        }

        // Set total amount
        totalAmountLabel.setText(String.format("Rs. %.2f", order.getTotalAmount()));
    }

    private void updateStatusStyle(String statusValue) {
        if ("Pending".equals(statusValue)) {
            orderStatus.setStyle(
                    "-fx-background-color: linear-gradient(to right, #ff9933, #ffaa55); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 12; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 15 5 15; " +
                            "-fx-border-color: #ffaa55; " +
                            "-fx-border-radius: 15; " +
                            "-fx-border-width: 1;"
            );
        } else if ("Received".equals(statusValue)) {
            orderStatus.setStyle(
                    "-fx-background-color: linear-gradient(to right, #00cc66, #00ff88); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 12; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 15 5 15; " +
                            "-fx-border-color: #00ff88; " +
                            "-fx-border-radius: 15; " +
                            "-fx-border-width: 1;"
            );
        }
    }

    @FXML
    private void handleClose() {
        closeBtn.getScene().getWindow().hide();
    }
}