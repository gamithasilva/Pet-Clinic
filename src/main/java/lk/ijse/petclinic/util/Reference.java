package lk.ijse.petclinic.util;

import lk.ijse.petclinic.controller.*;

import java.time.LocalDate;

public class Reference {
    public static ListViewController listview;

    public static OwnerViewController ownerView;
    public static PetView petView;
    public static SupplierView supplierView;
    public static PurchaseOrderListViewController purchaseOrderListView;
    public static AppointmentListViewController appointmentListView;
    public static InvoiceListViewController invoiceListView;
    public static MedicineView medicineView;






    public static java.sql.Date dateConverter(LocalDate selected){
        return java.sql.Date.valueOf(selected);
    }
}
