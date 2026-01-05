package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.List;

public class ListViewController {



    @FXML
    private GridPane gridPane;
    private DoctorModel doctorModel = new DoctorModel();
    public void initialize() {
        Reference.listview = this;
        try {
            if(!gridPane.getChildren().isEmpty()){
                gridPane.getChildren().clear();
            }
            List<DoctorDTO> ds = new DoctorModel().getAllDoctor();
            includeInGridPane(ds);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void includeInGridPane(List<DoctorDTO> cardDataList) {
        int row = 0;

        for (DoctorDTO cardData : cardDataList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctorCard.fxml"));
                AnchorPane card = loader.load();

                DoctorCardController cardController = loader.getController();
                cardController.setDoctorDTO(cardData);
                GridPane.setFillWidth(card,true );
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(card, 0, row);


                row++;
            } catch (IOException e) {
                new Alert(Alert.AlertType.CONFIRMATION).showAndWait();
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
    public void search(String id){

        try {
            removeElement();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/doctorCard.fxml"));
            AnchorPane card = loader.load();
            int row = 1;
            DoctorCardController cardController = loader.getController();
            cardController.setDoctorDTO(doctorModel.getOneDoctor(id));
            GridPane.setFillWidth(card, true);
            GridPane.setHgrow(card, Priority.ALWAYS);
            card.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(card, 0, row);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
