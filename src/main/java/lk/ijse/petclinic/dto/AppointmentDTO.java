package lk.ijse.petclinic.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentDTO {
    private Integer appointmentId;
    private Integer doctorId;
    private Integer petId;
    private LocalDateTime appointmentDatetime;
    private Double consultationFee;
    private String status;
    private String notes;

    // For display purposes
    private String doctorName;
    private String petName;
    private String petOwnerName;
    private String petSpecies;
    private List<AppointmentMedicineDTO> medicines;



    public AppointmentDTO() {
    }

    public AppointmentDTO(Integer appointmentId, Integer doctorId, Integer petId,
                          LocalDateTime appointmentDatetime, Double consultationFee,
                          String status, String notes) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.petId = petId;
        this.appointmentDatetime = appointmentDatetime;
        this.consultationFee = consultationFee;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }

    public LocalDateTime getAppointmentDatetime() { return appointmentDatetime; }
    public void setAppointmentDatetime(LocalDateTime appointmentDatetime) {
        this.appointmentDatetime = appointmentDatetime;
    }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPetOwnerName() { return petOwnerName; }
    public void setPetOwnerName(String petOwnerName) { this.petOwnerName = petOwnerName; }

    public String getPetSpecies() { return petSpecies; }
    public void setPetSpecies(String petSpecies) { this.petSpecies = petSpecies; }

    public List<AppointmentMedicineDTO> getMedicines() { return medicines; }
    public void setMedicines(List<AppointmentMedicineDTO> medicines) {
        this.medicines = medicines;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "appointmentId=" + appointmentId +
                ", doctorName='" + doctorName + '\'' +
                ", petName='" + petName + '\'' +
                ", appointmentDatetime=" + appointmentDatetime +
                ", status='" + status + '\'' +
                '}';
    }
}
