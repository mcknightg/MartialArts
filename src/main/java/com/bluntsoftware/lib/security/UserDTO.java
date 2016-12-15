package com.bluntsoftware.lib.security;

import java.util.List;

/**
 * Created by Alexander on 7/30/2014.
 */
public class UserDTO {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private String password;
    private String langKey;
    private String theme;
    private String imgSrc;

    public UserDTO(){

    }
    public UserDTO(String login, String firstName, String lastName, String email, List<String> roles) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
    public String getLogin() {
        return login;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getRoles() {
        return roles;
    }
    public String getPassword() { return password;}
    public String getLangKey() {return langKey;}
    public String getTheme() {return theme;}
    public String getImgSrc() {return imgSrc;}

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDTO{");
        sb.append("login='").append(login).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

}