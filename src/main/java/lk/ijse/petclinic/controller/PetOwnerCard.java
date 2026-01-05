package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

public class PetOwnerCard {
    @FXML
    private Label poad;

    @FXML
    private Label poemail;

    @FXML
    private Label poid;

    @FXML
    private Label poname;

    @FXML
    private Label pophone;

    private PetOwnerDTO dto;
    private PetOwnerModel model = new PetOwnerModel();

    public void setPOwnerData(PetOwnerDTO dto){
        this.dto = dto;
        poid.setText(String.valueOf(dto.getPet_owner_id()));
        poname.setText(dto.getName());
        poemail.setText(dto.getEmail());
        poad.setText(dto.getAddress());
        pophone.setText(dto.getPhone());

    }
    @FXML
    public void editOwner() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/editPetOwner.fxml"));
        Parent root = fxmlLoader.load();  // ← Controller is created and @FXML fields injected HERE

        EditPetOwner controller = fxmlLoader.getController();
        controller.setPetOwnerDTO(dto);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Pet Owner");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(poad.getScene().getWindow());  // recommended

        stage.showAndWait();

    }
    @FXML
    public void deleteOwner(){
        int id = this.dto.getPet_owner_id();
        if (model.deletePetOWner(id)){
            new Alert(Alert.AlertType.CONFIRMATION).showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        }
        Reference.ownerView.initialize();

    }
}
