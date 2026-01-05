package lk.ijse.petclinic.dto;

import java.time.LocalDate;
import java.util.List;

public class PetDTO {
    private int petId;
    private int petOwnerId;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;

    private List<MedicalHistoryDTO> medicalHistoryDTO;

    public List<MedicalHistoryDTO> getMedicalHistory() {
        return medicalHistoryDTO;
    }

    public void setMedicalHistory(List<MedicalHistoryDTO> medicalHistoryDTO) {
        this.medicalHistoryDTO = medicalHistoryDTO;
    }

    private String imageURL;

    // Constructors
    public PetDTO() {}

    public PetDTO(int petId, int petOwnerId, String name, String species, String breed,
               String gender, LocalDate dateOfBirth, String imageURL) {
        this.petId = petId;
        this.petOwnerId = petOwnerId;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;

        this.imageURL = imageURL;
    }

    // Getters and Setters
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getPetOwnerId() {
        return petOwnerId;
    }

    public void setPetOwnerId(int petOwnerId) {
        this.petOwnerId = petOwnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }



    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
