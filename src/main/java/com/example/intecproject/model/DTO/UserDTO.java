package com.example.intecproject.model.DTO;

import com.example.intecproject.model.User;

public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;

    public UserDTO() {
        super();
    }

    public UserDTO(Long id, String firstName, String lastName, String login, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.login = user.getEmail();
        return dto;
    }
    @Override
    public String toString() {
        return String.format("%s",login);
    }
}