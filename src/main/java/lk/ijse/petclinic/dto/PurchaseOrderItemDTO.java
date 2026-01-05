package lk.ijse.petclinic.dto;

public class PurchaseOrderItemDTO {

    private Integer orderId;
    private Integer medicineId;
    private Integer quantity;
    private Double unitBuyingPrice;
    private Double subtotal;

    // For display purposes
    private String medicineName;
    private String genericName;

    public PurchaseOrderItemDTO() {
    }

    public PurchaseOrderItemDTO(Integer orderId, Integer medicineId, Integer quantity,
                                Double unitBuyingPrice) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitBuyingPrice = unitBuyingPrice;
        this.subtotal = quantity * unitBuyingPrice;
    }

    public PurchaseOrderItemDTO(Integer medicineId, Integer quantity, Double unitBuyingPrice) {
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitBuyingPrice = unitBuyingPrice;
        this.subtotal = quantity * unitBuyingPrice;
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        updateSubtotal();
    }

    public Double getUnitBuyingPrice() {
        return unitBuyingPrice;
    }

    public void setUnitBuyingPrice(Double unitBuyingPrice) {
        this.unitBuyingPrice = unitBuyingPrice;
        updateSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    private void updateSubtotal() {
        if (quantity != null && unitBuyingPrice != null) {
            this.subtotal = quantity * unitBuyingPrice;
        }
    }

    @Override
    public String toString() {
        return "PurchaseOrderItemDTO{" +
                "orderId=" + orderId +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", unitBuyingPrice=" + unitBuyingPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
