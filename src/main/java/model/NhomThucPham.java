package model;

/**
 *
 * @author ADMIN
 */
public class NhomThucPham {
    private int id;
    private String tenNhom;
    private String moTa;

    /**
     * Constructor with all fields
     */
    public NhomThucPham(int id, String tenNhom, String moTa) {
        this.id = id;
        this.tenNhom = tenNhom;
        this.moTa = moTa;
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
     * Get group name
     */
    public String getTenNhom() {
        return tenNhom;
    }

    /**
     * Set group name
     */
    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    /**
     * Get description
     */
    public String getMoTa() {
        return moTa;
    }

    /**
     * Set description
     */
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenNhom;
    }
} 