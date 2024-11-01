package lugzan.co.huntjavaserver.controllers.steam;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lugzan.co.huntjavaserver.controllers.steam.dto.SteamIdDto;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.steam.SteamIdParser;

@Controller
@RequestMapping(path = "/api/steam", produces = "text/plain;charset=UTF-8")
public class SteamController {

    private final static ApiService apiService = new ApiService();

    @GetMapping(path = "/steamid/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> getUserBySteamid(@RequestParam("path") String path) {
        if (path == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_PATH_INVALID, path);
        }

        String steamId = SteamIdParser.parseSteamIdFromHtml(path);

        if (steamId == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_ID_FINDER_FAILED, path);
        }

        return apiService.createSuccessResponse(new SteamIdDto(steamId));
    }

    // @GetMapping(path = "/steam/list", produces = MediaType.APPLICATION_JSON_VALUE)
    // public @ResponseBody ResponseEntity<ApiDTO> getBannedUsersInfo() {
    //     if (path == null) {
    //         apiService.setStatus(400);
    //         return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_PATH_INVALID, path);
    //     }

    //     String steamId = SteamIdParser.parseSteamIdFromHtml(path);

    //     if (steamId == null) {
    //         apiService.setStatus(400);
    //         return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_ID_FINDER_FAILED, path);
    //     }

    //     return apiService.createSuccessResponse(steamId);
    // }
}
