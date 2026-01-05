package lk.ijse.petclinic.dto;

import java.time.LocalDate;
import java.util.List;

    /**
     * DTO for Purchase Order
     */
    public class PurchaseOrderDTO {
        private Integer orderId;
        private Integer supplierId;
        private LocalDate orderDate;
        private Double totalAmount;
        private String status;
        private LocalDate receivedDate;

        // For display purposes (not in DB)
        private String supplierName;
        private List<PurchaseOrderItemDTO> items;

        public PurchaseOrderDTO() {
        }

        public PurchaseOrderDTO(Integer orderId, Integer supplierId, LocalDate orderDate,
                                Double totalAmount, String status, LocalDate receivedDate) {
            this.orderId = orderId;
            this.supplierId = supplierId;
            this.orderDate = orderDate;
            this.totalAmount = totalAmount;
            this.status = status;
            this.receivedDate = receivedDate;
        }

        public PurchaseOrderDTO(Integer supplierId, LocalDate orderDate, Double totalAmount,
                                String status, List<PurchaseOrderItemDTO> items) {
            this.supplierId = supplierId;
            this.orderDate = orderDate;
            this.totalAmount = totalAmount;
            this.status = status;
            this.items = items;
        }

        // Getters and Setters
        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(Integer supplierId) {
            this.supplierId = supplierId;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getReceivedDate() {
            return receivedDate;
        }

        public void setReceivedDate(LocalDate receivedDate) {
            this.receivedDate = receivedDate;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public List<PurchaseOrderItemDTO> getItems() {
            return items;
        }

        public void setItems(List<PurchaseOrderItemDTO> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "PurchaseOrderDTO{" +
                    "orderId=" + orderId +
                    ", supplierId=" + supplierId +
                    ", orderDate=" + orderDate +
                    ", totalAmount=" + totalAmount +
                    ", status='" + status + '\'' +
                    ", receivedDate=" + receivedDate +
                    '}';
        }
    }

