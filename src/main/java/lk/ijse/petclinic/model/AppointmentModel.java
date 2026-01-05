package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.AppointmentDTO;
import lk.ijse.petclinic.dto.AppointmentMedicineDTO;
import lk.ijse.petclinic.dto.MedicalHistoryDTO;
import lk.ijse.petclinic.dto.PaymentDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Appointment operations
 */
public class AppointmentModel {

    /**
     * Save a new appointment
     */
    public boolean saveAppointment(AppointmentDTO appointmentDTO) {
        String sql = "INSERT INTO appointments (doctor_id, pet_id, appointment_datetime, consultation_fee, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    appointmentDTO.getDoctorId(),
                    appointmentDTO.getPetId(),
                    Timestamp.valueOf(appointmentDTO.getAppointmentDatetime()),
                    appointmentDTO.getConsultationFee(),
                    appointmentDTO.getStatus(),
                    appointmentDTO.getNotes()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all appointments with doctor, pet, and owner details
     */
    public List<AppointmentDTO> getAllAppointments() {
        String sql = "SELECT a.*, " +
                "u.name AS doctor_name, " +
                "p.name AS pet_name, " +
                "p.species AS pet_species, " +
                "po.name AS owner_name " +
                "FROM appointments a " +
                "INNER JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "INNER JOIN users u ON d.user_id = u.user_id " +
                "INNER JOIN pets p ON a.pet_id = p.pet_id " +
                "INNER JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "ORDER BY a.appointment_datetime DESC";

        List<AppointmentDTO> appointments = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("pet_id"),
                        rs.getTimestamp("appointment_datetime").toLocalDateTime(),
                        rs.getDouble("consultation_fee"),
                        rs.getString("status"),
                        rs.getString("notes")
                );
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("pet_species"));
                dto.setPetOwnerName(rs.getString("owner_name"));
                appointments.add(dto);
            }

            return appointments.isEmpty() ? null : appointments;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get appointment by ID with full details
     */
    public AppointmentDTO getAppointmentById(int appointmentId) {
        String sql = "SELECT a.*, " +
                "u.name AS doctor_name, " +
                "p.name AS pet_name, " +
                "p.species AS pet_species, " +
                "po.name AS owner_name " +
                "FROM appointments a " +
                "INNER JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "INNER JOIN users u ON d.user_id = u.user_id " +
                "INNER JOIN pets p ON a.pet_id = p.pet_id " +
                "INNER JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "WHERE a.appointment_id = ?";

        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            if (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("pet_id"),
                        rs.getTimestamp("appointment_datetime").toLocalDateTime(),
                        rs.getDouble("consultation_fee"),
                        rs.getString("status"),
                        rs.getString("notes")
                );
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("pet_species"));
                dto.setPetOwnerName(rs.getString("owner_name"));

                // Get medicines for this appointment
                AppointmentMedicineModel medicineModel = new AppointmentMedicineModel();
                dto.setMedicines(medicineModel.getAppointmentMedicines(appointmentId));

                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try {
            return Crudutil.execute(sql, status, appointmentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update appointment
     */
    public boolean updateAppointment(AppointmentDTO appointmentDTO) {
        String sql = "UPDATE appointments SET doctor_id = ?, pet_id = ?, appointment_datetime = ?, " +
                "consultation_fee = ?, status = ?, notes = ? WHERE appointment_id = ?";
        try {
            return Crudutil.execute(sql,
                    appointmentDTO.getDoctorId(),
                    appointmentDTO.getPetId(),
                    Timestamp.valueOf(appointmentDTO.getAppointmentDatetime()),
                    appointmentDTO.getConsultationFee(),
                    appointmentDTO.getStatus(),
                    appointmentDTO.getNotes(),
                    appointmentDTO.getAppointmentId()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete appointment
     */
    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try {
            return Crudutil.execute(sql, appointmentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Search appointments by keyword
     */
    public List<AppointmentDTO> searchAppointments(String keyword) {
        String sql = "SELECT a.*, " +
                "u.name AS doctor_name, " +
                "p.name AS pet_name, " +
                "p.species AS pet_species, " +
                "po.name AS owner_name " +
                "FROM appointments a " +
                "INNER JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "INNER JOIN users u ON d.user_id = u.user_id " +
                "INNER JOIN pets p ON a.pet_id = p.pet_id " +
                "INNER JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "WHERE p.name LIKE ? OR po.name LIKE ? OR a.status LIKE ? OR u.name LIKE ? " +
                "ORDER BY a.appointment_datetime DESC";

        String searchPattern = "%" + keyword + "%";
        List<AppointmentDTO> appointments = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, searchPattern, searchPattern, searchPattern, searchPattern);

            while (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("pet_id"),
                        rs.getTimestamp("appointment_datetime").toLocalDateTime(),
                        rs.getDouble("consultation_fee"),
                        rs.getString("status"),
                        rs.getString("notes")
                );
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("pet_species"));
                dto.setPetOwnerName(rs.getString("owner_name"));
                appointments.add(dto);
            }

            return appointments.isEmpty() ? null : appointments;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get appointments by status
     */
    public List<AppointmentDTO> getAppointmentsByStatus(String status) {
        String sql = "SELECT a.*, " +
                "u.name AS doctor_name, " +
                "p.name AS pet_name, " +
                "p.species AS pet_species, " +
                "po.name AS owner_name " +
                "FROM appointments a " +
                "INNER JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "INNER JOIN users u ON d.user_id = u.user_id " +
                "INNER JOIN pets p ON a.pet_id = p.pet_id " +
                "INNER JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "WHERE a.status = ? " +
                "ORDER BY a.appointment_datetime DESC";

        List<AppointmentDTO> appointments = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, status);

            while (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("doctor_id"),
                        rs.getInt("pet_id"),
                        rs.getTimestamp("appointment_datetime").toLocalDateTime(),
                        rs.getDouble("consultation_fee"),
                        rs.getString("status"),
                        rs.getString("notes")
                );
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("pet_species"));
                dto.setPetOwnerName(rs.getString("owner_name"));
                appointments.add(dto);
            }

            return appointments.isEmpty() ? null : appointments;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get completed appointments for invoice generation
     */
    public List<AppointmentDTO> getCompletedAppointments() {
        String sql = "SELECT a.*, p.name as pet_name, p.species, " +
                "po.name as owner_name, u.name as doctor_name " +
                "FROM appointments a " +
                "JOIN pets p ON a.pet_id = p.pet_id " +
                "JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "WHERE a.status = 'completed' " +
                "ORDER BY a.appointment_datetime DESC";

        List<AppointmentDTO> appointments = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO();
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setDoctorId(rs.getInt("doctor_id"));
                dto.setPetId(rs.getInt("pet_id"));
                dto.setAppointmentDatetime(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                dto.setConsultationFee(rs.getDouble("consultation_fee"));
                dto.setStatus(rs.getString("status"));
                dto.setNotes(rs.getString("notes"));

                // Display fields
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("species"));
                dto.setPetOwnerName(rs.getString("owner_name"));
                dto.setDoctorName(rs.getString("doctor_name"));

                appointments.add(dto);
            }

            return appointments.isEmpty() ? null : appointments;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get appointment by ID with full details including owner info
     */
    public AppointmentDTO getAppointmentByIdi(int appointmentId) {
        String sql = "SELECT a.*, p.name as pet_name, p.species, " +
                "po.name as owner_name, po.phone, po.email, po.address, " +
                "u.name as doctor_name, d.consultation_fee as specialization " +
                "FROM appointments a " +
                "JOIN pets p ON a.pet_id = p.pet_id " +
                "JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "WHERE a.appointment_id = ?";

        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            if (rs.next()) {
                AppointmentDTO dto = new AppointmentDTO();
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setDoctorId(rs.getInt("doctor_id"));
                dto.setPetId(rs.getInt("pet_id"));
                dto.setAppointmentDatetime(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                dto.setConsultationFee(rs.getDouble("consultation_fee"));
                dto.setStatus(rs.getString("status"));
                dto.setNotes(rs.getString("notes"));

                // Display fields
                dto.setPetName(rs.getString("pet_name"));
                dto.setPetSpecies(rs.getString("species"));
                dto.setPetOwnerName(rs.getString("owner_name"));
                dto.setDoctorName(rs.getString("doctor_name"));

                // Load medicines for this appointment
                dto.setMedicines(getAppointmentMedicines(appointmentId));

                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get medicines prescribed in an appointment
     */
    private List<AppointmentMedicineDTO> getAppointmentMedicines(int appointmentId) {
        String sql = "SELECT am.*, m.name as medicine_name, m.selling_price as unit_price " +
                "FROM appointment_medicines am " +
                "JOIN medicines m ON am.medicine_id = m.medicine_id " +
                "WHERE am.appointment_id = ?";

        List<AppointmentMedicineDTO> medicines = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            while (rs.next()) {
                AppointmentMedicineDTO dto = new AppointmentMedicineDTO();
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setMedicineId(rs.getInt("medicine_id"));
                dto.setMedicineName(rs.getString("medicine_name"));
                dto.setQuantity(rs.getInt("quantity"));
                dto.setUnitPrice(rs.getDouble("unit_price"));
                dto.calculateTotal();
                medicines.add(dto);
            }

            return medicines;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}