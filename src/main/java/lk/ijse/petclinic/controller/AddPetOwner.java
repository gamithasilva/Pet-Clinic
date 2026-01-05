package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

public class AddPetOwner {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField ownerAddress;

    @FXML
    private TextField ownerEmail;

    @FXML
    private TextField ownerName;

    @FXML
    private TextField ownerPhone;

    @FXML
    private Button saveBtn;

    PetOwnerModel model = new PetOwnerModel();

    @FXML
    void cancel(ActionEvent event) {

        closeWindow();

    }

    @FXML
    void savePetOwner(ActionEvent event) {

        if(ownerName.getText().trim().isEmpty()||ownerAddress.getText().trim().isEmpty()||ownerPhone.getText().trim().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please fill all fields").showAndWait();
            return;
        }
        if(ownerPhone.getText().trim().length() != 10){
            new Alert(Alert.AlertType.ERROR,"Phone number must be 10 digits").showAndWait();
            return;
        }
        if(!ownerEmail.getText().contains("@")){
            new Alert(Alert.AlertType.ERROR,"Invalid email").showAndWait();
            return;
        }

        PetOwnerDTO dto = new PetOwnerDTO(ownerName.getText(), ownerAddress.getText(), ownerPhone.getText(), ownerEmail.getText());

        if(model.addPetOwner(dto)){
            new Alert(Alert.AlertType.CONFIRMATION).showAndWait();
            Reference.ownerView.initialize();
        }else {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        }
        Reference.ownerView.initialize();
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

}
