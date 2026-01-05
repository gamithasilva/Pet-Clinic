package lk.ijse.petclinic.dto;

import java.time.LocalDate;

public class MedicalHistoryDTO {
    private Integer mediHisId;
    private Integer petId;
    private Integer appointmentId;
    private String medicalHistory;

    public MedicalHistoryDTO() {
    }

    public MedicalHistoryDTO(Integer petId, Integer appointmentId, String medicalHistory) {
        this.petId = petId;
        this.appointmentId = appointmentId;
        this.medicalHistory = medicalHistory;
    }

    // Getters and Setters
    public Integer getMediHisId() { return mediHisId; }
    public void setMediHisId(Integer mediHisId) { this.mediHisId = mediHisId; }

    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "MedicalHistoryDTO{" +
                "mediHisId=" + mediHisId +
                ", petId=" + petId +
                ", appointmentId=" + appointmentId +
                '}';
    }
}
