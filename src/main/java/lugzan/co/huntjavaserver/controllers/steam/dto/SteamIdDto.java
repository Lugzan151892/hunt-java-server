package lugzan.co.huntjavaserver.controllers.steam.dto;

public class SteamIdDto {

    private String steamId;

    public SteamIdDto() {}

    public SteamIdDto(String steamId) {
        setSteamId(steamId);
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }
}
