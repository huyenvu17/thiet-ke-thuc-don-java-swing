/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.NguyenLieuDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface INguyenLieuController {
   List<NguyenLieuDTO> getAllKhachHang();
    boolean addNguyenLieu(NguyenLieuDTO nlDto);
}
