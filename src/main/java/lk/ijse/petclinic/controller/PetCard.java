package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.model.PetModel;
import lk.ijse.petclinic.util.Reference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PetCard {

    @FXML
    private AnchorPane cardPane;

    @FXML
    private Label pname;

    @FXML
    private ImageView petImage;

    @FXML
    private Label dob;

    @FXML
    private Label pgen;

    @FXML
    private Label breed;

    @FXML
    private Hyperlink medicalHistoryLink;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    private PetDTO pet;

    public void setPetData(PetDTO pet) {
        this.pet = pet;

        if (pet != null) {
            // Set pet name
            pname.setText(pet.getName() != null ? pet.getName() : "Unknown");

            // Set date of birth
            if (pet.getDateOfBirth() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dob.setText(pet.getDateOfBirth().format(formatter));
            } else {
                dob.setText("-");
            }

            // Set gender
            pgen.setText(pet.getGender() != null ? pet.getGender() : "-");

            // Set breed
            breed.setText(pet.getBreed() != null ? pet.getBreed() : "-");

            // Load pet image
            loadPetImage(pet.getImageURL());
        }
    }

    private void loadPetImage(String imageURL) {
        // Always start with placeholder
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/image/icons8-no-image-100.png"));
            petImage.setImage(placeholder);
        } catch (Exception e) {
            petImage.setImage(null);
            System.err.println("Placeholder image not found!");
        }

        if (imageURL == null || imageURL.trim().isEmpty()) {
            return; // Keep placeholder
        }

        try {
            File file = new File(imageURL);
            if (file.exists()) {
                Image realImage = new Image(file.toURI().toString(), true); // background load
                petImage.setImage(realImage);
                System.out.println("Loaded pet image: " + imageURL);
            } else {
                System.err.println("Image file missing: " + imageURL);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imageURL);
            e.printStackTrace();
        }
    }

    @FXML
    public void editPet() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editPet.fxml"));
        Parent root = loader.load();

        EditPetController controller = loader.getController();
        controller.setData(pet, EditPetController.Mode.EDIT);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Pet");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(editBtn.getScene().getWindow());

        stage.showAndWait();

        // Refresh the pet view after editing
        if (Reference.petView != null) {
            Reference.petView.refreshPetList();
        }
    }

    @FXML
    public void deletePet() {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete " + pet.getName() + "?",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmAlert.setTitle("Delete Pet");
        confirmAlert.setHeaderText("Confirm Deletion");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            PetModel petModel = new PetModel();

            if (petModel.deletePet(pet.getPetId())) {
                // Show success message
                Alert successAlert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Pet deleted successfully!"
                );
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.showAndWait();

                // Refresh the pet view
                if (Reference.petView != null) {
                    Reference.petView.refreshPetList();
                }
            } else {
                // Show error message
                Alert errorAlert = new Alert(
                        Alert.AlertType.ERROR,
                        "Failed to delete pet. Please try again."
                );
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.showAndWait();
            }
        }
    }
}