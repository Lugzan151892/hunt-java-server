package lugzan.co.huntjavaserver.models.banned_users;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lugzan.co.huntjavaserver.controllers.banned_users.dto.AddBannedRequest;
import lugzan.co.huntjavaserver.models.user.UserModel;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "steam_id", nullable = false)
    private String steamId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private Timestamp updatedAt;

    private String comment = "";

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserModel user;

    /** Не записываем в БД */
    @Transient
    private Integer communityVisibilityState;

    @Transient
    private Integer profileState;

    @Transient
    private String personaName;

    @Transient
    private String profileUrl;

    @Transient
    private String avatarMedium;

    @Transient
    private String avatarFull;

    @Transient
    private Integer commentPermission;

    @Transient
    private String avatar;

    @Transient
    private Integer lastlogoff;

    @Transient
    private String primaryclanid;

    @Transient
    private Integer timecreated;

    @Transient
    private Integer personastateflags;

    @Transient
    private Boolean banned;

    @Transient
    private Integer personastate;

    public BannedUser() {}

    public BannedUser(String steamId, UserModel user) {
        setSteamId(steamId);
        setUser(user);
    }

    public BannedUser(AddBannedRequest request, UserModel user) {
        setSteamId(request.getSteamId());
        setUser(user);
        setComment(request.getComment());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BannedUser that = (BannedUser) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public UserModel getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public void setSteamId(String steam_id) {
        this.steamId = steam_id;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /** Поля не записанные в БД */
    public String getAvatarFull() {
        return avatarFull;
    }

    public void setAvatarFull(String avatarFull) {
        this.avatarFull = avatarFull;
    }

    public String getAvatarMedium() {
        return avatarMedium;
    }

    public void setAvatarMedium(String avatarMedium) {
        this.avatarMedium = avatarMedium;
    }

    public Integer getCommunityVisibilityState() {
        return communityVisibilityState;
    }

    public void setCommunityVisibilityState(Integer communityVisibilityState) {
        this.communityVisibilityState = communityVisibilityState;
    }

    public String getPersonaName() {
        return personaName;
    }
    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Integer getProfileState() {
        return profileState;
    }

    public void setProfileState(Integer profileState) {
        this.profileState = profileState;
    }

    public Integer getCommentPermission() {
        return commentPermission;
    }

    public void setCommentPermission(Integer commentPermission) {
        this.commentPermission = commentPermission;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getLastlogoff() {
        return lastlogoff;
    }

    public void setLastlogoff(Integer lastlogoff) {
        this.lastlogoff = lastlogoff;
    }

    public Integer getPersonastateflags() {
        return personastateflags;
    }

    public void setPersonastateflags(Integer personastateflags) {
        this.personastateflags = personastateflags;
    }

    public String getPrimaryclanid() {
        return primaryclanid;
    }

    public void setPrimaryclanid(String primaryclanid) {
        this.primaryclanid = primaryclanid;
    }

    public Integer getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(Integer timecreated) {
        this.timecreated = timecreated;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getPersonastate() {
        return personastate;
    }

    public void setPersonastate(Integer personastate) {
        this.personastate = personastate;
    }
}
