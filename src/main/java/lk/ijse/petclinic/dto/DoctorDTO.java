package lk.ijse.petclinic.dto;

public class DoctorDTO {
  private   int doctor_id;
    private int user_id;
    private double consultation_fee;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DoctorDTO(int doctor_id, int user_id, double consultation_fee, String name, String email, String phone, String status, String password) {
        this.doctor_id = doctor_id;
        this.user_id = user_id;
        this.consultation_fee = consultation_fee;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.password = password;
    }

    public DoctorDTO() {
    }
    public DoctorDTO(double consultation_fee,String name, String email, String phone,String password) {
        this.consultation_fee = consultation_fee;
        this.name = name;
        this.email = email;
        this.phone= phone;
        this.password = password;
    }
    public DoctorDTO(int doctor_id, int user_id, double consultation_fee, String name, String email, String phone, String status) {
        this.doctor_id = doctor_id;
        this.user_id = user_id;
        this.consultation_fee = consultation_fee;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getConsultation_fee() {
        return consultation_fee;
    }

    public void setConsultation_fee(double consultation_fee) {
        this.consultation_fee = consultation_fee;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
                "doctor_id=" + doctor_id +
                ", user_id=" + user_id +
                ", consultation_fee=" + consultation_fee +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
