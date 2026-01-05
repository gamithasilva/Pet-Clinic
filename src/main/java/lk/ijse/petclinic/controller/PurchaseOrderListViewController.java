package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PurchaseOrderDTO;
import lk.ijse.petclinic.model.PurchaseOrderModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Purchase Order ListView (GridPane with cards)
 */
public class PurchaseOrderListViewController {

    @FXML
    private GridPane gridPane;

    private PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();

    public void initialize() {
        Reference.purchaseOrderListView = this;
        try {
            if (!gridPane.getChildren().isEmpty()) {
                gridPane.getChildren().clear();
            }

            List<PurchaseOrderDTO> orders = purchaseOrderModel.getAllOrders();
            if (orders != null) {
                includeInGridPane(orders);
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading orders: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void includeInGridPane(List<PurchaseOrderDTO> orderList) {
        int row = 0;

        for (PurchaseOrderDTO order : orderList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/purchaseOrderCard.fxml"));
                AnchorPane card = loader.load();

                PurchaseOrderCardController cardController = loader.getController();
                cardController.setPurchaseOrderDTO(order);

                GridPane.setFillWidth(card, true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(card, 0, row);

                row++;
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error loading order card: " + e.getMessage()).showAndWait();
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

            List<PurchaseOrderDTO> orders = purchaseOrderModel.searchOrders(keyword);
            if (orders != null) {
                includeInGridPane(orders);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No orders found").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search error: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }
}