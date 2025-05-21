package model;

/**
 *
 * @author ADMIN
 */
public class ChiTietThucDon {
    private int id;
    private int thucDonId;
    private int ngay;
    private String buoi;
    private int monAnId;
    
    /**
     * Constructor with all fields
     */
    public ChiTietThucDon(int id, int thucDonId, int ngay, String buoi, int monAnId) {
        this.id = id;
        this.thucDonId = thucDonId;
        this.ngay = ngay;
        this.buoi = buoi;
        this.monAnId = monAnId;
    }
    
    /**
     * Get ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get ThucDon ID
     */
    public int getThucDonId() {
        return thucDonId;
    }
    
    /**
     * Set ThucDon ID
     */
    public void setThucDonId(int thucDonId) {
        this.thucDonId = thucDonId;
    }
    
    /**
     * Get day
     */
    public int getNgay() {
        return ngay;
    }
    
    /**
     * Set day
     */
    public void setNgay(int ngay) {
        this.ngay = ngay;
    }
    
    /**
     * Get meal type
     */
    public String getBuoi() {
        return buoi;
    }
    
    /**
     * Set meal type
     */
    public void setBuoi(String buoi) {
        this.buoi = buoi;
    }
    
    /**
     * Get MonAn ID
     */
    public int getMonAnId() {
        return monAnId;
    }
    
    /**
     * Set MonAn ID
     */
    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }
} 