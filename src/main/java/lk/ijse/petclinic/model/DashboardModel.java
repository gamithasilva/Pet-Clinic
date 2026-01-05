package lk.ijse.petclinic.model;

import lk.ijse.petclinic.db.DBConnection;
import lk.ijse.petclinic.dto.DashboardDTO;
import lk.ijse.petclinic.util.Crudutil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardModel {

    /**
     * Get all dashboard statistics
     */
    public DashboardDTO getDashboardStats() throws SQLException {
        DashboardDTO stats = new DashboardDTO();

        stats.setTotalAppointments(getTotalAppointments());
        stats.setTotalPets(getTotalPets());
        stats.setActiveDoctors(getActiveDoctors());
        stats.setTotalRevenue(getTotalRevenue());
        stats.setLowStockMedicines(getLowStockMedicines());
        stats.setTodayAppointments(getTodayAppointments());
        stats.setPendingPayments(getPendingPayments());
        stats.setTotalOwners(getTotalOwners());

        return stats;
    }

    /**
     * Get total number of appointments
     */
    public int getTotalAppointments() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM appointments";
        try  {

            ResultSet rs = Crudutil.execute(sql);
            if (rs.next()) {
                return rs.getInt("total");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get total number of pets
     */
    public int getTotalPets() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM pets";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Get number of active doctors
     */
    public int getActiveDoctors() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM doctors WHERE status = 'Available'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Get total revenue from completed payments
     */
    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM payments WHERE status = 'completed'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    /**
     * Get count of medicines below reorder level
     */
    public int getLowStockMedicines() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM medicines WHERE current_stock <= reorder_level";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Get today's appointments count
     */
    public int getTodayAppointments() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM appointments WHERE DATE(appointment_datetime) = CURDATE()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Get total pending payments
     */
    public double getPendingPayments() throws SQLException {
        String sql = "SELECT SUM(amount) as total FROM payments WHERE status = 'pending'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    /**
     * Get total number of pet owners
     */
    public int getTotalOwners() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM pet_owners";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Get appointments data for last 7 days (for chart)
     */
    public Map<String, Integer> getAppointmentsLast7Days() throws SQLException {
        Map<String, Integer> data = new HashMap<>();
        String sql = "SELECT DATE(appointment_datetime) as date, COUNT(*) as count " +
                "FROM appointments " +
                "WHERE appointment_datetime >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                "GROUP BY DATE(appointment_datetime) " +
                "ORDER BY date";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("date"), rs.getInt("count"));
            }
        }
        return data;
    }

    /**
     * Get revenue data for last 6 months (for chart)
     */
    public Map<String, Double> getRevenueLast6Months() throws SQLException {
        Map<String, Double> data = new HashMap<>();
        String sql = "SELECT DATE_FORMAT(payment_date, '%Y-%m') as month, SUM(amount) as revenue " +
                "FROM payments " +
                "WHERE status = 'completed' " +
                "AND payment_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                "GROUP BY DATE_FORMAT(payment_date, '%Y-%m') " +
                "ORDER BY month";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("month"), rs.getDouble("revenue"));
            }
        }
        return data;
    }
}
