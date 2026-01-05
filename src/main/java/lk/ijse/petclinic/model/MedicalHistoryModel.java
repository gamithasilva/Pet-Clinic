package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.MedicalHistoryDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryModel {

    /**
     * Add medical history
     */
    public boolean addMedicalHistory(MedicalHistoryDTO historyDTO) {
        String sql = "INSERT INTO medical_history (pet_id, appointment_id, medical_history) VALUES (?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    historyDTO.getPetId(),
                    historyDTO.getAppointmentId(),
                    historyDTO.getMedicalHistory()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get medical history by appointment
     */
    public MedicalHistoryDTO getMedicalHistoryByAppointment(int appointmentId) {
        String sql = "SELECT * FROM medical_history WHERE appointment_id = ?";
        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            if (rs.next()) {
                MedicalHistoryDTO dto = new MedicalHistoryDTO(
                        rs.getInt("pet_id"),
                        rs.getInt("appointment_id"),
                        rs.getString("medical_history")
                );
                dto.setMediHisId(rs.getInt("medi_his_id"));
                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all medical history for a pet
     */
    public List<MedicalHistoryDTO> getPetMedicalHistory(int petId) {
        String sql = "SELECT * FROM medical_history WHERE pet_id = ? ORDER BY medi_his_id DESC";
        List<MedicalHistoryDTO> historyList = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, petId);

            while (rs.next()) {
                MedicalHistoryDTO dto = new MedicalHistoryDTO(
                        rs.getInt("pet_id"),
                        rs.getInt("appointment_id"),
                        rs.getString("medical_history")
                );
                dto.setMediHisId(rs.getInt("medi_his_id"));
                historyList.add(dto);
            }

            return historyList.isEmpty() ? null : historyList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update medical history
     */
    public boolean updateMedicalHistory(MedicalHistoryDTO historyDTO) {
        String sql = "UPDATE medical_history SET medical_history = ? WHERE medi_his_id = ?";
        try {
            return Crudutil.execute(sql,
                    historyDTO.getMedicalHistory(),
                    historyDTO.getMediHisId()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

