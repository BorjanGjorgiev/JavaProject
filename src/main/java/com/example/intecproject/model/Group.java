package com.example.intecproject.model;
import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Data
@Entity
@Table(name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    @OneToMany(mappedBy="group",cascade=CascadeType.ALL)
    private List<User> users;
    private LocalDateTime createdAt;
    public Group(String groupName) {
        this.groupName = groupName;
        this.users=new ArrayList<>();
        this.createdAt=LocalDateTime.now();
    }
    public Long getId() {
        return id;
    }
    public void addUser(User u)
    {
        users.add(u);

    }
    public void removeUser(User u)
    {
        users.remove(u);
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
