package service;

import dao.MonAnDao;
import dao.ThucDonDao;
import dao.ChiTietThucDonDao;
import entity.MonAnEntity;
import entity.ThucDonEntity;
import entity.ChiTietThucDonEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service for generating menu suggestions
 */
public class ThucDonService {
    
    private final MonAnDao monAnDao;
    private final ThucDonDao thucDonDao;
    private final ChiTietThucDonDao chiTietThucDonDao;
    private final Random random;
    
    /**
     * Constructor for ThucDonService
     */
    public ThucDonService() {
        this.monAnDao = MonAnDao.getInstance();
        this.thucDonDao = ThucDonDao.getInstance();
        this.chiTietThucDonDao = ChiTietThucDonDao.getInstance();
        this.random = new Random();
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
        ThucDonEntity thucDon = new ThucDonEntity(0, tenThucDon, soNgay);
        int thucDonId = thucDonDao.addThucDon(thucDon);
        
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
                
                ChiTietThucDonEntity chiTiet = new ChiTietThucDonEntity(
                        0, 
                        thucDonId, 
                        day, 
                        mealType, 
                        selectedDish.id()
                );
                
                int chiTietId = chiTietThucDonDao.addChiTietThucDon(chiTiet);
                
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
     * Get a menu by ID including all its details
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
     * Get all menus with their details
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
        
        return detailsDeleted && menuDeleted;
    }
} 