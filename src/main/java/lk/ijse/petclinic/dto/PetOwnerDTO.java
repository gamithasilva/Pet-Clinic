package lk.ijse.petclinic.dto;

public class PetOwnerDTO {
    private int pet_owner_id ;
    private String name;
    private String address;
    private String phone;
    private String email;


    public PetOwnerDTO(int pet_owner_id, String name, String address, String phone, String email) {
        this.pet_owner_id = pet_owner_id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public PetOwnerDTO() {
    }

    public PetOwnerDTO(String name, String address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public int getPet_owner_id() {
        return pet_owner_id;
    }

    public void setPet_owner_id(int pet_owner_id) {
        this.pet_owner_id = pet_owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
