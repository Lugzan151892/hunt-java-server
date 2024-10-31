package lugzan.co.huntjavaserver.services.enviromentvariables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnviromentVariables {
    private static Environment env;

    @Autowired
    public void EnvironmentVariables(Environment environment) {
        env = environment;
    }

    public static String getSteamApiUrl() {
        return env.getProperty("steam.api.url");
    }

    public static String getSteamApiKey() {
        return env.getProperty("steam.api.key");
    }

    public static String getJwtSecretKey() {
        return env.getProperty("jwt.token.secret.key");
    }
}
