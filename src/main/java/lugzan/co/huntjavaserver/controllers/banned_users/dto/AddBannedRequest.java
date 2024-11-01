package lugzan.co.huntjavaserver.controllers.banned_users.dto;

public class AddBannedRequest {
    private String steamId;
    private String comment = "";

    public String getSteamId() {
        return steamId;
    }

    public String getComment() {
        return comment;
    }
}
