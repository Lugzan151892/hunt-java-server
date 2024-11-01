package lugzan.co.huntjavaserver.controllers.steam;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import lugzan.co.huntjavaserver.controllers.steam.dto.SteamIdDto;
import lugzan.co.huntjavaserver.controllers.steam.dto.SteamListResponse;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.steam.SteamPlayer;
import lugzan.co.huntjavaserver.services.steam.SteamService;

@Controller
@RequestMapping(path = "/api/steam", produces = "text/plain;charset=UTF-8")
public class SteamController {

    private final static ApiService apiService = new ApiService();

    private final RestTemplate restTemplate;

    @Autowired
    public SteamController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "/steamid/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> getUserBySteamid(@RequestParam("path") String path) {
        if (path == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_PATH_INVALID, path);
        }

        String steamId = SteamService.parseSteamIdFromHtml(path);

        if (steamId == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.STEAM_ID_FINDER_FAILED, path);
        }

        return apiService.createSuccessResponse(new SteamIdDto(steamId));
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> getBannedUsersInfo() {

        UserModel user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, "");
        }

        List<String> bannedIds = user.getBannedUsers().stream().map(BannedUser::getSteamId).collect(Collectors.toList());
        List<SteamPlayer> playersData = SteamService.getSteamUserData(bannedIds, restTemplate);
        List<BannedUser> bannedUsers = user.getBannedUsers();

        for (BannedUser bannedUser : bannedUsers) {
            for (SteamPlayer player : playersData) {
                if (bannedUser.getSteamId().equals(player.getSteamId())) {
                    bannedUser.setPersonaName(player.getPersonaName());
                    bannedUser.setProfileUrl(player.getProfileUrl());
                    bannedUser.setAvatarFull(player.getAvatarFull());
                    bannedUser.setAvatarMedium(player.getAvatarMedium());
                    bannedUser.setProfileState(player.getProfileState());
                    bannedUser.setCommunityVisibilityState(player.getCommunityVisibilityState());
                    bannedUser.setCommentPermission(player.getCommentPermission());
                    bannedUser.setAvatar(player.getAvatar());
                    bannedUser.setLastlogoff(player.getLastlogoff());
                    bannedUser.setPrimaryclanid(player.getPrimaryclanid());
                    bannedUser.setTimecreated(player.getTimecreated());
                    bannedUser.setPersonastateflags(player.getPersonastateflags());
                }
            }
        }

        SteamListResponse response = new SteamListResponse(bannedUsers);

        return apiService.createSuccessResponse(response);
    }
}
