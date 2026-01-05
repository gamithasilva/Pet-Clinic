package lk.ijse.petclinic.dto;

public class DashboardDTO {
    private int totalAppointments;
    private int totalPets;
    private int activeDoctors;
    private double totalRevenue;
    private int lowStockMedicines;
    private int todayAppointments;
    private double pendingPayments;
    private int totalOwners;

    // Constructor
    public DashboardDTO() {}

    public DashboardDTO(int totalAppointments, int totalPets, int activeDoctors,
                        double totalRevenue, int lowStockMedicines, int todayAppointments,
                        double pendingPayments, int totalOwners) {
        this.totalAppointments = totalAppointments;
        this.totalPets = totalPets;
        this.activeDoctors = activeDoctors;
        this.totalRevenue = totalRevenue;
        this.lowStockMedicines = lowStockMedicines;
        this.todayAppointments = todayAppointments;
        this.pendingPayments = pendingPayments;
        this.totalOwners = totalOwners;
    }

    // Getters and Setters
    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getTotalPets() {
        return totalPets;
    }

    public void setTotalPets(int totalPets) {
        this.totalPets = totalPets;
    }

    public int getActiveDoctors() {
        return activeDoctors;
    }

    public void setActiveDoctors(int activeDoctors) {
        this.activeDoctors = activeDoctors;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getLowStockMedicines() {
        return lowStockMedicines;
    }

    public void setLowStockMedicines(int lowStockMedicines) {
        this.lowStockMedicines = lowStockMedicines;
    }

    public int getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(int todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    public double getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(double pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    public int getTotalOwners() {
        return totalOwners;
    }

    public void setTotalOwners(int totalOwners) {
        this.totalOwners = totalOwners;
    }
}
