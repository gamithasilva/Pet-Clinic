package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.dto.PurchaseOrderDTO;
import lk.ijse.petclinic.dto.PurchaseOrderItemDTO;
import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Purchase Order operations
 */
public class PurchaseOrderModel {

    /**
     * Save a new purchase order with its items (Transaction)
     */
    public boolean savePurchaseOrder(PurchaseOrderDTO orderDTO) {
        try {
            // 1. Insert purchase order
            String orderSql = "INSERT INTO purchase_orders (supplier_id, order_date, total_amount, status) VALUES (?, ?, ?, ?)";

            boolean orderSaved = Crudutil.execute(
                    orderSql,
                    orderDTO.getSupplierId(),
                    orderDTO.getOrderDate(),
                    orderDTO.getTotalAmount(),
                    orderDTO.getStatus()
            );

            if (!orderSaved) {
                return false;
            }

            // 2. Get the maximum order_id (the one just inserted)
            String getMaxIdSql = "SELECT MAX(order_id) FROM purchase_orders";
            ResultSet rs = Crudutil.execute(getMaxIdSql);

            int orderId = 0;
            if (rs != null && rs.next()) {
                orderId = rs.getInt(1);
            }

            // Check if orderId was retrieved
            if (orderId == 0) {
                throw new RuntimeException("Failed to retrieve generated order_id");
            }

            // 3. Insert order items
            String itemSql = "INSERT INTO purchase_order_items (order_id, medicine_id, quantity, unit_buying_price) VALUES (?, ?, ?, ?)";

            for (PurchaseOrderItemDTO item : orderDTO.getItems()) {
                boolean itemSaved = Crudutil.execute(
                        itemSql,
                        orderId,
                        item.getMedicineId(),
                        item.getQuantity(),
                        item.getUnitBuyingPrice()
                );

                if (!itemSaved) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all purchase orders
     */
    public List<PurchaseOrderDTO> getAllOrders() {
        String sql = "SELECT po.*, s.name AS supplier_name " +
                "FROM purchase_orders po " +
                "INNER JOIN suppliers s ON po.supplier_id = s.supplier_id " +
                "ORDER BY po.order_date DESC";

        List<PurchaseOrderDTO> orders = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                LocalDate receivedDate = rs.getDate("received_date") != null ?
                        rs.getDate("received_date").toLocalDate() : null;

                PurchaseOrderDTO dto = new PurchaseOrderDTO(
                        rs.getInt("order_id"),
                        rs.getInt("supplier_id"),
                        orderDate,
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        receivedDate
                );
                dto.setSupplierName(rs.getString("supplier_name"));
                orders.add(dto);
            }

            return orders.isEmpty() ? null : orders;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get purchase order by ID with items
     */
    public PurchaseOrderDTO getOrderById(int orderId) {
        String orderSql = "SELECT po.*, s.name AS supplier_name " +
                "FROM purchase_orders po " +
                "INNER JOIN suppliers s ON po.supplier_id = s.supplier_id " +
                "WHERE po.order_id = ?";

        try {
            ResultSet rs = Crudutil.execute(orderSql, orderId);

            if (rs.next()) {
                LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                LocalDate receivedDate = rs.getDate("received_date") != null ?
                        rs.getDate("received_date").toLocalDate() : null;

                PurchaseOrderDTO dto = new PurchaseOrderDTO(
                        rs.getInt("order_id"),
                        rs.getInt("supplier_id"),
                        orderDate,
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        receivedDate
                );
                dto.setSupplierName(rs.getString("supplier_name"));

                // Get order items
                dto.setItems(getOrderItems(orderId));

                return dto;
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get items for a specific order
     */
    public List<PurchaseOrderItemDTO> getOrderItems(int orderId) {
        String sql = "SELECT poi.*, m.name AS medicine_name, m.generic_name " +
                "FROM purchase_order_items poi " +
                "INNER JOIN medicines m ON poi.medicine_id = m.medicine_id " +
                "WHERE poi.order_id = ?";

        List<PurchaseOrderItemDTO> items = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, orderId);

            while (rs.next()) {
                PurchaseOrderItemDTO item = new PurchaseOrderItemDTO(
                        rs.getInt("order_id"),
                        rs.getInt("medicine_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_buying_price")
                );
                item.setMedicineName(rs.getString("medicine_name"));
                item.setGenericName(rs.getString("generic_name"));
                items.add(item);
            }

            return items.isEmpty() ? null : items;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update purchase order status
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE purchase_orders SET status = ? WHERE order_id = ?";
        try {
            return Crudutil.execute(sql, status, orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mark order as received and update medicine stock
     */
    public boolean receiveOrder(int orderId) {
        try {
            // 1. Get order items
            List<PurchaseOrderItemDTO> items = getOrderItems(orderId);

            if (items == null) {
                return false;
            }

            // 2. Update medicine stock for each item
            String updateStockSql = "UPDATE medicines SET current_stock = current_stock + ? WHERE medicine_id = ?";

            for (PurchaseOrderItemDTO item : items) {
                boolean updated = Crudutil.execute(
                        updateStockSql,
                        item.getQuantity(),
                        item.getMedicineId()
                );

                if (!updated) {
                    return false;
                }
            }

            // 3. Update order status and received_date
            String updateOrderSql = "UPDATE purchase_orders SET status = 'Received', received_date = ? WHERE order_id = ?";

            return Crudutil.execute(updateOrderSql, LocalDate.now(), orderId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete purchase order
     */
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM purchase_orders WHERE order_id = ?";
        try {
            return Crudutil.execute(sql, orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Search orders by supplier or status
     */
    public List<PurchaseOrderDTO> searchOrders(String keyword) {
        String sql = "SELECT po.*, s.name AS supplier_name " +
                "FROM purchase_orders po " +
                "INNER JOIN suppliers s ON po.supplier_id = s.supplier_id " +
                "WHERE s.name LIKE ? OR po.status LIKE ? " +
                "ORDER BY po.order_date DESC";

        String searchPattern = "%" + keyword + "%";
        List<PurchaseOrderDTO> orders = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, searchPattern, searchPattern);

            while (rs.next()) {
                LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                LocalDate receivedDate = rs.getDate("received_date") != null ?
                        rs.getDate("received_date").toLocalDate() : null;

                PurchaseOrderDTO dto = new PurchaseOrderDTO(
                        rs.getInt("order_id"),
                        rs.getInt("supplier_id"),
                        orderDate,
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        receivedDate
                );
                dto.setSupplierName(rs.getString("supplier_name"));
                orders.add(dto);
            }

            return orders.isEmpty() ? null : orders;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get pending orders count
     */
    public int getPendingOrdersCount() {
        String sql = "SELECT COUNT(*) FROM purchase_orders WHERE status = 'Pending'";
        try {
            ResultSet rs = Crudutil.execute(sql);

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

