package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.model.SupplierModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SupplierView {

    @FXML
    private GridPane gridPane;
    private SupplierModel model = new SupplierModel();

    public void initialize(){
        if(Reference.supplierView == null){
            Reference.supplierView = this;
        }

        try {
            if(!gridPane.getChildren().isEmpty()){
                gridPane.getChildren().clear();
            }

            List<SupplierDTO> suppliers = model.getAllSuppliers();
            if(suppliers != null) {
                includeGridPane(suppliers);
            } else {
                new Alert(Alert.AlertType.ERROR,"There are no suppliers");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void includeGridPane(List<SupplierDTO> list){
        int row = 0;

        for(SupplierDTO cardData : list){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplierCard.fxml"));
                AnchorPane card = loader.load();
                SupplierCard controller = loader.getController();
                controller.setData(cardData);
                GridPane.setFillWidth(gridPane,true);
                GridPane.setHgrow(card, Priority.ALWAYS);
                card.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(card,0,row);
                row++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void search(String id){
        removeElement();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/supplierCard.fxml"));
            AnchorPane card = loader.load();
            SupplierCard controller = loader.getController();
            controller.setData(model.getSupplierById(Integer.parseInt(id)));
            GridPane.setFillWidth(gridPane,true);
            GridPane.setHgrow(card, Priority.ALWAYS);
            card.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(card,0,1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeElement(){
        try {
            gridPane.getChildren().clear();
        } catch (Exception e) {

            new Alert(Alert.AlertType.ERROR);
        }
    }
}
