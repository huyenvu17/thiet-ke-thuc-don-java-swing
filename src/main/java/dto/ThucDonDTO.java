package dto;

/**
 *
 * @author ADMIN
 */
public class ThucDonDTO {
    private int id;
    private String tenThucDon;
    private int soNgay;
    
    // Default constructor
    public ThucDonDTO() {
    }

    public ThucDonDTO(int id, String tenThucDon, int soNgay) {
        this.id = id;
        this.tenThucDon = tenThucDon;
        this.soNgay = soNgay;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenThucDon() {
        return tenThucDon;
    }

    public void setTenThucDon(String tenThucDon) {
        this.tenThucDon = tenThucDon;
    }

    public int getSoNgay() {
        return soNgay;
    }

    public void setSoNgay(int soNgay) {
        this.soNgay = soNgay;
    }
    
    @Override
    public String toString() {
        return tenThucDon;
    }
} 