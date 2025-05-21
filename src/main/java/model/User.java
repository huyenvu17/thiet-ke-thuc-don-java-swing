package model;

import doanthietkethucdon.BHException;

/**
 *
 * @author ADMIN
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    
    /**
     * Constructor with all fields
     */
    public User(int id, String username, String password, String fullName, String email, String phone, String role) throws BHException {
        setId(id);
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setRole(role);
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     * @throws BHException if username is null or empty
     */
    public final void setUsername(String username) throws BHException {
        if (username == null || username.trim().isEmpty()) {
            throw new BHException("Username cannot be empty");
        }
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     * @throws BHException if password is null or empty
     */
    public final void setPassword(String password) throws BHException {
        if (password == null || password.trim().isEmpty()) {
            throw new BHException("Password cannot be empty");
        }
        this.password = password;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     * @throws BHException if fullName is null or empty
     */
    public final void setFullName(String fullName) throws BHException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new BHException("Full name cannot be empty");
        }
        this.fullName = fullName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public final void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public final void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     * @throws BHException if role is invalid
     */
    public final void setRole(String role) throws BHException {
        if (role == null || (!role.equals("admin") && !role.equals("user"))) {
            throw new BHException("Role must be 'admin' or 'user'");
        }
        this.role = role;
    }
    
    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return true; // Optional field
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validate phone number format
     * @param phoneNumber Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true; // Optional field
        }
        
        String phoneRegex = "^[0-9]+$";
        return phoneNumber.matches(phoneRegex);
    }
} 