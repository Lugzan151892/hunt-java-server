package lugzan.co.huntjavaserver.controllers.users;

import jakarta.servlet.http.HttpServletResponse;
import lugzan.co.huntjavaserver.models.refresh_token.RefreshToken;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.repository.RefreshTokenRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.jwtservice.JwtService;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import org.springframework.security.core.Authentication;

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
    private final static ApiService apiService = new ApiService();

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
        response.setHeader("Set-Cookie", "auth-token=" + refreshToken.getToken() + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");

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
        refreshToken.updateToken(user, user.getUsername());
        refreshTokenRepository.save(refreshToken);

        response.setHeader("Set-Cookie", "auth-token=" + user.getRefreshToken().getToken() + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");

        return apiService.createSuccessResponse(user);
    }

    @GetMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> check () {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            apiService.setStatus(401);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_INCORRECT, "");
        }

        return apiService.createSuccessResponse(user);
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> getUser () {
        UserModel user = null;

        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }
        
        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_EXPIRED, "");
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
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_EXPIRED, "");
        }

        user.setHunt_settings(request.getHunt_settings());
        user.setSpectated_users(request.getSpectated_users());
        userRepository.save(user);

        return apiService.createSuccessResponse(user);
    }
}
