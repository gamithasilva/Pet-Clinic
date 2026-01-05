package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.model.MedicineModel;
import lk.ijse.petclinic.util.Reference;

import java.io.File;

public class AddMedicine {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button uploadImageBtn;

    @FXML
    private TextField medicineName;

    @FXML
    private TextField genericName;

    @FXML
    private TextField unit;

    @FXML
    private TextField buyingPrice;

    @FXML
    private TextField sellingPrice;

    @FXML
    private TextField currentStock;

    @FXML
    private TextField reorderLevel;

    private String imageURL;
    private MedicineModel model = new MedicineModel();
    private MedicineDTO dto;
    private MODE currentMode;

    public enum MODE {
        ADD, EDIT
    }

    public void setData(MedicineDTO dto, MODE mode) {
        this.currentMode = mode;
        this.dto = dto;

        if (mode == MODE.EDIT) {
            loadData();
        }
    }

    public void setMode(MODE mode) {
        this.currentMode = mode;
    }

    public void initialize() {
        // Add validation listeners
        setupNumericValidation(buyingPrice);
        setupNumericValidation(sellingPrice);
        setupIntegerValidation(currentStock);
        setupIntegerValidation(reorderLevel);
    }

    @FXML
    void cancel(ActionEvent event) {
        close();
    }

    @FXML
    void saveMedicine(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        if (currentMode == MODE.ADD) {
            addMedicine();
        } else {
            updateMedicine();
        }

        close();
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Medicine Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());

        if (selectedFile != null) {
            imageURL = "/image/" + selectedFile.getName();
            new Alert(Alert.AlertType.INFORMATION, "Image selected: " + selectedFile.getName()).showAndWait();
        }
    }

    private void addMedicine() {
        try {
            dto = new MedicineDTO();
            dto.setName(medicineName.getText().trim());
            dto.setGenericName(genericName.getText().trim());
            dto.setUnit(unit.getText().trim());
            dto.setBuyingPrice(Double.parseDouble(buyingPrice.getText().trim()));
            dto.setSellingPrice(Double.parseDouble(sellingPrice.getText().trim()));
            dto.setCurrentStock(Integer.parseInt(currentStock.getText().trim()));
            dto.setReorderLevel(Integer.parseInt(reorderLevel.getText().trim()));
            dto.setImageURL(imageURL);

            if (model.addMedicine(dto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Medicine added successfully!").showAndWait();
                if (Reference.medicineView != null) {
                    Reference.medicineView.initialize();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add medicine.").showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in price or stock fields.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    private void updateMedicine() {
        try {
            dto.setName(medicineName.getText().trim());
            dto.setGenericName(genericName.getText().trim());
            dto.setUnit(unit.getText().trim());
            dto.setBuyingPrice(Double.parseDouble(buyingPrice.getText().trim()));
            dto.setSellingPrice(Double.parseDouble(sellingPrice.getText().trim()));
            dto.setCurrentStock(Integer.parseInt(currentStock.getText().trim()));
            dto.setReorderLevel(Integer.parseInt(reorderLevel.getText().trim()));

            if (imageURL != null) {
                dto.setImageURL(imageURL);
            }

            if (model.updateMedicine(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Medicine updated successfully!").showAndWait();
                if (Reference.medicineView != null) {
                    Reference.medicineView.initialize();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update medicine.").showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in price or stock fields.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).showAndWait();
        }
    }

    private boolean validateFields() {
        if (medicineName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter medicine name.").showAndWait();
            return false;
        }

        if (genericName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter generic name.").showAndWait();
            return false;
        }

        if (unit.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter unit.").showAndWait();
            return false;
        }

        try {
            double buy = Double.parseDouble(buyingPrice.getText().trim());
            double sell = Double.parseDouble(sellingPrice.getText().trim());

            if (buy < 0 || sell < 0) {
                new Alert(Alert.AlertType.WARNING, "Prices cannot be negative.").showAndWait();
                return false;
            }

            if (sell < buy) {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Selling price is lower than buying price. Continue anyway?");
                return alert.showAndWait().get() == alert.getButtonTypes().get(0);
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Invalid price format.").showAndWait();
            return false;
        }

        try {
            int stock = Integer.parseInt(currentStock.getText().trim());
            int reorder = Integer.parseInt(reorderLevel.getText().trim());

            if (stock < 0 || reorder < 0) {
                new Alert(Alert.AlertType.WARNING, "Stock values cannot be negative.").showAndWait();
                return false;
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Invalid stock format.").showAndWait();
            return false;
        }

        return true;
    }

    private void setupNumericValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private void setupIntegerValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

    private void loadData() {
        if (dto == null) {
            return;
        }

        medicineName.setText(dto.getName());
        genericName.setText(dto.getGenericName());
        unit.setText(dto.getUnit());
        buyingPrice.setText(String.valueOf(dto.getBuyingPrice()));
        sellingPrice.setText(String.valueOf(dto.getSellingPrice()));
        currentStock.setText(String.valueOf(dto.getCurrentStock()));
        reorderLevel.setText(String.valueOf(dto.getReorderLevel()));
        imageURL = dto.getImageURL();
    }

    private void close() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
}