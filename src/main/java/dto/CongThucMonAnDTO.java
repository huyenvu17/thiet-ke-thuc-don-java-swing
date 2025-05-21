package dto;

/**
 *
 * @author ADMIN
 */
public class CongThucMonAnDTO {
    private int id;
    private int monAnId;
    private String tenMonAn;
    private int nguyenLieuId;
    private String tenNguyenLieu;
    private Double khoiLuong;
    
    // Default constructor
    public CongThucMonAnDTO() {
    }

    public CongThucMonAnDTO(int id, int monAnId, String tenMonAn, int nguyenLieuId, String tenNguyenLieu, Double khoiLuong) {
        this.id = id;
        this.monAnId = monAnId;
        this.tenMonAn = tenMonAn;
        this.nguyenLieuId = nguyenLieuId;
        this.tenNguyenLieu = tenNguyenLieu;
        this.khoiLuong = khoiLuong;
    }

    public CongThucMonAnDTO(int id, int monAnId, int nguyenLieuId, Double khoiLuong) {
        this.id = id;
        this.monAnId = monAnId;
        this.nguyenLieuId = nguyenLieuId;
        this.khoiLuong = khoiLuong;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonAnId() {
        return monAnId;
    }

    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }
    
    public String getTenMonAn() {
        return tenMonAn;
    }
    
    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public int getNguyenLieuId() {
        return nguyenLieuId;
    }

    public void setNguyenLieuId(int nguyenLieuId) {
        this.nguyenLieuId = nguyenLieuId;
    }
    
    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }
    
    public void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    public Double getKhoiLuong() {
        return khoiLuong;
    }

    public void setKhoiLuong(Double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }
    
    @Override
    public String toString() {
        return tenMonAn + " - " + tenNguyenLieu;
    }
} 