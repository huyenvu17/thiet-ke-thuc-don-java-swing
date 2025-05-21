package model;

/**
 *
 * @author ADMIN
 */
public class ThucDon {
    private int id;
    private String tenThucDon;
    private int soNgay;
    
    /**
     * Constructor with all fields
     */
    public ThucDon(int id, String tenThucDon, int soNgay) {
        this.id = id;
        this.tenThucDon = tenThucDon;
        this.soNgay = soNgay;
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
     * Get menu name
     */
    public String getTenThucDon() {
        return tenThucDon;
    }
    
    /**
     * Set menu name
     */
    public void setTenThucDon(String tenThucDon) {
        this.tenThucDon = tenThucDon;
    }
    
    /**
     * Get number of days
     */
    public int getSoNgay() {
        return soNgay;
    }
    
    /**
     * Set number of days
     */
    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }
} 