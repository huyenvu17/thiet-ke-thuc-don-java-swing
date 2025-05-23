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

    public User(int id, String username, String password, String fullName, String email, String phone, String role) throws BHException {
        setId(id);
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
        setEmail(email);
        setPhone(phone);
        setRole(role);
    }

    public int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public final void setUsername(String username) throws BHException {
        if (username == null || username.trim().isEmpty()) {
            throw new BHException("Username cannot be empty");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public final void setPassword(String password) throws BHException {
        if (password == null || password.trim().isEmpty()) {
            throw new BHException("Password cannot be empty");
        }
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public final void setFullName(String fullName) throws BHException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new BHException("Full name cannot be empty");
        }
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public final void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public final void setRole(String role) throws BHException {
        if (role == null || (!role.equals("admin") && !role.equals("user"))) {
            throw new BHException("Role must be 'admin' or 'user'");
        }
        this.role = role;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        
        String phoneRegex = "^[0-9]+$";
        return phoneNumber.matches(phoneRegex);
    }
} 