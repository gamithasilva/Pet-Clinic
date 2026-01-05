package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.model.SupplierModel;
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

public class SupplierCard {

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label saddress;

    @FXML
    private Label semail;

    @FXML
    private Label sid;

    @FXML
    private Label sname;

    @FXML
    private Label sphone;

    private SupplierDTO supplierDTO;
    private SupplierModel model = new SupplierModel();

    @FXML
    void deleteSupplier(ActionEvent event) {

        String id = sid.getText();
        if(model.deleteSupplier(Integer.parseInt(id))){
            new Alert(Alert.AlertType.CONFIRMATION,"DELETE SUCCESSFULLY").showAndWait();;
            Reference.supplierView.initialize();
        } else {
            new Alert(Alert.AlertType.ERROR,"DELETE FAILED").showAndWait();
        }

    }

    @FXML
    void editSupplier(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addSupplier.fxml"));
        Parent root = loader.load();

        AddSupplier controller = loader.getController();
        controller.setData(supplierDTO, AddSupplier.MODE.EDIT);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Supplier");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(editBtn.getScene().getWindow());

        stage.showAndWait();
    }

    public void setData(SupplierDTO dto){
        if(supplierDTO==null){
            this.supplierDTO=dto;
        }

        sid.setText(String.valueOf(dto.getSupplierId()));
        sname.setText(dto.getName());
        sphone.setText(String.valueOf(dto.getContactNumber()));
        semail.setText(dto.getEmail());
        saddress.setText(dto.getAddress());

    }


}
