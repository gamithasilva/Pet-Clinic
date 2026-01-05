package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.AppointmentMedicineDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentMedicineModel {
    /**
     * Add medicine to appointment
     */
    public boolean addMedicineToAppointment(AppointmentMedicineDTO medicineDTO) {
        String sql = "INSERT INTO appointment_medicines (appointment_id, medicine_id, quantity) VALUES (?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    medicineDTO.getAppointmentId(),
                    medicineDTO.getMedicineId(),
                    medicineDTO.getQuantity()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all medicines for an appointment
     */
    public List<AppointmentMedicineDTO> getAppointmentMedicines(int appointmentId) {
        String sql = "SELECT am.*, m.name AS medicine_name, m.generic_name, m.selling_price " +
                "FROM appointment_medicines am " +
                "INNER JOIN medicines m ON am.medicine_id = m.medicine_id " +
                "WHERE am.appointment_id = ?";

        List<AppointmentMedicineDTO> medicines = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            while (rs.next()) {
                AppointmentMedicineDTO dto = new AppointmentMedicineDTO(
                        rs.getInt("appointment_id"),
                        rs.getInt("medicine_id"),
                        rs.getInt("quantity")
                );
                dto.setMedicineName(rs.getString("medicine_name"));
                dto.setGenericName(rs.getString("generic_name"));
                dto.setSellingPrice(rs.getDouble("selling_price"));
                dto.setSubtotal(rs.getInt("quantity") * rs.getDouble("selling_price"));
                medicines.add(dto);
            }

            return medicines.isEmpty() ? null : medicines;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove medicine from appointment
     */
    public boolean removeMedicineFromAppointment(int appointmentId, int medicineId) {
        String sql = "DELETE FROM appointment_medicines WHERE appointment_id = ? AND medicine_id = ?";
        try {
            return Crudutil.execute(sql, appointmentId, medicineId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update medicine stock after appointment completion
     */
    public boolean updateMedicineStock(int medicineId, int quantity) {
        String sql = "UPDATE medicines SET current_stock = current_stock - ? WHERE medicine_id = ?";
        try {
            return Crudutil.execute(sql, quantity, medicineId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
