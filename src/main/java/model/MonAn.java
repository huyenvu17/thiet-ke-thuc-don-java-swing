package model;

/**
 *
 * @author ADMIN
 */
public class MonAn {
    private int id;
    private String tenMon;
    private String loaiMon;
    private String cachCheBien;

    /**
     * Constructor with all fields
     */
    public MonAn(int id, String tenMon, String loaiMon, String cachCheBien) {
        this.id = id;
        this.tenMon = tenMon;
        this.loaiMon = loaiMon;
        this.cachCheBien = cachCheBien;
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
     * Get dish name
     */
    public String getTenMon() {
        return tenMon;
    }

    /**
     * Set dish name
     */
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    /**
     * Get dish type/category
     */
    public String getLoaiMon() {
        return loaiMon;
    }

    /**
     * Set dish type/category
     */
    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }

    /**
     * Get cooking instructions
     */
    public String getCachCheBien() {
        return cachCheBien;
    }

    /**
     * Set cooking instructions
     */
    public void setCachCheBien(String cachCheBien) {
        this.cachCheBien = cachCheBien;
    }

    @Override
    public String toString() {
        return tenMon;
    }
} 