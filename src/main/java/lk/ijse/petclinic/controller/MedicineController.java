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
import lk.ijse.petclinic.util.Reference;

import java.io.IOException;

public class MedicineController {

    @FXML
    private Button addMedicineBtn;

    @FXML
    private Button addOrderBtn;

    @FXML
    private Button addSupplyBtn;

    @FXML
    private BorderPane medBpane;

    @FXML
    private AnchorPane medicineContentPane;

    @FXML
    private TextField medicineSearchBox;

    @FXML
    private BorderPane orderBpane;

    @FXML
    private AnchorPane orderContentPane;

    @FXML
    private TextField orderSearchBox;

    @FXML
    private Button refreshMedicineBtn;

    @FXML
    private Button refreshOrderBtn;

    @FXML
    private Button refreshSupplyBtn;

    @FXML
    private BorderPane suppBpane;

    @FXML
    private AnchorPane supplyContentPane;

    @FXML
    private TextField supplySearchBox;

    @FXML
    public void initialize() throws IOException {

        FXMLLoader supplierLoader = new FXMLLoader(getClass().getResource("/view/supplierView.fxml"));
        Parent supplierRoot = supplierLoader.load();
        suppBpane.setCenter(supplierRoot);

        FXMLLoader medicineLoader = new FXMLLoader(getClass().getResource("/view/medicineView.fxml"));
        Parent medicineRoot = medicineLoader.load();
        medBpane.setCenter(medicineRoot);


        try {
            FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("/view/purchaseOrderListView.fxml"));
            AnchorPane purchaseOrderListView = orderLoader.load();


            AnchorPane.setTopAnchor(purchaseOrderListView, 0.0);
            AnchorPane.setBottomAnchor(purchaseOrderListView, 0.0);
            AnchorPane.setLeftAnchor(purchaseOrderListView, 0.0);
            AnchorPane.setRightAnchor(purchaseOrderListView, 0.0);

            orderContentPane.getChildren().add(purchaseOrderListView);


            orderSearchBox.setOnAction(e -> searchOrders());


            addOrderBtn.setOnAction(e -> addOrder());
            refreshOrderBtn.setOnAction(e -> refreshOrders());

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading Purchase Order view: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }



    @FXML
    private void addSupplier() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addSupplier.fxml"));
        Parent root = loader.load();

        AddSupplier controller = loader.getController();
        controller.setMode(AddSupplier.MODE.ADD);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add Supplier");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addSupplyBtn.getScene().getWindow());

        stage.showAndWait();
        Reference.supplierView.initialize();
    }

    @FXML
    private void searchSupplier() {
        Reference.supplierView.search(supplySearchBox.getText());
    }

    @FXML
    private void setRefreshSupplyBtn() {
        Reference.supplierView.initialize();
        supplySearchBox.clear();
    }



    @FXML
    private void searchOrders() {
        String keyword = orderSearchBox.getText();
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (Reference.purchaseOrderListView != null) {
                Reference.purchaseOrderListView.search(keyword);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a search term").showAndWait();
        }
    }

    @FXML
    private void addOrder() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/purchaseOrderForm.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("New Purchase Order");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(addOrderBtn.getScene().getWindow());

            stage.showAndWait();

            // Refresh list after adding
            if (Reference.purchaseOrderListView != null) {
                Reference.purchaseOrderListView.initialize();
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error opening add order form: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshOrders() {
        if (Reference.purchaseOrderListView != null) {
            Reference.purchaseOrderListView.initialize();
            orderSearchBox.clear();
        }
    }

    // ==================== MEDICINE METHODS (TODO) ====================

    @FXML
    private void searchMedicine() {
        String id = medicineSearchBox.getText();
        Reference.medicineView.searchById(id);
    }

    @FXML
    private void addMedicine() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addMedicine.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("New Medicine");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(addMedicineBtn.getScene().getWindow());
        stage.showAndWait();

        if(Reference.medicineView != null){
            Reference.medicineView.initialize();
        }


    }

    @FXML
    private void refreshMedicine() {
        // TODO: Implement refresh medicine
    }
}