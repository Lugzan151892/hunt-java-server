package lugzan.co.huntjavaserver.models.banned_users;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lugzan.co.huntjavaserver.models.banned_users_comments.BannedComment;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Entity
@Table(name = "banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "steam_id", nullable = false)
    private String steamId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp updatedAt;

    @OneToOne(mappedBy = "banned_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BannedComment comment;

    @JsonManagedReference
    @ManyToMany(mappedBy = "banned_users")
    private List<UserModel> users;

    public BannedUser() {}

    public BannedUser(String steamId) {
        setSteamId(steamId);
    }

    public Long getId() {
        return id;
    }

    public String getSteamId() {
        return steamId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public BannedComment getComment() {
        return comment;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setSteamId(String steam_id) {
        this.steamId = steam_id;
    }

    public void setComments(BannedComment comment) {
        this.comment = comment;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
