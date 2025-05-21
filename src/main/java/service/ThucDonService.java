package service;

import dao.MonAnDao;
import dao.ThucDonDao;
import dao.ChiTietThucDonDao;
import doanthietkethucdon.BHException;
import entity.MonAnEntity;
import entity.ThucDonEntity;
import entity.ChiTietThucDonEntity;
import model.ThucDon;
import model.ChiTietThucDon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ADMIN
 */
public class ThucDonService {
    private static ThucDonService instance;
    private final MonAnDao monAnDao;
    private final ThucDonDao thucDonDao;
    private final ChiTietThucDonDao chiTietThucDonDao;
    private final Random random;
    private final List<ThucDon> danhSachThucDon;
    
    public static ThucDonService getInstance() {
        if (ThucDonService.instance == null) {
            ThucDonService.instance = new ThucDonService();
        }
        return instance;
    }
    
    /**
     * Constructor for ThucDonService
     */
    private ThucDonService() {
        this.monAnDao = MonAnDao.getInstance();
        this.thucDonDao = ThucDonDao.getInstance();
        this.chiTietThucDonDao = ChiTietThucDonDao.getInstance();
        this.random = new Random();
        this.danhSachThucDon = new ArrayList<>();
    }
    
    /**
     * Get all menus
     */
    public List<ThucDon> getAllThucDon() {
        loadAllThucDonFromDatabase();
        return danhSachThucDon;
    }
    
