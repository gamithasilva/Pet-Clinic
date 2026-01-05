package lk.ijse.petclinic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.model.SupplierModel;

public class AddSupplier {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField supplierAddress;

    @FXML
    private TextField supplierEmail;

    @FXML
    private TextField supplierName;

    @FXML
    private TextField supplierPhone;

    private SupplierModel model = new SupplierModel();
    private SupplierDTO dto;
    private MODE currentMode;

    public enum MODE{
        ADD,EDIT
    }

    public void setData(SupplierDTO dto,MODE mode) {
        this.currentMode = mode;
        this.dto = dto;

        if(mode ==  MODE.EDIT){
            loadData();
        }
    }

    public void setMode(MODE mode){
        this.currentMode = mode;
    }

    public void initialize(){

        if(currentMode == MODE.EDIT){

        }

    }


    @FXML
    void cancel(ActionEvent event) {
        close();

    }

    @FXML
    void saveSupplier(ActionEvent event) {
        if(currentMode == MODE.ADD){
            addSupplier();
        }else {
            update();
        }

        close();

    }

    public void addSupplier(){

        dto = new SupplierDTO();
        dto.setName(supplierName.getText());
        dto.setContactNumber(supplierPhone.getText());
        dto.setEmail(supplierEmail.getText());
        dto.setAddress(supplierAddress.getText());

        if (model.saveSupplier(dto)){
            new Alert(Alert.AlertType.CONFIRMATION,"Saved Successfully").showAndWait();


        } else {
            new Alert(Alert.AlertType.ERROR,"Save Failed").showAndWait();

        }

    }

    public void update(){


        dto.setName(supplierName.getText());
        dto.setContactNumber(supplierPhone.getText());
        dto.setEmail(supplierEmail.getText());
        dto.setAddress(supplierAddress.getText());

        if(model.updateSupplier(dto)){
            new Alert(Alert.AlertType.INFORMATION,"Updated Successfully").showAndWait();

        } else {
            new Alert(Alert.AlertType.ERROR,"Update Failed").showAndWait();

        }
    }
    public void close(){
        Stage stage = (Stage)  saveBtn.getScene().getWindow();
        stage.close();
    }

    public void loadData(){
        if(dto == null){return;}
        supplierName.setText(dto.getName());
        supplierPhone.setText(dto.getContactNumber());
        supplierEmail.setText(dto.getEmail());
        supplierAddress.setText(dto.getAddress());
    }
}
