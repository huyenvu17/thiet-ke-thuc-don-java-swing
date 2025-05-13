/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import doanthietkethucdon.BHException;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class NguyenLieu {

    private int id;
    private String tenNguyenLieu;
    private String donViTinh;
    private BigDecimal donGia;
    private int nhomThucPhamId;

    public NguyenLieu(
            int id,
            String tenNguyenLieu,
            String donViTinh,
            BigDecimal donGia) throws BHException {
        this(id, tenNguyenLieu, donViTinh, donGia, 0);
    }
    
    public NguyenLieu(
            int id,
            String tenNguyenLieu,
            String donViTinh,
            BigDecimal donGia,
            int nhomThucPhamId) throws BHException {
        setId(id);
        setTenNguyenLieu(tenNguyenLieu);
        setDonViTinh(donViTinh);
        setDonGia(donGia);
        setNhomThucPhamId(nhomThucPhamId);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * @return the tenNguyenLieu
     */
    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }

    /**
     * @param tenNguyenLieu the tenNguyenLieu to set
     */
    public final void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    /**
     * @return the donViTinh
     */
    public String getDonViTinh() {
        return donViTinh;
    }

    /**
     * @param donViTinh the donViTinh to set
     */
    public final void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    /**
     * @return the donGia
     */
    public BigDecimal getDonGia() {
        return donGia;
    }

    /**
     * @param donGia the donGia to set
     */
    public final void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
    
    /**
     * @return the nhomThucPhamId
     */
    public int getNhomThucPhamId() {
        return nhomThucPhamId;
    }

    /**
     * @param nhomThucPhamId the nhomThucPhamId to set
     */
    public final void setNhomThucPhamId(int nhomThucPhamId) {
        this.nhomThucPhamId = nhomThucPhamId;
    }
}
