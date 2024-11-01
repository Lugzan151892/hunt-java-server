package lugzan.co.huntjavaserver.services.steam;

import java.util.List;

public class SteamApiResponse {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public static class Response {
        private List<SteamPlayer> players;

        public List<SteamPlayer> getPlayers() {
            return players;
        }

        public void setPlayers(List<SteamPlayer> players) {
            this.players = players;
        }
    }
}
