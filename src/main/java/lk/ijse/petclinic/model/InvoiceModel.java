package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.AppointmentMedicineDTO;
import lk.ijse.petclinic.dto.InvoiceDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceModel {

    /**
     * Create new invoice
     */
    public boolean createInvoice(InvoiceDTO invoiceDTO) {
        String sql = "INSERT INTO invoices (appointment_id, payment_id, invoice_number, " +
                "consultation_fee, medicine_total, subtotal, discount, total_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    invoiceDTO.getAppointmentId(),
                    invoiceDTO.getPaymentId(),
                    invoiceDTO.getInvoiceNumber(),
                    invoiceDTO.getConsultationFee(),
                    invoiceDTO.getMedicineTotal(),
                    invoiceDTO.getSubtotal(),
                    invoiceDTO.getDiscount(),
                    invoiceDTO.getTotalAmount()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all invoices with complete details
     */
    public List<InvoiceDTO> getAllInvoices() {
        String sql = "SELECT i.*, p.name as pet_name, p.species, " +
                "po.name as owner_name, u.name as doctor_name, " +
                "a.appointment_datetime, pay.status as payment_status " +
                "FROM invoices i " +
                "JOIN appointments a ON i.appointment_id = a.appointment_id " +
                "JOIN pets p ON a.pet_id = p.pet_id " +
                "JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN payments pay ON i.payment_id = pay.payment_id " +
                "ORDER BY i.invoice_date DESC";

        List<InvoiceDTO> invoices = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(rs.getInt("invoice_id"));
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setInvoiceNumber(rs.getString("invoice_number"));
                dto.setInvoiceDate(rs.getTimestamp("invoice_date").toLocalDateTime());
                dto.setConsultationFee(rs.getDouble("consultation_fee"));
                dto.setMedicineTotal(rs.getDouble("medicine_total"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                dto.setDiscount(rs.getDouble("discount"));
                dto.setTotalAmount(rs.getDouble("total_amount"));

                // Display fields
                dto.setPetName(rs.getString("pet_name"));
                dto.setOwnerName(rs.getString("owner_name"));
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setAppointmentDate(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                dto.setPaymentStatus(rs.getString("payment_status"));

                invoices.add(dto);
            }

            return invoices.isEmpty() ? null : invoices;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get invoice by ID with full details
     */
    public InvoiceDTO getInvoiceById(int invoiceId) {
        String sql = "SELECT i.*, p.name as pet_name, p.species, " +
                "po.name as owner_name, po.phone as owner_phone, po.email as owner_email, po.address as owner_address, " +
                "u.name as doctor_name, d.consultation_fee, " +
                "a.appointment_datetime, a.notes, pay.status as payment_status " +
                "FROM invoices i " +
                "JOIN appointments a ON i.appointment_id = a.appointment_id " +
                "JOIN pets p ON a.pet_id = p.pet_id " +
                "JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN payments pay ON i.payment_id = pay.payment_id " +
                "WHERE i.invoice_id = ?";

        try {
            ResultSet rs = Crudutil.execute(sql, invoiceId);

            if (rs.next()) {
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(rs.getInt("invoice_id"));
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setInvoiceNumber(rs.getString("invoice_number"));
                dto.setInvoiceDate(rs.getTimestamp("invoice_date").toLocalDateTime());
                dto.setConsultationFee(rs.getDouble("consultation_fee"));
                dto.setMedicineTotal(rs.getDouble("medicine_total"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                dto.setDiscount(rs.getDouble("discount"));
                dto.setTotalAmount(rs.getDouble("total_amount"));

                dto.setPetName(rs.getString("pet_name"));
                dto.setOwnerName(rs.getString("owner_name"));
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setAppointmentDate(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                dto.setPaymentStatus(rs.getString("payment_status"));

                // Load medicines for this appointment
                dto.setMedicines(getAppointmentMedicines(dto.getAppointmentId()));

                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get medicines for an appointment
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

    /**
     * Search invoices by keyword
     */
    public List<InvoiceDTO> searchInvoices(String keyword) {
        String sql = "SELECT i.*, p.name as pet_name, po.name as owner_name, u.name as doctor_name, " +
                "a.appointment_datetime, pay.status as payment_status " +
                "FROM invoices i " +
                "JOIN appointments a ON i.appointment_id = a.appointment_id " +
                "JOIN pets p ON a.pet_id = p.pet_id " +
                "JOIN pet_owners po ON p.pet_owner_id = po.pet_owner_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "JOIN payments pay ON i.payment_id = pay.payment_id " +
                "WHERE i.invoice_number LIKE ? OR po.name LIKE ? OR p.name LIKE ? " +
                "ORDER BY i.invoice_date DESC";

        List<InvoiceDTO> invoices = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";

        try {
            ResultSet rs = Crudutil.execute(sql, searchPattern, searchPattern, searchPattern);

            while (rs.next()) {
                InvoiceDTO dto = new InvoiceDTO();
                dto.setInvoiceId(rs.getInt("invoice_id"));
                dto.setAppointmentId(rs.getInt("appointment_id"));
                dto.setPaymentId(rs.getInt("payment_id"));
                dto.setInvoiceNumber(rs.getString("invoice_number"));
                dto.setInvoiceDate(rs.getTimestamp("invoice_date").toLocalDateTime());
                dto.setConsultationFee(rs.getDouble("consultation_fee"));
                dto.setMedicineTotal(rs.getDouble("medicine_total"));
                dto.setSubtotal(rs.getDouble("subtotal"));
                dto.setDiscount(rs.getDouble("discount"));
                dto.setTotalAmount(rs.getDouble("total_amount"));

                dto.setPetName(rs.getString("pet_name"));
                dto.setOwnerName(rs.getString("owner_name"));
                dto.setDoctorName(rs.getString("doctor_name"));
                dto.setAppointmentDate(rs.getTimestamp("appointment_datetime").toLocalDateTime());
                dto.setPaymentStatus(rs.getString("payment_status"));

                invoices.add(dto);
            }

            return invoices.isEmpty() ? null : invoices;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate unique invoice number
     */
    public String generateInvoiceNumber() {
        String sql = "SELECT MAX(invoice_id) as max_id FROM invoices";
        try {
            ResultSet rs = Crudutil.execute(sql);
            if (rs.next()) {
                int nextId = rs.getInt("max_id") + 1;
                return String.format("INV%05d", nextId);
            }
            return "INV00001";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if invoice exists for appointment
     */
    public boolean invoiceExistsForAppointment(int appointmentId) {
        String sql = "SELECT COUNT(*) as count FROM invoices WHERE appointment_id = ?";
        try {
            ResultSet rs = Crudutil.execute(sql, appointmentId);
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}