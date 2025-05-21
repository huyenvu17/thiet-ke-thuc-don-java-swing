/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import doanthietkethucdon.BHException;

/**
 *
 * @author ADMIN
 */
public class NguyenLieu {

    private int id;
    private String tenNguyenLieu;
    private Double khoiLuong;
    private Double donGia;
    private int nhomThucPhamId;

    public NguyenLieu(
            int id,
            String tenNguyenLieu,
            Double khoiLuong,
            Double donGia) throws BHException {
        this(id, tenNguyenLieu, khoiLuong, donGia, 0);
    }
    
    public NguyenLieu(
            int id,
            String tenNguyenLieu,
            Double khoiLuong,
            Double donGia,
            int nhomThucPhamId) throws BHException {
        setId(id);
        setTenNguyenLieu(tenNguyenLieu);
        setKhoiLuong(khoiLuong);
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
     * @return the khoiLuong
     */
    public Double getKhoiLuong() {
        return khoiLuong;
    }

    /**
     * @param khoiLuong the khoiLuong to set
     */
    public final void setKhoiLuong(Double khoiLuong) {
        this.khoiLuong = khoiLuong;
    }

    /**
     * @return the donGia
     */
    public Double getDonGia() {
        return donGia;
    }

    /**
     * @param donGia the donGia to set
     */
    public final void setDonGia(Double donGia) {
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
