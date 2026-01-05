package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.model.MedicineModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.List;

public class MedicineView {

    @FXML
    private GridPane gridPane;

    private MedicineModel model = new MedicineModel();

    public void initialize() {
        if (Reference.medicineView == null) {
            Reference.medicineView = this;
        }

        try {
            if (!gridPane.getChildren().isEmpty()) {
                gridPane.getChildren().clear();
            }

            List<MedicineDTO> medicines = model.getAllMedicines();
            if (medicines != null && !medicines.isEmpty()) {
                includeGridPane(medicines);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No medicines available in the system.").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading medicines: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void includeGridPane(List<MedicineDTO> list) {
        int row = 0;

        for (MedicineDTO cardData : list) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/medicineCard.fxml"));
                AnchorPane card = loader.load();
                MedicineCard controller = loader.getController();
                controller.setData(cardData);

                GridPane.setFillWidth(gridPane, true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);

                gridPane.add(card, 0, row);
                row++;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error loading medicine card: " + e.getMessage());
            }
        }
    }

    public void search(String keyword) {
        removeElement();
        try {
            List<MedicineDTO> medicines = model.searchMedicines(keyword);

            if (medicines != null && !medicines.isEmpty()) {
                includeGridPane(medicines);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No medicines found matching: " + keyword).showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Search failed: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void searchById(String id) {
        removeElement();
        try {
            MedicineDTO medicine = model.getMedicineById(Integer.parseInt(id));

            if (medicine != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/medicineCard.fxml"));
                AnchorPane card = loader.load();
                MedicineCard controller = loader.getController();
                controller.setData(medicine);

                GridPane.setFillWidth(gridPane, true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);

                gridPane.add(card, 0, 0);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Medicine not found with ID: " + id).showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid medicine ID format.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void showLowStock() {
        removeElement();
        try {
            List<MedicineDTO> lowStockMedicines = model.getLowStockMedicines();

            if (lowStockMedicines != null && !lowStockMedicines.isEmpty()) {
                includeGridPane(lowStockMedicines);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "No low stock medicines found.").showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void removeElement() {
        try {
            gridPane.getChildren().clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error clearing view.").showAndWait();
        }
    }
}