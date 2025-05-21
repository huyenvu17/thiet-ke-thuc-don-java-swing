package model;

/**
 *
 * @author ADMIN
 */
public class CongThucMonAn {
    private int id;
    private int monAnId;
    private int nguyenLieuId;
    private Double khoiLuong;

    /**
     * Constructor with all fields
     */
    public CongThucMonAn(int id, int monAnId, int nguyenLieuId, Double khoiLuong) {
        this.id = id;
        this.monAnId = monAnId;
        this.nguyenLieuId = nguyenLieuId;
        this.khoiLuong = khoiLuong;
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

    /**
     * Get NguyenLieu ID
     */
    public int getNguyenLieuId() {
        return nguyenLieuId;
    }

    /**
     * Set NguyenLieu ID
     */
    public void setNguyenLieuId(int nguyenLieuId) {
        this.nguyenLieuId = nguyenLieuId;
    }

    /**
     * Get amount
     */
    public Double getKhoiLuong() {
        return khoiLuong;
    }

    /**
     * Set amount
     */
    public void setKhoiLuong(Double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }
} 