package lk.ijse.petclinic.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetModel;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class EditPetController {

    @FXML
    private TextField breedField;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextArea medicalHistoryArea;

    @FXML
    private ComboBox<PetOwnerDTO> ownerComboBox1;

    @FXML
    private ImageView petImageView;

    @FXML
    private TextField petNameField;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField speciesField;

    @FXML
    private Button uploadImageBtn;

    public enum Mode {ADD,EDIT};
    private Mode currentMode;

    private PetOwnerModel ownerModel = new PetOwnerModel();
    private PetModel petModel = new PetModel();

    private PetDTO pet;
    String imagePath;

    public void setData(PetDTO pet, Mode mode){
        this.pet = pet;
        this.currentMode = mode;

        if(ownerComboBox1.getItems() != null){
            loadPetDataToForm();
        }
    }

    public void setMode(Mode mode){
        this.currentMode = mode;
    }

    @FXML
    public void initialize() {
        // Load owners
        List<PetOwnerDTO> ownerList = ownerModel.getALlPetOwner();
        ownerComboBox1.setItems(FXCollections.observableList(ownerList));

        // Cell factories for ownerComboBox
        ownerComboBox1.setCellFactory(lv -> new ListCell<PetOwnerDTO>() {
            @Override
            protected void updateItem(PetOwnerDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPet_owner_id() + " : " + item.getName());
            }
        });

        ownerComboBox1.setButtonCell(new ListCell<PetOwnerDTO>() {
            @Override
            protected void updateItem(PetOwnerDTO item, boolean empty) {
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

        // Add value change listener for styling
        ownerComboBox1.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ownerComboBox1.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 8; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 8; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            }
        });

        // Setup gender ComboBox
        genderComboBox.getItems().addAll("Male", "Female");

        // Add custom button cell for gender ComboBox
        genderComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item);
                    setStyle("-fx-text-fill: #00ff88; -fx-font-weight: bold;");
                } else {
                    setText(null);
                    setStyle("");
                }
            }
        });

        // Add value change listener for gender ComboBox styling
        genderComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                genderComboBox.setStyle(
                        "-fx-background-color: linear-gradient(to right, #1a4d2e, #2d5f3f); " +
                                "-fx-text-fill: #00ff88; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 8; " +
                                "-fx-border-color: #00ff88; " +
                                "-fx-border-radius: 8; " +
                                "-fx-border-width: 1; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0, 255, 136, 0.3), 8, 0, 0, 0);"
                );
            }
        });

        // Set default placeholder
        loadPlaceholderImage();
    }

    @FXML
    private void uploadImageBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Pet image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg","*.jpeg"));

        File file = fileChooser.showOpenDialog(uploadImageBtn.getScene().getWindow());
        if (file != null){
            try{
                File destDir = new File("src/main/resources/image/pet");
                if (!destDir.exists()){
                    destDir.mkdir();
                }

                String newFileName = System.currentTimeMillis() + "_" + file.getName();
                File destFile = new File(destDir, newFileName);

                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image(destFile.toURI().toString());
                petImageView.setImage(image);

                imagePath = destFile.getAbsolutePath();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        close();
    }

    @FXML
    private void savePet(ActionEvent event) {
        if (currentMode == Mode.ADD){
            addPet();
        }else {
            updatePet();
        }
        close();
        Reference.petView.initialize();
    }

    public void setPetDto(PetDTO pet){
        this.pet = pet;
    }

    private void addPet(){
        if(ownerComboBox1.hasProperties() || petNameField.getText().isEmpty() || breedField.getText().isEmpty() || speciesField.getText().isEmpty() || dobPicker.hasProperties() || genderComboBox.hasProperties()){
            new Alert(Alert.AlertType.ERROR,"please fill all the data").showAndWait();
        }
        pet = new PetDTO();

        pet.setPetOwnerId(ownerComboBox1.getValue().getPet_owner_id());
        pet.setName(petNameField.getText());
        pet.setBreed(breedField.getText());
        pet.setSpecies(speciesField.getText());
        pet.setGender(genderComboBox.getValue());
        pet.setDateOfBirth(dobPicker.getValue());
        pet.setImageURL(imagePath);

        if(petModel.addPet(pet)){
            new Alert(Alert.AlertType.INFORMATION,"Pet added successfully").showAndWait();
        }else{
            new Alert(Alert.AlertType.ERROR,"error in adding").showAndWait();
        }
    }

    private void updatePet(){
        // Transfer current form values to the pet object
        pet.setName(petNameField.getText());
        pet.setSpecies(speciesField.getText());
        pet.setBreed(breedField.getText());
        pet.setGender(genderComboBox.getValue());
        pet.setDateOfBirth(dobPicker.getValue());
        pet.setPetOwnerId(ownerComboBox1.getValue().getPet_owner_id());

        // Crucial: Update image only if a new one was uploaded
        if (imagePath != null) {
            pet.setImageURL(imagePath);
        }

        if(petModel.updatePet(pet)){
            new Alert(Alert.AlertType.CONFIRMATION,"Pet updated successfully").showAndWait();
        }else{
            new Alert(Alert.AlertType.ERROR,"Error in updating").showAndWait();
        }
    }

    public void close(){
        Stage stage = (Stage) uploadImageBtn.getScene().getWindow();
        stage.close();
    }

    private void loadPetDataToForm() {
        if (pet == null) return;

        // Basic fields
        petNameField.setText(pet.getName() != null ? pet.getName() : "");
        speciesField.setText(pet.getSpecies() != null ? pet.getSpecies() : "");
        breedField.setText(pet.getBreed() != null ? pet.getBreed() : "");
        genderComboBox.setValue(pet.getGender());
        dobPicker.setValue(pet.getDateOfBirth());

        // Owner selection
        if (pet.getPetOwnerId() > 0) {
            List<PetOwnerDTO> ownerList = ownerComboBox1.getItems();
            if (ownerList != null) {
                for (PetOwnerDTO owner : ownerList) {
                    if (owner.getPet_owner_id() == pet.getPetOwnerId()) {
                        ownerComboBox1.setValue(owner);
                        break;
                    }
                }
            }
        }

        // Load pet image
        if (pet.getImageURL() != null && !pet.getImageURL().trim().isEmpty()) {
            try {
                File imageFile = new File(pet.getImageURL());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    petImageView.setImage(image);
                    imagePath = pet.getImageURL();
                } else {
                    loadPlaceholderImage();
                }
            } catch (Exception e) {
                loadPlaceholderImage();
                e.printStackTrace();
            }
        } else {
            loadPlaceholderImage();
        }
    }

    private void loadPlaceholderImage() {
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/image/icons8-no-image-100.png"));
            petImageView.setImage(placeholder);
        } catch (Exception e) {
            petImageView.setImage(null);
        }
    }
}