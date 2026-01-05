package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.SupplierDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {

    public List<SupplierDTO> getAllSuppliers() {
        String sql = "SELECT * FROM suppliers ORDER BY name";
        List<SupplierDTO> suppliers = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                suppliers.add(extractSupplierFromResultSet(rs));
            }

            return suppliers.isEmpty() ? null : suppliers;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SupplierDTO getSupplierById(int supplierId) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";

        try {
            ResultSet rs = Crudutil.execute(sql, supplierId);

            if (rs.next()) {
                return extractSupplierFromResultSet(rs);
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveSupplier(SupplierDTO dto) {
        String sql = "INSERT INTO suppliers (name, phone, email, address) VALUES (?, ?, ?, ?)";
        try {
            return Crudutil.execute(sql, dto.getName(), dto.getContactNumber(),
                    dto.getEmail(), dto.getAddress());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateSupplier(SupplierDTO dto) {
        String sql = "UPDATE suppliers SET name = ?, phone = ?, email = ?, address = ? WHERE supplier_id = ?";
        try {
            return Crudutil.execute(sql, dto.getName(), dto.getContactNumber(),
                    dto.getEmail(), dto.getAddress(), dto.getSupplierId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        try {
            return Crudutil.execute(sql, supplierId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SupplierDTO extractSupplierFromResultSet(ResultSet rs) throws Exception {
        return new SupplierDTO(
                rs.getInt("supplier_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address")
        );
    }
}
