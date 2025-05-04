/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class NguyenLieuDTO {
    private int id;
    private String tenNguyenLieu;
    private String donViTinh;
    private BigDecimal donGia;
    
    // Default constructor
    public NguyenLieuDTO() {
    }
    
    // Constructor with all fields except ID
    public NguyenLieuDTO(String tenNguyenLieu, String donViTinh, BigDecimal donGia) {
        this.tenNguyenLieu = tenNguyenLieu;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
    }
    
    // Constructor with all fields
    public NguyenLieuDTO(int id, String tenNguyenLieu, String donViTinh, BigDecimal donGia) {
        this.id = id;
        this.tenNguyenLieu = tenNguyenLieu;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }

    public void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
    
    @Override
    public String toString() {
        return tenNguyenLieu;
    }
}
