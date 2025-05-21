package dto;

/**
 *
 * @author ADMIN
 */
public class NhomThucPhamDTO {
    private int id;
    private String tenNhom;
    private String moTa;
    private int soLuongNguyenLieu;
    
    // Default constructor
    public NhomThucPhamDTO() {
    }

    public NhomThucPhamDTO(int id, String tenNhom, String moTa) {
        this.id = id;
        this.tenNhom = tenNhom;
        this.moTa = moTa;
        this.soLuongNguyenLieu = 0;
    }

    public NhomThucPhamDTO(int id, String tenNhom, String moTa, int soLuongNguyenLieu) {
        this.id = id;
        this.tenNhom = tenNhom;
        this.moTa = moTa;
        this.soLuongNguyenLieu = soLuongNguyenLieu;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNhom() {
        return tenNhom;
    }

    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public int getSoLuongNguyenLieu() {
        return soLuongNguyenLieu;
    }
    
    public void setSoLuongNguyenLieu(int soLuongNguyenLieu) {
        this.soLuongNguyenLieu = soLuongNguyenLieu;
    }
    
    @Override
    public String toString() {
        return tenNhom;
    }
} 