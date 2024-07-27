package lugzan.co.huntjavaserver.models.banned_users_comments;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Entity
@Table(name = "banned_comments")
public class BannedComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text")
    private String text;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private UserModel author;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "banned_user_id", nullable = false)
    private BannedUser banned_user;
}
