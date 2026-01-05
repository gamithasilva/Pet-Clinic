package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lk.ijse.petclinic.Application;
import lk.ijse.petclinic.dto.UserDTO;
import lk.ijse.petclinic.model.LoginModel;



public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private final LoginModel userModel = new LoginModel();
    @FXML
    private void login(){
        String enterEmail = emailField.getText();
        String enterPassword = passwordField.getText();

        try{
            UserDTO userDTO = userModel.login(enterEmail);
            if(userDTO != null){
                String email = userDTO.getEmail();
                String password = userDTO.getPassword_hash();
                if(password.equals(enterPassword)){
                    new Alert(Alert.AlertType.CONFIRMATION,"Login Success").show();
                    Application.setRoot("/view/layout");
                }else{
                    new Alert(Alert.AlertType.ERROR,"Password is wrong").show();
                }

            }else{
                new Alert(Alert.AlertType.ERROR,"User not found").show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
