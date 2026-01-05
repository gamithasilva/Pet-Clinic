package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.model.PetModel;
import lk.ijse.petclinic.util.Reference;

import java.util.List;

public class PetView {

    @FXML
    private GridPane gridPane;

    private PetModel petModel = new PetModel();

    @FXML
    public void initialize() {

        if (Reference.petView == null) {
            Reference.petView = this;
        }


        refreshPetList();
    }


    public void refreshPetList() {
        try {
            List<PetDTO> pets = petModel.getAllPet();

            if (pets != null && !pets.isEmpty()) {
                loadPets(pets);
            } else {

                gridPane.getChildren().clear();
                System.out.println("No pets found in database");
            }
        } catch (Exception e) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Error loading pets: " + e.getMessage()
            );
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    public void loadPets(List<PetDTO> pets) {

        gridPane.getChildren().clear();

        int row = 0;
        int col = 0;

        for (PetDTO pet : pets) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/petCard.fxml"));
                AnchorPane pane = loader.load();
                PetCard controller = loader.getController();
                controller.setPetData(pet);

                gridPane.add(pane, col, row);

                col++;
                if (col > 5) {
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                System.err.println("Error loading pet card: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void search(String id) {
        if (id == null || id.trim().isEmpty()) {

            refreshPetList();
            return;
        }

        try {

            gridPane.getChildren().clear();


            PetDTO pet = petModel.getOnePet(id);

            if (pet != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/petCard.fxml"));
                AnchorPane pane = loader.load();
                PetCard controller = loader.getController();
                controller.setPetData(pet);

                gridPane.add(pane, 0, 0);
            } else {
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "No pet found with ID: " + id
                );
                alert.setTitle("Search Result");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Error searching for pet: " + e.getMessage()
            );
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    public void removeElement() {
        gridPane.getChildren().clear();
    }
}