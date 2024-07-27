package lugzan.co.huntjavaserver.models.banned_users;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lugzan.co.huntjavaserver.models.banned_users_comments.BannedComment;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Entity
@Table(name="banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "steam_id")
    private Integer steam_id;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private Date created_at;

    @Column
    @JsonManagedReference
    @OneToMany(mappedBy = "banned_user")
    private Set<BannedComment> comments;

    @ManyToMany
    @JoinTable(
        name = "users", 
        joinColumns = @JoinColumn(name = "banned_id"), 
        inverseJoinColumns = @JoinColumn(name = "comment_id"))
    Set<UserModel> users;
}
