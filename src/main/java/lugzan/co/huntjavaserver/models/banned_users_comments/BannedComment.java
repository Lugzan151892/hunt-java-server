package lugzan.co.huntjavaserver.models.banned_users_comments;

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
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserModel author;

    @ManyToOne
    @JoinColumn(name = "banned_user_id", nullable = false)
    private BannedUser banned_user;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public UserModel getAuthor() {
        return author;
    }

    public BannedUser getBanned_user() {
        return banned_user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public void setBanned_user(BannedUser banned_user) {
        this.banned_user = banned_user;
    }
}
