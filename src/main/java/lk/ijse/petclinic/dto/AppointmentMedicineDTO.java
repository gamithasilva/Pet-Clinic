package lk.ijse.petclinic.dto;

public class AppointmentMedicineDTO {
    private Integer appointmentId;
    private Integer medicineId;
    private Integer quantity;

    // For display
    private String medicineName;
    private String genericName;
    private Double sellingPrice;
    private Double subtotal;

    private Double unitPrice;
    private Double total;

    public AppointmentMedicineDTO() {
    }

    public AppointmentMedicineDTO(Integer appointmentId, Integer medicineId, Integer quantity) {
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.quantity = quantity;
    }
    public void calculateTotal() {
        if (quantity != null && unitPrice != null) {
            this.total = quantity * unitPrice;
        }
    }
    // Getters and Setters
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        updateSubtotal();
    }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }

    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
        updateSubtotal();
    }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    private void updateSubtotal() {
        if (quantity != null && sellingPrice != null) {
            this.subtotal = quantity * sellingPrice;
        }
    }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    @Override
    public String toString() {
        return "AppointmentMedicineDTO{" +
                "appointmentId=" + appointmentId +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}

