package lk.ijse.petclinic.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    private Integer paymentId;
    private Integer appointmentId;
    private Double amount;
    private LocalDateTime paymentDate;
    private String status;

    public PaymentDTO() {
    }

    public PaymentDTO(Integer appointmentId, Double amount, String status) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public Integer getPaymentId() { return paymentId; }
    public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "paymentId=" + paymentId +
                ", appointmentId=" + appointmentId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
