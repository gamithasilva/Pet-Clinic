package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetModel;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

public class PetController {
    @FXML
    private Button addOwnerBtn;

    @FXML
    private Button addPetBtn;

    @FXML
    private AnchorPane ownerContentPane;

    @FXML
    private TextField ownerSearchBox;

    @FXML
    private AnchorPane petContentPane;

    @FXML
    private TextField petSearchBox;

    @FXML
    private Button refreshOwnerBtn;

    @FXML
    private Button refreshPetBtn;

    @FXML
    private Tab tab2;

    @FXML
    private BorderPane ownerbpane;

    @FXML
    private BorderPane petbpane;

    @FXML
    private TabPane tabPane;

    private PetOwnerModel pomodel = new PetOwnerModel();
    private PetModel pmodel = new PetModel();

    @FXML
    private void initialize() throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OwnerView.fxml"));
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/petView.fxml"));
        Parent root = loader.load();
        Parent parent2 = loader2.load();
        ownerbpane.setCenter(root);
        petbpane.setCenter(parent2);
        ownerSearchBox.setOnAction(e -> searchOwner());
        petSearchBox.setOnAction(e -> searchPet());
        refreshPetBtn.setOnAction(e -> refereshPet());
        setupTabHighlighting();

    }

    @FXML
    private void doAddOwner() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPetOwner.fxml"));
        Parent root = loader.load();
        AddPetOwner controller = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Doctor");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addOwnerBtn.getScene().getWindow());

        stage.showAndWait();


    }

    @FXML
    private void doAddPet() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editPet.fxml"));
        Parent root = loader.load();

        EditPetController controller = loader.getController();
        controller.setMode(EditPetController.Mode.ADD);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add Pet");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addPetBtn.getScene().getWindow());  // recommended

        stage.showAndWait();
        if(Reference.petView != null){
            Reference.petView.initialize();
        }
    }

    @FXML
    private void searchPet(){
        String id = petSearchBox.getText();
        PetDTO pet = pmodel.getOnePet(id);
        if(pet != null){
            Reference.petView.search(id);
        }else {
            new Alert(Alert.AlertType.WARNING, "No Data Found").show();
        }

    }

    @FXML
    private void searchOwner(){

        String id = ownerSearchBox.getText();
        PetOwnerDTO po =  pomodel.getPetOwner(id);
        if(po == null){
            new Alert(Alert.AlertType.WARNING, "No Data Found").show();
        }else {

            Reference.ownerView.search(id);


        }


    }

    @FXML
    private void refreshOwner(){
        Reference.ownerView.initialize();
        ownerSearchBox.clear();
    }

    @FXML
    private void refereshPet(){
        Reference.petView.initialize();
        petSearchBox.clear();
    }


    private void setupTabHighlighting() {
     for (Tab tab : tabPane.getTabs()) {
        tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                tab.setStyle(
                        "-fx-background-color: #1a1a1a; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-color: white; " +
                                "-fx-border-width: 0 0 3 0; " +
                                "-fx-background-radius: 10 10 0 0;"
                );
            } else {
                tab.setStyle(
                        "-fx-background-color: #0d0d0d; " +
                                "-fx-text-fill: #999999; " +
                                "-fx-background-radius: 10 10 0 0;"
                );
            }
        });
     }
    }

}
