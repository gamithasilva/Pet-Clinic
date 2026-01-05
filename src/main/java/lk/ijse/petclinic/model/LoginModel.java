package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.UserDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    public UserDTO login(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? ";
        ResultSet rs = Crudutil.execute(sql, email);
        if(rs.next()){
            return new UserDTO(rs.getString("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), rs.getString("password_hash"), rs.getString("role"));
        }
        return null;
    }
}
