package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.PetDTO;
import lk.ijse.petclinic.util.Crudutil;
import lk.ijse.petclinic.util.Reference;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PetModel {

    public List<PetDTO> getAllPet(){
        String sql = "SELECT * FROM pets";
        List<PetDTO> pets = new ArrayList<>();
        try{
            ResultSet rs = Crudutil.execute(sql);

                while (rs.next()){
                    LocalDate date  = rs.getDate("date_of_birth").toLocalDate();
                   pets.add(new PetDTO(rs.getInt("pet_id"),rs.getInt("pet_owner_id"),rs.getString("name"),rs.getString("species"),rs.getString("breed"),rs.getString("gender"),date,rs.getString("imageURL")));

                }
                return pets.isEmpty() ? null : pets;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addPet(PetDTO dto){
        String sql = "INSERT INTO pets (pet_owner_id,name,species,breed,gender,date_of_birth,imageURL) VALUES (?,?,?,?,?,?,?)";
        try{
            return Crudutil.execute(sql,dto.getPetOwnerId(),dto.getName(),dto.getSpecies(),dto.getBreed(),dto.getGender(), Reference.dateConverter(dto.getDateOfBirth()),dto.getImageURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePet(PetDTO dto) {
        String sql = "UPDATE pets SET name = ?, species = ?, breed = ?, gender = ?, date_of_birth = ?, imageURL = ? WHERE pet_id = ?";
        try {
            return Crudutil.execute(sql,
                    dto.getName(),
                    dto.getSpecies(),
                    dto.getBreed(),
                    dto.getGender(),
                    Reference.dateConverter(dto.getDateOfBirth()),
                    dto.getImageURL(),
                    dto.getPetId()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletePet(int id){
        String sql ="DELETE FROM pets WHERE pet_id = ?";
        try{
            return Crudutil.execute(sql,id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PetDTO getOnePet(String id){
        String sql = "SELECT * FROM pets WHERE pet_id = ?";
        try {
            ResultSet rs = Crudutil.execute(sql,id);
            if(rs.next()){
                LocalDate date = rs.getDate("date_of_birth").toLocalDate();
                return new PetDTO(rs.getInt("pet_id"),rs.getInt("pet_owner_id"),rs.getString("name"),rs.getString("species"),rs.getString("breed"),rs.getString("gender"),date,rs.getString("imageURL"));
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
