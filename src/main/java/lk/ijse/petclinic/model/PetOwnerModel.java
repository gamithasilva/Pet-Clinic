package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.PetOwnerDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetOwnerModel {
    public boolean addPetOwner(PetOwnerDTO dto){
    String sql = "INSERT INTO pet_owners ( address, email,phone,name) VALUES (?, ?, ?, ?)";
    try{
        return Crudutil.execute(sql,dto.getAddress(),dto.getEmail(),dto.getPhone(),dto.getName());
    }catch (SQLException e){
        throw new RuntimeException(e);
    }

    }

    public List<PetOwnerDTO> getALlPetOwner(){
        String sql = "SELECT * FROM pet_owners";
        try{
            ResultSet rs =  Crudutil.execute(sql);
            List<PetOwnerDTO> list = new ArrayList<>();

            if (rs != null) {
                while (rs.next()) {
                    list.add(new PetOwnerDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                }
                return list;
            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean deletePetOWner(int id ){
        String sql = "DELETE FROM pet_owners WHERE pet_owner_id = ?";
        try{
            return Crudutil.execute(sql,id);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public PetOwnerDTO getPetOwner(String id){
        String sql = "SELECT * FROM pet_owners WHERE pet_owner_id = ?";
        try{
            ResultSet rs = Crudutil.execute(sql,id);
            if (rs.next()){
                return new PetOwnerDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePetOwner(PetOwnerDTO dto){
        String sql = "UPDATE pet_owners SET name = ?, address = ?, phone = ?, email = ? WHERE pet_owner_id = ?";
        try{
           return Crudutil.execute(sql,dto.getName(),dto.getAddress(),dto.getPhone(),dto.getEmail(),dto.getPet_owner_id());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
