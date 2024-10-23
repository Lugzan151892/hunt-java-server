package lugzan.co.huntjavaserver.controllers.users;

import java.util.List;

public class SetUserRequest {
    private String hunt_settings;
    private List<String> spectated_users;

    public String getHunt_settings() {
        return hunt_settings;
    }

    public List<String> getSpectated_users() {
        return spectated_users;
    }
}
