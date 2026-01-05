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
import lk.ijse.petclinic.dto.PurchaseOrderDTO;
import lk.ijse.petclinic.model.PurchaseOrderModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

public  class PurchaseOrderCardController {

    @FXML
    private Label orderId;

    @FXML
    private Label supplierName;

    @FXML
    private Label orderDate;

    @FXML
    private Label totalAmount;

    @FXML
    private Label status;

    @FXML
    private Button viewBtn;

    @FXML
    private Button receiveBtn;

    @FXML
    private Button deleteBtn;

    private PurchaseOrderDTO purchaseOrderDTO;
    private PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();

    public void setPurchaseOrderDTO(PurchaseOrderDTO purchaseOrderDTO) {
        this.purchaseOrderDTO = purchaseOrderDTO;

        orderId.setText("PO" + String.format("%03d", purchaseOrderDTO.getOrderId()));
        supplierName.setText(purchaseOrderDTO.getSupplierName());
        orderDate.setText(purchaseOrderDTO.getOrderDate().toString());
        totalAmount.setText(String.format("Rs. %.2f", purchaseOrderDTO.getTotalAmount()));
        status.setText(purchaseOrderDTO.getStatus());

        // Style status based on value
        updateStatusStyle(purchaseOrderDTO.getStatus());

        // Hide receive button if already received
        if ("Received".equals(purchaseOrderDTO.getStatus())) {
            receiveBtn.setVisible(false);
            receiveBtn.setManaged(false);
        }
    }

    private void updateStatusStyle(String statusValue) {
        if ("Pending".equals(statusValue)) {
            status.setStyle(
                    "-fx-background-color: linear-gradient(to right, #ff9933, #ffaa55); " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-font-size: 11; " +
                            "-fx-background-radius: 12; " +
                            "-fx-padding: 4 12 4 12; " +
                            "-fx-border-color: #ffaa55; " +
                            "-fx-border-radius: 12; " +
                            "-fx-border-width: 1;"
            );
        } else if ("Received".equals(statusValue)) {
            status.setStyle(
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
        }
    }

    @FXML
    private void viewOrder() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/viewPurchaseOrder.fxml"));
            Parent root = fxmlLoader.load();

            ViewPurchaseOrderController controller = fxmlLoader.getController();
            controller.setPurchaseOrderDTO(purchaseOrderDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View Order Details - PO" + purchaseOrderDTO.getOrderId());
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(viewBtn.getScene().getWindow());

            stage.showAndWait();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error opening order details: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void receiveOrder() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Receive Order");
        confirm.setHeaderText("Mark order as received?");
        confirm.setContentText("This will update medicine stock levels.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = purchaseOrderModel.receiveOrder(purchaseOrderDTO.getOrderId());

                    if (success) {
                        new Alert(Alert.AlertType.INFORMATION, "Order received successfully! Stock updated.").showAndWait();
                        Reference.purchaseOrderListView.initialize();

                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to receive order").showAndWait();
                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void deleteOrder() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Order");
        confirm.setHeaderText("Delete this purchase order?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean success = purchaseOrderModel.deleteOrder(purchaseOrderDTO.getOrderId());

                    if (success) {
                        new Alert(Alert.AlertType.INFORMATION, "Order deleted successfully").showAndWait();
                        Reference.purchaseOrderListView.initialize();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete order").showAndWait();
                    }

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }
}
