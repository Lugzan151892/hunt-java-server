package lugzan.co.huntjavaserver.models.user;

import jakarta.persistence.*;
import lugzan.co.huntjavaserver.controllers.users.dto.SignUpRequest;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private List<String> spectatedUsers;

    @Lob
    @Column(name = "hunt_settings", columnDefinition = "TEXT")
    @Convert(converter = HuntSettingsConverter.class)
    private Map<String, Object> huntSettings;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BannedUser> bannedUsers;

    public UserModel() {}

    public UserModel(SignUpRequest request) {
        setPassword(request.getPassword());
        setUsername(request.getEmail());
    }

    public Long getId() {
        return id;
    }

    public List<String> getSpectatedUsers() {
        return spectatedUsers;
    }

    public void setSpectatedUsers(List<String> spectatedUsers) {
        this.spectatedUsers = spectatedUsers;
    }

    public Map<String, Object> getHuntSettings() {
        return huntSettings;
    }

    public void setHuntSettings(Map<String, Object> huntSettings) {
        this.huntSettings = huntSettings;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
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

    public List<BannedUser> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<BannedUser> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
