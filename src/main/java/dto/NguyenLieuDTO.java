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
    private Double khoiLuong;
    private BigDecimal donGia;
    private int nhomThucPhamId;
    private String tenNhomThucPham;
    
    // Default constructor
    public NguyenLieuDTO() {
    }
    
    // Constructor with all fields except ID
    public NguyenLieuDTO(String tenNguyenLieu, Double khoiLuong, BigDecimal donGia) {
        this.tenNguyenLieu = tenNguyenLieu;
        this.khoiLuong = khoiLuong;
        this.donGia = donGia;
    }
    
    // Constructor with all fields
    public NguyenLieuDTO(int id, String tenNguyenLieu, Double khoiLuong, BigDecimal donGia, 
                        int nhomThucPhamId, String tenNhomThucPham) {
        this.id = id;
        this.tenNguyenLieu = tenNguyenLieu;
        this.khoiLuong = khoiLuong;
        this.donGia = donGia;
        this.nhomThucPhamId = nhomThucPhamId;
        this.tenNhomThucPham = tenNhomThucPham;
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

    public Double getKhoiLuong() {
        return khoiLuong;
    }

    public void setKhoiLuong(Double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
    
    public int getNhomThucPhamId() {
        return nhomThucPhamId;
    }
    
    public void setNhomThucPhamId(int nhomThucPhamId) {
        this.nhomThucPhamId = nhomThucPhamId;
    }
    
    public String getTenNhomThucPham() {
        return tenNhomThucPham;
    }
    
    public void setTenNhomThucPham(String tenNhomThucPham) {
        this.tenNhomThucPham = tenNhomThucPham;
    }
    
    @Override
    public String toString() {
        return tenNguyenLieu;
    }
}
