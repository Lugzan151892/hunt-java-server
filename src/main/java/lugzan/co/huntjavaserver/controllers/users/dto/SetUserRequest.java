package lugzan.co.huntjavaserver.controllers.users.dto;

import java.util.List;
import java.util.Map;

public class SetUserRequest {
    private Map<String, Object> hunt_settings;
    private List<String> spectated_users;

    public Map<String, Object> getHunt_settings() {
        return hunt_settings;
    }

    public List<String> getSpectated_users() {
        return spectated_users;
    }
}
