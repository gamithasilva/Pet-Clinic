package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetOwnerModel;

public class EditPetOwner {

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
    PetOwnerDTO dto;

    public void setPetOwnerDTO(PetOwnerDTO dto){
        this.dto = dto;

        if(dto != null){
            ownerName.setText(dto.getName());
            ownerAddress.setText(dto.getAddress());
            ownerPhone.setText(dto.getPhone());
            ownerEmail.setText(dto.getEmail());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void saveOwner(ActionEvent event) {
        if(model.updatePetOwner(dto)){
            new Alert(Alert.AlertType.CONFIRMATION, "Saved").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR,"something went wrong").show();
        }
        closeWindow();;
    }

    private void closeWindow() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

}
