package lk.ijse.petclinic.dto;

import lk.ijse.petclinic.model.Role;
import java.util.Date;

public class UserDTO {
   private String user_id;
   private String name;
   private String email;
   private String phone;
   private String password_hash;
   private String role;

   public UserDTO() {
   }

   public UserDTO(String user_id, String name, String email, String phone, String password_hash, String role) {
      this.user_id = user_id;
      this.name = name;
      this.email = email;
      this.phone = phone;
      this.password_hash = password_hash;
      this.role = role;
   }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
