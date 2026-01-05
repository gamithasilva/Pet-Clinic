package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.model.PetOwnerModel;
import lk.ijse.petclinic.util.Reference;

import java.util.List;

public class OwnerViewController {

    @FXML
    private GridPane gridPane;

    private PetOwnerModel model = new PetOwnerModel();

    public void initialize(){

        Reference.ownerView = this;

        if(!gridPane.getChildren().isEmpty()){
            gridPane.getChildren().clear();
        }
        List<PetOwnerDTO> list = model.getALlPetOwner();
        if(list != null){
            loadAllPetOwner(list);
        } else {
            new Alert(Alert.AlertType.ERROR, "there is no pet owners").showAndWait();
        }


    }

    public void loadAllPetOwner(List<PetOwnerDTO> list){
        int row = 0;
        for (PetOwnerDTO dto : list){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/petOwnerCard.fxml"));
                AnchorPane card = loader.load();

                PetOwnerCard controller = loader.getController();
                controller.setPOwnerData(dto);
                GridPane.setRowIndex(card, row);
                GridPane.setColumnIndex(card, 0);
                gridPane.add(card, 0, row);
                row++;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeElement() {
        try {
            gridPane.getChildren().clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR);
        }
    }


    public void search(String id ){

        try {
            removeElement();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/petOwnerCard.fxml"));
            AnchorPane card = loader.load();
            int row = 1;
            PetOwnerCard cardController = loader.getController();
            cardController.setPOwnerData(model.getPetOwner(id));
            GridPane.setFillWidth(card, true);
            GridPane.setHgrow(card, Priority.ALWAYS);
            card.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(card,0,row);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