    /**
     * Load all menus from database
     */
    public void loadAllThucDonFromDatabase() {
        this.danhSachThucDon.clear();
        List<ThucDonEntity> entityList = this.thucDonDao.getAllThucDon();
        for (ThucDonEntity entity : entityList) {
            try {
                this.danhSachThucDon.add(toModel(entity));
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Get menu by ID
     */
    public ThucDon getThucDonById(int id) {
        ThucDonEntity entity = this.thucDonDao.getThucDonById(id);
        if (entity != null) {
            try {
                return toModel(entity);
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Get all menu details for a specific menu
     */
    public List<ChiTietThucDon> getChiTietByThucDonId(int thucDonId) {
        List<ChiTietThucDon> result = new ArrayList<>();
        List<ChiTietThucDonEntity> entityList = this.chiTietThucDonDao.getByThucDonId(thucDonId);
        for (ChiTietThucDonEntity entity : entityList) {
            try {
                result.add(toModel(entity));
            } catch (Exception ex) {
                Logger.getLogger(ThucDonService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    /**
     * Generate a menu for a specific number of days
     * 
     * @param tenThucDon The name of the menu
     * @param soNgay The number of days
     * @param maxBudgetPerMeal Maximum budget per meal (optional)
     * @return The ID of the generated menu, or -1 if failed
     */
    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal) {
        // Create the ThucDon
        ThucDon thucDon = new ThucDon(0, tenThucDon, soNgay);
        int thucDonId = thucDonDao.addThucDon(toEntity(thucDon));
        
        if (thucDonId <= 0) {
            return -1; // Failed to create ThucDon
        }
        
        List<MonAnEntity> allMonAn = monAnDao.getAllMonAn();
        
        // Group by meal type
        Map<String, List<MonAnEntity>> monAnByType = allMonAn.stream()
                .collect(Collectors.groupingBy(MonAnEntity::loaiMon));
        
        // Generate meals for each day and each meal type
        String[] mealTypes = {"sang", "trua", "xe"};
        
        boolean success = true;
        
        for (int day = 1; day <= soNgay; day++) {
            for (String mealType : mealTypes) {
                // Get dishes for this meal type
                List<MonAnEntity> availableDishes = monAnByType.getOrDefault(mealType, new ArrayList<>());
                
                if (availableDishes.isEmpty()) {
                    continue; // Skip if no dishes available for this meal type
                }
                
                // Pick a random dish for this meal
                MonAnEntity selectedDish = pickRandomDish(availableDishes);
                
                ChiTietThucDon chiTiet = new ChiTietThucDon(
                        0, 
                        thucDonId, 
                        day, 
                        mealType, 
                        selectedDish.id()
                );
                
                int chiTietId = chiTietThucDonDao.addChiTietThucDon(toEntity(chiTiet));
                
                if (chiTietId <= 0) {
                    success = false;
                    break;
                }
            }
            
            if (!success) {
                break;
            }
        }
        
        if (!success) {
            // If something went wrong, clean up
            chiTietThucDonDao.deleteByThucDonId(thucDonId);
            thucDonDao.deleteThucDon(thucDonId);
            return -1;
        }
        
        // Reload data
        loadAllThucDonFromDatabase();
        
        return thucDonId;
    }
    
    /**
     * Pick a random dish from a list
     * 
     * @param dishes List of dishes to choose from
     * @return A randomly selected dish
     */
    private MonAnEntity pickRandomDish(List<MonAnEntity> dishes) {
        int randomIndex = random.nextInt(dishes.size());
        return dishes.get(randomIndex);
    }
    
    /**
     * Get a menu by ID including all its details as entities
     * 
     * @param thucDonId The ID of the menu
     * @return A map with the menu and its details
     */
    public Map<String, Object> getThucDonWithDetails(int thucDonId) {
        Map<String, Object> result = new HashMap<>();
        
        ThucDonEntity thucDon = thucDonDao.getThucDonById(thucDonId);
        if (thucDon == null) {
            return result; // Empty result if menu not found
        }
        
        List<ChiTietThucDonEntity> chiTietList = chiTietThucDonDao.getByThucDonId(thucDonId);
        
        result.put("thucDon", thucDon);
        result.put("chiTietList", chiTietList);
        
        return result;
    }
    
    /**
     * Get all menus with their details as entities
     * 
     * @return A list of maps, each containing a menu and its details
     */
    public List<Map<String, Object>> getAllThucDonWithDetails() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        List<ThucDonEntity> thucDonList = thucDonDao.getAllThucDon();
        
        for (ThucDonEntity thucDon : thucDonList) {
            Map<String, Object> thucDonMap = new HashMap<>();
            List<ChiTietThucDonEntity> chiTietList = chiTietThucDonDao.getByThucDonId(thucDon.id());
            
            thucDonMap.put("thucDon", thucDon);
            thucDonMap.put("chiTietList", chiTietList);
            
            result.add(thucDonMap);
        }
        
        return result;
    }
    
    /**
     * Delete a menu and all its details
     * 
     * @param thucDonId The ID of the menu to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteThucDon(int thucDonId) {
        // First delete all menu details
        boolean detailsDeleted = chiTietThucDonDao.deleteByThucDonId(thucDonId);
        
        // Then delete the menu
        boolean menuDeleted = thucDonDao.deleteThucDon(thucDonId);
        
        if (detailsDeleted && menuDeleted) {
            // Reload data if successful
            loadAllThucDonFromDatabase();
        }
        
        return detailsDeleted && menuDeleted;
    }
    
    /**
     * Convert ThucDon entity to model
     */
    public ThucDon toModel(ThucDonEntity entity) {
        return new ThucDon(
                entity.id(),
                entity.tenThucDon(),
                entity.soNgay());
    }
    
    /**
     * Convert ThucDon model to entity
     */
    public ThucDonEntity toEntity(ThucDon model) {
        return new ThucDonEntity(
                model.getId(),
                model.getTenThucDon(),
                model.getSoNgay());
    }
    
    /**
     * Convert ChiTietThucDon entity to model
     */
    public ChiTietThucDon toModel(ChiTietThucDonEntity entity) {
        return new ChiTietThucDon(
                entity.id(),
                entity.thucDonId(),
                entity.ngay(),
                entity.buoi(),
                entity.monAnId());
    }
    
    /**
     * Convert ChiTietThucDon model to entity
     */
    public ChiTietThucDonEntity toEntity(ChiTietThucDon model) {
        return new ChiTietThucDonEntity(
                model.getId(),
                model.getThucDonId(),
                model.getNgay(),
                model.getBuoi(),
                model.getMonAnId());
    }
} 