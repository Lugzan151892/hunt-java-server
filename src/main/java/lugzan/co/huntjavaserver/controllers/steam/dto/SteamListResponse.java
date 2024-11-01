package lugzan.co.huntjavaserver.controllers.steam.dto;

import java.util.List;

import lugzan.co.huntjavaserver.models.banned_users.BannedUser;

public class SteamListResponse {

    private List<BannedUser> bannedUsers;

    public SteamListResponse() {
    }

    public SteamListResponse(List<BannedUser> users) {
        setBannedUsers(users);
    }

    public List<BannedUser> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(List<BannedUser> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
