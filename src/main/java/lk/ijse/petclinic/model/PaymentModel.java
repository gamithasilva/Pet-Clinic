package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.PaymentDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentModel {

    /**
     * Create payment for appointment
     */
    public boolean createPayment(PaymentDTO paymentDTO) {
        String sql = "INSERT INTO payments (appointment_id, amount, status) VALUES (?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    paymentDTO.getAppointmentId(),
                    paymentDTO.getAmount(),
                    paymentDTO.getStatus()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get payment by appointment
     */
    public PaymentDTO getPaymentByAppointment(int appointmentId) {
        String sql = "SELECT * FROM payments WHERE appointment_id = ?";
        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);

            if (rs.next()) {
                PaymentDTO dto = new PaymentDTO(
                        rs.getInt("appointment_id"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                );
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
        try {
            return Crudutil.execute(sql, status, paymentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all completed payments
     */
    public List<PaymentDTO> getAllCompletedPayments() {
        String sql = "SELECT * FROM payments WHERE status = 'completed' ORDER BY payment_date DESC";
        List<PaymentDTO> payments = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                PaymentDTO dto = new PaymentDTO(
                        rs.getInt("appointment_id"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                );
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                payments.add(dto);
            }

            return payments.isEmpty() ? null : payments;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
