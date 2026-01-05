package lk.ijse.petclinic.model;

import lk.ijse.petclinic.dto.MedicineDTO;
import lk.ijse.petclinic.util.Crudutil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineModel {

    public List<MedicineDTO> getAllMedicines() {
        String sql = "SELECT * FROM medicines ORDER BY name";
        List<MedicineDTO> medicines = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }

            return medicines.isEmpty() ? null : medicines;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MedicineDTO getMedicineById(int medicineId) {
        String sql = "SELECT * FROM medicines WHERE medicine_id = ?";

        try {
            ResultSet rs = Crudutil.execute(sql, medicineId);

            if (rs.next()) {
                return extractMedicineFromResultSet(rs);
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MedicineDTO> searchMedicines(String keyword) {
        String sql = "SELECT * FROM medicines WHERE name LIKE ? OR generic_name LIKE ? ORDER BY name";
        String searchPattern = "%" + keyword + "%";
        List<MedicineDTO> medicines = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql, searchPattern, searchPattern);

            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }

            return medicines.isEmpty() ? null : medicines;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MedicineDTO> getLowStockMedicines() {
        String sql = "SELECT * FROM medicines WHERE current_stock <= reorder_level ORDER BY current_stock";
        List<MedicineDTO> medicines = new ArrayList<>();

        try {
            ResultSet rs = Crudutil.execute(sql);

            while (rs.next()) {
                medicines.add(extractMedicineFromResultSet(rs));
            }

            return medicines.isEmpty() ? null : medicines;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addMedicine(MedicineDTO dto) {
        String sql = "INSERT INTO medicines (name, generic_name, unit, buying_price, selling_price, current_stock, reorder_level, imageURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return Crudutil.execute(sql,
                    dto.getName(),
                    dto.getGenericName(),
                    dto.getUnit(),
                    dto.getBuyingPrice(),
                    dto.getSellingPrice(),
                    dto.getCurrentStock(),
                    dto.getReorderLevel(),
                    dto.getImageURL()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateMedicine(MedicineDTO dto) {
        String sql = "UPDATE medicines SET name = ?, generic_name = ?, unit = ?, buying_price = ?, selling_price = ?, current_stock = ?, reorder_level = ?, imageURL = ? WHERE medicine_id = ?";
        try {
            return Crudutil.execute(sql,
                    dto.getName(),
                    dto.getGenericName(),
                    dto.getUnit(),
                    dto.getBuyingPrice(),
                    dto.getSellingPrice(),
                    dto.getCurrentStock(),
                    dto.getReorderLevel(),
                    dto.getImageURL(),
                    dto.getMedicineId()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteMedicine(int medicineId) {
        String sql = "DELETE FROM medicines WHERE medicine_id = ?";
        try {
            return Crudutil.execute(sql, medicineId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MedicineDTO extractMedicineFromResultSet(ResultSet rs) throws Exception {
        return new MedicineDTO(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getString("generic_name"),
                rs.getString("unit"),
                rs.getDouble("buying_price"),
                rs.getDouble("selling_price"),
                rs.getInt("current_stock"),
                rs.getInt("reorder_level"),
                rs.getString("imageURL")
        );
    }
}
