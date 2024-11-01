package lugzan.co.huntjavaserver.services.steam;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lugzan.co.huntjavaserver.services.enviromentvariables.EnviromentVariables;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SteamService {
    public static String parseSteamIdFromHtml(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String html = doc.html();
            
            Pattern pattern = Pattern.compile("\"steamid\":\"(\\d+)\"");
            Matcher matcher = pattern.matcher(html);
            
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                System.out.println("steamid not found");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SteamPlayer> getSteamUserData(List<String> steamIds, RestTemplate restTemplate) {
        String steamIdList = String.join(",", steamIds.stream().map(String::valueOf).toArray(String[]::new));
        String apiUrl = EnviromentVariables.getSteamApiUrl() + "?key=" + EnviromentVariables.getSteamApiKey() + "&steamids=" + steamIdList;

        try {
            String response = restTemplate.getForObject(apiUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            SteamApiResponse steamApiResponse = objectMapper.readValue(response, SteamApiResponse.class);

            List<SteamPlayer> players = steamApiResponse.getResponse().getPlayers();
            
            return players;
        } catch (RestClientException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
