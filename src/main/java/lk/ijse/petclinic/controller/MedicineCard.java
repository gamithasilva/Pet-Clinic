package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.model.MedicineModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.Optional;

public class MedicineCard {

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label medicineId;

    @FXML
    private Label medicineName;

    @FXML
    private Label genericName;

    @FXML
    private Label unit;

    @FXML
    private Label currentStock;

    @FXML
    private Label stockStatus;

    @FXML
    private Label reorderLevel;

    @FXML
    private Label buyingPrice;

    @FXML
    private Label sellingPrice;

    @FXML
    private ImageView medicineImage;

    private MedicineDTO medicineDTO;
    private MedicineModel model = new MedicineModel();

    @FXML
    void deleteMedicine(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Medicine");
        confirmAlert.setHeaderText("Are you sure you want to delete this medicine?");
        confirmAlert.setContentText("Medicine: " + medicineName.getText() + "\nThis action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = medicineId.getText().replace("M", "");

            if (model.deleteMedicine(Integer.parseInt(id))) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Medicine deleted successfully!");
                successAlert.showAndWait();

                if (Reference.medicineView != null) {
                    Reference.medicineView.initialize();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Delete Failed");
                errorAlert.setContentText("Unable to delete medicine. It may be referenced in other records.");
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    void editMedicine(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addMedicine.fxml"));
        Parent root = loader.load();

        AddMedicine controller = loader.getController();
        controller.setData(medicineDTO, AddMedicine.MODE.EDIT);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Medicine");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(editBtn.getScene().getWindow());

        stage.showAndWait();
    }

    public void setData(MedicineDTO dto) {
        if (medicineDTO == null) {
            this.medicineDTO = dto;
        }

        medicineId.setText("M" + String.format("%03d", dto.getMedicineId()));
        medicineName.setText(dto.getName());
        genericName.setText(dto.getGenericName());
        unit.setText(dto.getUnit());
        currentStock.setText(String.valueOf(dto.getCurrentStock()));
        reorderLevel.setText("Reorder at: " + dto.getReorderLevel());
        buyingPrice.setText(String.format("Rs. %.2f", dto.getBuyingPrice()));
        sellingPrice.setText(String.format("Rs. %.2f", dto.getSellingPrice()));

        // Update stock status with color coding
        updateStockStatus(dto.getCurrentStock(), dto.getReorderLevel());

        // Load medicine image if available
        if (dto.getImageURL() != null && !dto.getImageURL().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(dto.getImageURL()));
                medicineImage.setImage(image);
            } catch (Exception e) {
                // Use default image if loading fails
                System.err.println("Failed to load medicine image: " + e.getMessage());
            }
        }
    }

    private void updateStockStatus(int stock, int reorder) {
        if (stock == 0) {
            currentStock.setStyle("-fx-text-fill: #F44336; -fx-font-size: 14; -fx-font-weight: bold;");
            stockStatus.setText("Out of Stock");
            stockStatus.setStyle("-fx-text-fill: #F44336; -fx-font-size: 9; -fx-background-color: rgba(244, 67, 54, 0.2); -fx-background-radius: 8; -fx-padding: 2 6 2 6; -fx-font-weight: bold;");
        } else if (stock <= reorder) {
            currentStock.setStyle("-fx-text-fill: #FF9800; -fx-font-size: 14; -fx-font-weight: bold;");
            stockStatus.setText("Low Stock");
            stockStatus.setStyle("-fx-text-fill: #FF9800; -fx-font-size: 9; -fx-background-color: rgba(255, 152, 0, 0.2); -fx-background-radius: 8; -fx-padding: 2 6 2 6; -fx-font-weight: bold;");
        } else {
            currentStock.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 14; -fx-font-weight: bold;");
            stockStatus.setText("In Stock");
            stockStatus.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 9; -fx-background-color: rgba(76, 175, 80, 0.2); -fx-background-radius: 8; -fx-padding: 2 6 2 6; -fx-font-weight: bold;");
        }
    }
}