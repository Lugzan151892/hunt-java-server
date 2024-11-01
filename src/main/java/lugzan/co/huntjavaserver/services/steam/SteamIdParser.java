package lugzan.co.huntjavaserver.services.steam;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamIdParser {
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
}
