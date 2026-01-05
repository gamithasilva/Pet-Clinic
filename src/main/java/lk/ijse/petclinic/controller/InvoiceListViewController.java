package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.InvoiceDTO;
import lk.ijse.petclinic.model.InvoiceModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.List;

public class InvoiceListViewController {

    @FXML
    private GridPane gridPane;

    private InvoiceModel model = new InvoiceModel();

    public void initialize() {
        if (Reference.invoiceListView == null) {
            Reference.invoiceListView = this;
        }

        try {
            if (!gridPane.getChildren().isEmpty()) {
                gridPane.getChildren().clear();
            }

            List<InvoiceDTO> invoices = model.getAllInvoices();
            if (invoices != null) {
                includeGridPane(invoices);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No invoices found").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error loading invoices: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void includeGridPane(List<InvoiceDTO> list) {
        int row = 0;

        for (InvoiceDTO invoiceData : list) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoiceCard.fxml"));
                AnchorPane card = loader.load();
                InvoiceCardController controller = loader.getController();
                controller.setData(invoiceData);

                GridPane.setFillWidth(gridPane, true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);

                gridPane.add(card, 0, row);
                row++;
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR,
                        "Error loading invoice card: " + e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }
    }

    public void search(String keyword) {
        removeElement();
        try {
            List<InvoiceDTO> invoices = model.searchInvoices(keyword);

            if (invoices != null && !invoices.isEmpty()) {
                includeGridPane(invoices);
            } else {
                new Alert(Alert.AlertType.INFORMATION,
                        "No invoices found matching '" + keyword + "'").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error searching invoices: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void removeElement() {
        try {
            gridPane.getChildren().clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error clearing grid: " + e.getMessage()).showAndWait();
        }
    }
}