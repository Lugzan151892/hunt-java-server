package lugzan.co.huntjavaserver.models.user;

import jakarta.persistence.*;
import lugzan.co.huntjavaserver.controllers.users.SignUpRequest;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(nullable = false, columnDefinition="LONGTEXT")
    private String spectated_users;

    @Column(nullable = false, columnDefinition="LONGTEXT")
    private String hunt_settings;

    @CreatedDate
    @Column(updatable = false)
    private Date created_at;

    @LastModifiedDate
    @Column
    private LocalDateTime updated_at;

    public UserModel() {}

    public UserModel(SignUpRequest request) {
        setPassword(request.getPassword());
        setUsername(request.getEmail());
        setSpectated_users(String.valueOf(new JSONObject()));
        setHunt_settings(String.valueOf(new JSONObject()));
        this.created_at = new Date();
        this.updated_at = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getSpectated_users() {
        return spectated_users;
    }

    public void setSpectated_users(String spectated_users) {
        this.spectated_users = spectated_users;
    }

    public String getHunt_settings() {
        return hunt_settings;
    }

    public void setHunt_settings(String hunt_settings) {
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
