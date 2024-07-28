package lugzan.co.huntjavaserver.models.user;

import jakarta.persistence.*;
import lugzan.co.huntjavaserver.controllers.users.SignUpRequest;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.banned_users_comments.BannedComment;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="users")
public class UserModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column()
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private List<String> spectated_users = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    @JsonAnySetter
    private Map<String,Object> hunt_settings = new HashMap<>();

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @LastModifiedDate
    @Column
    private LocalDateTime updated_at;

    @Column
    @JsonManagedReference
    @OneToMany(mappedBy = "author")
    private List<BannedComment> banned_comments;

    @ManyToMany(mappedBy = "users")
    private Set<BannedUser> banned_users;

    public UserModel() {}

    public UserModel(SignUpRequest request) {
        setPassword(request.getPassword());
        setUsername(request.getEmail());
        this.created_at = new Date();
        this.updated_at = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public List<String> getSpectated_users() {
        return spectated_users;
    }

    public void setSpectated_users(List<String> spectated_users) {
        this.spectated_users = spectated_users;
    }

    public Map<String, Object> getHunt_settings() {
        return hunt_settings;
    }

    public void setHunt_settings(Map<String, Object> hunt_settings) {
        this.hunt_settings = hunt_settings;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        this.password = bCryptPasswordEncoder.encode(password);
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Boolean checkPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, this.password);
    }
}
