package lugzan.co.huntjavaserver.controllers.users;

import jakarta.servlet.http.HttpServletResponse;
import lugzan.co.huntjavaserver.controllers.users.dto.SetUserRequest;
import lugzan.co.huntjavaserver.controllers.users.dto.SignUpRequest;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.repository.BannedUserRepository;
import lugzan.co.huntjavaserver.repository.RefreshTokenRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.jwtservice.JwtService;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/user", produces = "text/plain;charset=UTF-8")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private BannedUserRepository bannedUserRepository;
    private final static ApiService apiService = new ApiService();

    private UserModel getUserFromAuth() {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        return user;
    }

    @PostMapping(path="/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> addNewUser (@RequestBody SignUpRequest request, HttpServletResponse response) {
        UserModel userExistName = userRepository.findByUsername(request.getEmail());

        if (userExistName != null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.EXISTED_USERNAME, request.getEmail());
        }

        UserModel newUser = new UserModel(request);
        userRepository.save(newUser);
        RefreshToken refreshToken = new RefreshToken(JwtService.createRefreshJwtToken(newUser, newUser.getUsername()), newUser);
        refreshTokenRepository.save(refreshToken);
        String newAccessToken = JwtService.createAccessJwtToken(newUser.getId(), newUser.getUsername());
        response.setHeader("Authorization", newAccessToken);
        response.setHeader("Set-Cookie", "auth-token=" + refreshToken.getToken() + "; HttpOnly; Path=/; Max-Age=604800; SameSite=Lax");

        return apiService.createSuccessResponse(newUser);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> login (@RequestBody SignUpRequest request, HttpServletResponse response) {
        UserModel user = userRepository.findByUsername(request.getEmail());
        if (user == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, request.getEmail());
        }

        Boolean isPasswordsMatch = user.checkPassword(request.getPassword());

        if (!isPasswordsMatch) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.PASSWORD_INCORRECT, request.getEmail());
        }

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken != null) {
            refreshToken.updateToken(user.getId(), user.getUsername());
            refreshTokenRepository.save(refreshToken);
        } else {
            String newToken = JwtService.createRefreshJwtToken(user.getId(), user.getUsername());
            RefreshToken newRefreshToken = new RefreshToken(newToken, user);
            refreshToken = newRefreshToken;
            refreshTokenRepository.save(newRefreshToken);
        }

        String newAccessToken = JwtService.createAccessJwtToken(user.getId(), user.getUsername());
        response.setHeader("Authorization", newAccessToken);
        response.setHeader("Set-Cookie", "auth-token=" + refreshToken.getToken() + "; HttpOnly; Path=/; Max-Age=604800; SameSite=Lax");

        return apiService.createSuccessResponse(user);
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> getUser () {
        UserModel user = getUserFromAuth();
        
        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_EXPIRED, "");
        }

        if (user.getSpectatedUsers() != null && !user.getSpectatedUsers().isEmpty()) {
            for (String steamId : user.getSpectatedUsers()) {
                if (bannedUserRepository.findBySteamIdAndUser(steamId, user).isEmpty()) {
                    BannedUser bannedUser = new BannedUser(steamId, user);

                    bannedUserRepository.save(bannedUser);
                }
            }

            user.setSpectatedUsers(new ArrayList<>());
            userRepository.save(user);
        }

        return apiService.createSuccessResponse(user);
    }

    @PutMapping(path = "/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> setUser (@RequestBody SetUserRequest request) {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            apiService.setStatus(401);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_EXPIRED, "");
        }

        user.setHuntSettings(request.getHunt_settings());
        user.setSpectatedUsers(request.getSpectated_users());
        userRepository.save(user);

        return apiService.createSuccessResponse(user);
    }

    @PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> logoutUser(@RequestBody String userName) {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            user = userRepository.findByUsername(userName);
        }

        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, "");
        }

        user.setRefreshToken(null);
        userRepository.save(user);

        return apiService.createSuccessResponse("");
    }
}
