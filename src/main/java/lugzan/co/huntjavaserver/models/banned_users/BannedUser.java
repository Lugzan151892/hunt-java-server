package lugzan.co.huntjavaserver.models.banned_users;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lugzan.co.huntjavaserver.models.banned_users_comments.BannedComment;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Entity
@Table(name = "banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "steam_id", nullable = false)
    private Integer steam_id;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "banned_user", cascade = CascadeType.ALL)
    private List<BannedComment> comments;

    @ManyToMany(mappedBy = "banned_users")
    private List<UserModel> users;

    public Long getId() {
        return id;
    }

    public Integer getSteam_id() {
        return steam_id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<BannedComment> getComments() {
        return comments;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setSteam_id(Integer steam_id) {
        this.steam_id = steam_id;
    }

    public void setComments(List<BannedComment> comments) {
        this.comments = comments;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
