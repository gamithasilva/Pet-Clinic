package lk.ijse.petclinic.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDTO {
    private Integer invoiceId;
    private Integer appointmentId;
    private Integer paymentId;
    private LocalDateTime invoiceDate;
    private Double consultationFee;
    private Double medicineTotal;
    private Double subtotal;
    private Double discount;
    private Double totalAmount;
    private String invoiceNumber;

    // Display fields from joined tables
    private String petName;
    private String ownerName;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private String paymentStatus;
    private List<AppointmentMedicineDTO> medicines;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Integer appointmentId, Integer paymentId, Double consultationFee,
                      Double medicineTotal, Double discount) {
        this.appointmentId = appointmentId;
        this.paymentId = paymentId;
        this.consultationFee = consultationFee;
        this.medicineTotal = medicineTotal;
        this.discount = discount;
        calculateTotals();
    }

    public void calculateTotals() {
        this.subtotal = (consultationFee != null ? consultationFee : 0.0) +
                (medicineTotal != null ? medicineTotal : 0.0);
        this.totalAmount = subtotal - (discount != null ? discount : 0.0);
    }

    // Getters and Setters
    public Integer getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Integer invoiceId) { this.invoiceId = invoiceId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }

    public Double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
        calculateTotals();
    }

    public Double getMedicineTotal() { return medicineTotal; }
    public void setMedicineTotal(Double medicineTotal) {
        this.medicineTotal = medicineTotal;
        calculateTotals();
    }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) {
        this.discount = discount;
        calculateTotals();
    }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public List<AppointmentMedicineDTO> getMedicines() { return medicines; }
    public void setMedicines(List<AppointmentMedicineDTO> medicines) {
        this.medicines = medicines;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "invoiceId=" + invoiceId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", appointmentId=" + appointmentId +
                ", totalAmount=" + totalAmount +
                '}';
    }
}