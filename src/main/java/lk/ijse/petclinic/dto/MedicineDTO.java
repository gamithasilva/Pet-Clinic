package lk.ijse.petclinic.dto;

public class MedicineDTO {
    private Integer medicineId;
    private String name;
    private String genericName;
    private String unit;
    private Double buyingPrice;
    private Double sellingPrice;
    private Integer currentStock;
    private Integer reorderLevel;
    private String imageURL;

    public MedicineDTO() {
    }

    public MedicineDTO(Integer medicineId, String name, String genericName, String unit,
                       Double buyingPrice, Double sellingPrice, Integer currentStock,
                       Integer reorderLevel, String imageURL) {
        this.medicineId = medicineId;
        this.name = name;
        this.genericName = genericName;
        this.unit = unit;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.imageURL = imageURL;
    }

    // Getters and Setters
    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "MedicineDTO{" +
                "medicineId=" + medicineId +
                ", name='" + name + '\'' +
                ", genericName='" + genericName + '\'' +
                ", unit='" + unit + '\'' +
                ", currentStock=" + currentStock +
                '}';
    }
    
}
