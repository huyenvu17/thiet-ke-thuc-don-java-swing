package entity;

/**
 * Entity record representing a ChiTietThucDon (Menu Detail) in the system
 */
public record ChiTietThucDonEntity(
    int id,
    int thucDonId,
    int ngay,
    String buoi, // 'sang', 'trua', 'xe'
    int monAnId
) {} 