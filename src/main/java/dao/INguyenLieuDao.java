/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.NguyenLieuEntity;
import java.util.List;

/**
 * Interface for NguyenLieu Data Access Object
 */
public interface INguyenLieuDao {
    List<NguyenLieuEntity> getAllNguyenLieu();
    int addNguyenLieu(NguyenLieuEntity entity);
    NguyenLieuEntity getById(int id);
    boolean updateNguyenLieu(NguyenLieuEntity entity);
    boolean deleteNguyenLieu(int id);
}
