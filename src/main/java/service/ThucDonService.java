package service;

import dao.MonAnDao;
import dao.ThucDonDao;
import dao.ChiTietThucDonDao;
import dao.CongThucMonAnDao;
import doanthietkethucdon.BHException;
import entity.MonAnEntity;
import entity.ThucDonEntity;
import entity.ChiTietThucDonEntity;
import entity.CongThucMonAnEntity;
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
    private final CongThucMonAnDao congThucMonAnDao;
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
        this.congThucMonAnDao = CongThucMonAnDao.getInstance();
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
     * @param maxBudgetPerMeal Maximum budget per meal
     * @param selectedNguyenLieuIds List of selected ingredient IDs to include
     * @return The ID of the generated menu, or a negative error code if failed:
     *         -1: General failure
     *         -2: No dishes available with selected ingredients
     *         -3: Missing dishes for one or more meal types
     */
    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal, List<Integer> selectedNguyenLieuIds) {
        // Get all available dishes
        List<MonAnEntity> allMonAn = monAnDao.getAllMonAn();
        
        // Filter dishes based on selected ingredients (if any)
        List<MonAnEntity> filteredMonAn = allMonAn;
        if (selectedNguyenLieuIds != null && !selectedNguyenLieuIds.isEmpty()) {
            filteredMonAn = filterMonAnByNguyenLieu(allMonAn, selectedNguyenLieuIds);
            
            // Check if we have any dishes after filtering
            if (filteredMonAn.isEmpty()) {
                return -2; // No dishes available with selected ingredients
            }
        }
        
        // Group by meal type
        Map<String, List<MonAnEntity>> monAnByType = filteredMonAn.stream()
                .collect(Collectors.groupingBy(MonAnEntity::loaiMon));
        
        // Validate that we have dishes for each meal type
        String[] mealTypes = {"sang", "trua", "xe"};
        boolean hasMissingMealType = false;
        
        for (String mealType : mealTypes) {
            List<MonAnEntity> dishesForMealType = monAnByType.getOrDefault(mealType, new ArrayList<>());
            if (dishesForMealType.isEmpty()) {
                hasMissingMealType = true;
                break;
            }
        }
        
        if (hasMissingMealType) {
            return -3; // Missing dishes for one or more meal types
        }
        
        // Create the ThucDon
        ThucDon thucDon = new ThucDon(0, tenThucDon, soNgay);
        int thucDonId = thucDonDao.addThucDon(toEntity(thucDon));
        
        if (thucDonId <= 0) {
            return -1; // Failed to create ThucDon
        }
        
        boolean success = true;
        
        // Track selected dishes to avoid repetition
        Map<String, List<MonAnEntity>> selectedDishes = new HashMap<>();
        for (String mealType : mealTypes) {
            selectedDishes.put(mealType, new ArrayList<>());
        }
        
        for (int day = 1; day <= soNgay; day++) {
            for (String mealType : mealTypes) {
                // Get dishes for this meal type
                List<MonAnEntity> availableDishes = monAnByType.getOrDefault(mealType, new ArrayList<>());
                
                // Apply budget constraint if provided
                Double maxBudget = maxBudgetPerMeal.getOrDefault(mealType, Double.MAX_VALUE);
                List<MonAnEntity> affordableDishes = filterByBudget(availableDishes, maxBudget);
                
                if (affordableDishes.isEmpty()) {
                    // If no dishes within budget, use the original list
                    affordableDishes = availableDishes;
                }
                
                // Pick a dish for this meal considering previous selections
                MonAnEntity selectedDish = selectDishForMeal(affordableDishes, day, mealType, selectedDishes.get(mealType));
                
                // Skip if no dish could be selected
                if (selectedDish == null) {
                    continue;
                }
                
                // Track the selected dish to avoid repetition
                selectedDishes.get(mealType).add(selectedDish);
                
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
     * Filter dishes by selected ingredients
     * A dish is included if it uses at least one of the selected ingredients
     * 
     * @param monAnList List of all dishes
     * @param selectedNguyenLieuIds IDs of selected ingredients
     * @return Filtered list of dishes
     */
    private List<MonAnEntity> filterMonAnByNguyenLieu(List<MonAnEntity> monAnList, List<Integer> selectedNguyenLieuIds) {
        List<MonAnEntity> filteredList = new ArrayList<>();
        
        for (MonAnEntity monAn : monAnList) {
            List<CongThucMonAnEntity> congThuc = congThucMonAnDao.getCongThucByMonAnId(monAn.id());
            
            // Check if any ingredient in this dish is in the selected list
            boolean containsSelectedIngredient = congThuc.stream()
                    .anyMatch(ct -> selectedNguyenLieuIds.contains(ct.nguyenLieuId()));
            
            if (containsSelectedIngredient) {
                filteredList.add(monAn);
            }
        }
        
        return filteredList;
    }
    
    /**
     * Filter dishes by maximum budget
     * 
     * @param monAnList List of dishes to filter
     * @param maxBudget Maximum budget per meal
     * @return Filtered list of dishes within budget
     */
    private List<MonAnEntity> filterByBudget(List<MonAnEntity> monAnList, Double maxBudget) {
        // In a real application, we would calculate the cost of each dish
        // based on its ingredients and their prices
        // For simplicity, we'll return all dishes for now
        return new ArrayList<>(monAnList);
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
     * Select the best dish for a meal based on various factors
     * 
     * @param dishes List of available dishes
     * @param day Current day in the menu
     * @param mealType Type of meal (breakfast, lunch, dinner)
     * @param selectedDishes Previously selected dishes in this menu
     * @return The best dish for this meal
     */
    private MonAnEntity selectDishForMeal(List<MonAnEntity> dishes, int day, String mealType, List<MonAnEntity> selectedDishes) {
        // If we have no dishes available, just return null
        if (dishes.isEmpty()) {
            return null;
        }
        
        // If we only have one dish available, return it
        if (dishes.size() == 1) {
            return dishes.get(0);
        }
        
        // Create a list of candidate dishes
        List<MonAnEntity> candidates = new ArrayList<>(dishes);
        
        // Remove recently used dishes to avoid repetition
        // We want to avoid using the same dish in consecutive periods
        if (!selectedDishes.isEmpty() && candidates.size() > 2) {
            // Get the most recently used dishes (up to last 3 dishes)
            List<MonAnEntity> recentDishes = selectedDishes.subList(
                    Math.max(0, selectedDishes.size() - 3),
                    selectedDishes.size());
                    
            // Remove them from candidates if possible
            candidates.removeAll(recentDishes);
            
            // If we removed all candidates, add some back to avoid having no options
            if (candidates.isEmpty()) {
                candidates = new ArrayList<>(dishes);
            }
        }
        
        // Apply meal-specific selection logic
        if ("sang".equals(mealType)) {
            // For breakfast, prefer lighter dishes 
            // In a real app, we would check ingredient types or dish categories
            // For simplicity, we'll just pick randomly from filtered candidates
            return pickRandomDish(candidates);
        } else if ("trua".equals(mealType)) {
            // For lunch, prefer substantial dishes
            // You could implement more complex logic here based on your needs
            return pickRandomDish(candidates);
        } else if ("xe".equals(mealType)) {
            // For dinner, avoid dishes already used at lunch on the same day
            // In a real app, we'd access the lunch dish for this day
            return pickRandomDish(candidates);
        }
        
        // Default case: pick a random dish from the filtered candidates
        return pickRandomDish(candidates);
    }
    
    /**
     * Calculate a score for each dish based on how well it fits in the current menu
     * Higher score means better fit
     * 
     * @param dish The dish to evaluate
     * @param day Current day in the menu
     * @param mealType Type of meal
     * @param selectedDishes Previously selected dishes
     * @return Score value (higher is better)
     */
    private double calculateDishScore(MonAnEntity dish, int day, String mealType, List<MonAnEntity> selectedDishes) {
        double score = 10.0; // Base score
        
        // In a real application, we would:
        // 1. Check nutritional balance
        // 2. Consider user preferences
        // 3. Account for seasonal ingredients
        // 4. Consider complementary flavors
        
        // For now, we just penalize dishes that were recently used
        for (int i = 0; i < selectedDishes.size(); i++) {
            MonAnEntity previousDish = selectedDishes.get(i);
            if (previousDish.id() == dish.id()) {
                // Penalize based on recency - more recent repetitions get higher penalties
                double penalty = 5.0 * (i + 1) / selectedDishes.size();
                score -= penalty;
            }
        }
        
        return score;
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
    
    /**
     * Generate a menu for a specific number of days (Legacy method without ingredient filtering)
     * 
     * @param tenThucDon The name of the menu
     * @param soNgay The number of days
     * @param maxBudgetPerMeal Maximum budget per meal
     * @return The ID of the generated menu, or a negative value if failed
     */
    public int generateThucDon(String tenThucDon, int soNgay, Map<String, Double> maxBudgetPerMeal) {
        // Forward to the enhanced version with empty ingredient list
        return generateThucDon(tenThucDon, soNgay, maxBudgetPerMeal, new ArrayList<>());
    }
} 