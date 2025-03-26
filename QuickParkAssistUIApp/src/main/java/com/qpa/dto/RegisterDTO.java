package com.qpa.dto;

import com.qpa.entity.UserType;
<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> e2b4397d59ac67ddd12ae072807174c9f280aff9

public class RegisterDTO {
    private String fullName;
    private String email;
    private String password;
<<<<<<< HEAD
    private String confirmPassword;
    private String username;
    private UserType userType;

=======
    private Set<UserType> roles;
    
>>>>>>> e2b4397d59ac67ddd12ae072807174c9f280aff9
    public RegisterDTO() {
    }
    
    public RegisterDTO(String fullName, String email, String password, String confirmPassword, String username, UserType userType) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userType = userType;
        this.confirmPassword = confirmPassword;
    }

<<<<<<< HEAD
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
=======
	public RegisterDTO(String username, String password, Set<UserType> roles) {
		super();
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Set<UserType> getRoles() { return roles; }
    public void setRoles(Set<UserType> roles) { this.roles = roles; }
}
>>>>>>> e2b4397d59ac67ddd12ae072807174c9f280aff9
