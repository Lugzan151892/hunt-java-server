package lugzan.co.huntjavaserver.models.user;

import jakarta.persistence.*;
import lugzan.co.huntjavaserver.controllers.users.dto.SignUpRequest;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.banned_users_comments.BannedComment;
import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Column(name = "spectated_user")
    private List<String> spectated_users;

    @Lob
    @Column(name = "hunt_settings", columnDefinition = "TEXT")
    @Convert(converter = HuntSettingsConverter.class)
    private Map<String, Object> hunt_settings;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp updated_at;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @ManyToMany
    @JoinTable(
        name = "user_banned_users",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "banned_user_id")
    )
    @JsonBackReference
    private List<BannedUser> banned_users;

    @ManyToMany
    @JoinTable(
        name = "user_banned_comments",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "banned_comment_id")
    )
    private List<BannedComment> bannedComments;

    public UserModel() {}

    public UserModel(SignUpRequest request) {
        setPassword(request.getPassword());
        setUsername(request.getEmail());
    }

    public Long getId() {
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Boolean checkPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, this.password);
    }

    public List<BannedUser> getBanned_users() {
        return banned_users;
    }

    public void setBanned_users(List<BannedUser> banned_users) {
        this.banned_users = banned_users;
    }
}
