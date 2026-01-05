package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.petclinic.dto.DoctorDTO;
import lk.ijse.petclinic.model.DoctorModel;
import lk.ijse.petclinic.util.Reference;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


public class DoctorController {
    @FXML
    BorderPane comb;
    @FXML
    private TextField searchbox;
    @FXML
    private Button add;
    @FXML
    private Button refresh;

    private  DoctorModel dt = new DoctorModel();
    public void initialize() throws IOException {

        searchbox.setOnAction(e -> search());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/listView.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        comb.setCenter(anchorPane);

    }
    @FXML
    public void refresh() {
        Reference.listview.initialize();
        searchbox.clear();
    }
    @FXML
    public void search() {
        String id = searchbox.getText();
        if(id != null) {
            Reference.listview.search(id);
        } else {
            new Alert(Alert.AlertType.ERROR,"there is no doctor using doctor id : " + id );
        }
    }
    @FXML
    public void addDoctor() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addDoctor.fxml"));
        Parent root = fxmlLoader.load();


        AddDoctorController controller = new AddDoctorController();


        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Doctor");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(add.getScene().getWindow());

        stage.showAndWait();
    }





}
