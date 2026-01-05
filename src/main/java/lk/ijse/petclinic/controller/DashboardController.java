package lk.ijse.petclinic.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import lk.ijse.petclinic.dto.DashboardDTO;
import lk.ijse.petclinic.model.DashboardModel;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalAppointmentsLabel;

    @FXML
    private Label appointmentsTrendLabel;

    @FXML
    private Label totalPetsLabel;

    @FXML
    private Label petsTrendLabel;

    @FXML
    private Label activeDoctorsLabel;

    @FXML
    private Label doctorsTrendLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label revenueTrendLabel;

    @FXML
    private Label lowStockLabel;

    @FXML
    private Label todayAppointmentsLabel;

    @FXML
    private Label pendingPaymentsLabel;

    @FXML
    private Label totalOwnersLabel;

    @FXML
    private LineChart<String, Number> appointmentsChart;

    @FXML
    private BarChart<String, Number> revenueChart;

    private DashboardModel dashboardModel;
    private DecimalFormat currencyFormat;

    @FXML
    public void initialize() {
        dashboardModel = new DashboardModel();
        currencyFormat = new DecimalFormat("#,##0.00");

        // Load dashboard data
        loadDashboardData();
        loadAppointmentsChart();
        loadRevenueChart();
    }

    /**
     * Load all dashboard statistics
     */
    private void loadDashboardData() {
        try {
            DashboardDTO stats = dashboardModel.getDashboardStats();

            // Update statistics cards
            totalAppointmentsLabel.setText(String.valueOf(stats.getTotalAppointments()));
            totalPetsLabel.setText(String.valueOf(stats.getTotalPets()));
            activeDoctorsLabel.setText(String.valueOf(stats.getActiveDoctors()));
            totalRevenueLabel.setText("Rs. " + currencyFormat.format(stats.getTotalRevenue()));

            // Update quick stats
            lowStockLabel.setText(stats.getLowStockMedicines() + " medicines below reorder level");
            todayAppointmentsLabel.setText(stats.getTodayAppointments() + " appointments scheduled");
            pendingPaymentsLabel.setText("Rs. " + currencyFormat.format(stats.getPendingPayments()) + " pending");
            totalOwnersLabel.setText(stats.getTotalOwners() + " registered owners");

        } catch (SQLException e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load appointments chart (last 7 days)
     */
    private void loadAppointmentsChart() {
        try {
            Map<String, Integer> data = dashboardModel.getAppointmentsLast7Days();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Appointments");

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            appointmentsChart.getData().clear();
            appointmentsChart.getData().add(series);

        } catch (SQLException e) {
            System.err.println("Error loading appointments chart: " + e.getMessage());
        }
    }

    /**
     * Load revenue chart (last 6 months)
     */
    private void loadRevenueChart() {
        try {
            Map<String, Double> data = dashboardModel.getRevenueLast6Months();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Revenue");

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            revenueChart.getData().clear();
            revenueChart.getData().add(series);

        } catch (SQLException e) {
            System.err.println("Error loading revenue chart: " + e.getMessage());
        }
    }

    /**
     * Refresh dashboard data
     */
    public void refreshDashboard() {
        loadDashboardData();
        loadAppointmentsChart();
        loadRevenueChart();
    }
}