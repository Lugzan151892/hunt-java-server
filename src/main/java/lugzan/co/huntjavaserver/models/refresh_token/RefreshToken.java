package lugzan.co.huntjavaserver.models.refresh_token;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.services.jwtservice.JwtService;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 4096)
    private String token;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    public RefreshToken() {}

    public RefreshToken(String token, UserModel user) {
        this.token = token;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void updateToken(Object data, String userName) {
        String token = JwtService.createRefreshJwtToken(data, userName);

        setToken(token);
    }
}
