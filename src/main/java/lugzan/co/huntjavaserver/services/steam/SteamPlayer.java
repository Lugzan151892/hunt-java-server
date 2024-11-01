package lugzan.co.huntjavaserver.services.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamPlayer {
    @JsonProperty("steamid")
    private String steamId;

    @JsonProperty("communityvisibilitystate")
    private Integer communityVisibilityState;

    @JsonProperty("profilestate")
    private Integer profileState;

    @JsonProperty("personaname")
    private String personaName;

    @JsonProperty("profileurl")
    private String profileUrl;

    @JsonProperty("avatarmedium")
    private String avatarMedium;

    @JsonProperty("avatarfull")
    private String avatarFull;

    @JsonProperty("commentpermission")
    private Integer commentPermission;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("lastlogoff")
    private Integer lastlogoff;

    @JsonProperty("primaryclanid")
    private String primaryclanid;

    @JsonProperty("timecreated")
    private Integer timecreated;

    @JsonProperty("personastateflags")
    private Integer personastateflags;

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
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

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

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
}
