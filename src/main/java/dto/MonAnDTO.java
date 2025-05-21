package dto;

/**
 *
 * @author ADMIN
 */
public class MonAnDTO {
    private int id;
    private String tenMon;
    private String loaiMon;
    private String cachCheBien;
    
    // Default constructor
    public MonAnDTO() {
    }

    public MonAnDTO(int id, String tenMon, String loaiMon, String cachCheBien) {
        this.id = id;
        this.tenMon = tenMon;
        this.loaiMon = loaiMon;
        this.cachCheBien = cachCheBien;
    }

    public MonAnDTO(String tenMon, String loaiMon, String cachCheBien) {
        this.id = 0;
        this.tenMon = tenMon;
        this.loaiMon = loaiMon;
        this.cachCheBien = cachCheBien;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }

    public String getCachCheBien() {
        return cachCheBien;
    }

    public void setCachCheBien(String cachCheBien) {
        this.cachCheBien = cachCheBien;
    }
    
    @Override
    public String toString() {
        return tenMon;
    }
} 