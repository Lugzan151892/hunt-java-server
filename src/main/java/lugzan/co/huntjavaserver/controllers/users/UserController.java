package lugzan.co.huntjavaserver.controllers.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.models.user.UserResponse;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import lugzan.co.huntjavaserver.utils.PrivateVariables;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/user", produces = "text/plain;charset=UTF-8")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final static ApiService apiService = new ApiService();

    private static final int millisecondsInHour = 60000 * 60;

    static public Claims getTokenData (String token) {
        return Jwts.parser()
                .verifyWith(PrivateVariables.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    static public String createJwtToken(Object data, String userName) {
        return Jwts.builder()
                .claim("user", data)
                .subject(userName)
                .expiration(new Date(System.currentTimeMillis() + millisecondsInHour * 10))
                .signWith(PrivateVariables.getSecretKey())
                .compact();
    }

    @PostMapping(path="/registration")
    public @ResponseBody ApiDTO addNewUser (@RequestBody SignUpRequest request) {
        UserModel userExistName = userRepository.findByUsername(request.getEmail());

        if (userExistName != null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.EXISTED_USERNAME, request.getEmail());
        }

        UserModel newUser = new UserModel(request);
        userRepository.save(newUser);

        String token = createJwtToken(newUser, request.getEmail());
        UserResponse response = new UserResponse(newUser, token);

        return apiService.createSuccessResponse(response);
    }

    @PostMapping(path = "/login")
    public @ResponseBody ApiDTO login (@RequestBody SignUpRequest request) {
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

        String token = createJwtToken(user, request.getEmail());
        UserResponse response = new UserResponse(user, token);

        return apiService.createSuccessResponse(response);
    }

    @GetMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ApiDTO check (@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_INCORRECT, "");

        }
        String subToken = token.substring(7);
        Claims claims = getTokenData(subToken);

        String userName = claims.getSubject();
        UserModel user = userRepository.findByUsername(userName);

        if (user == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_INCORRECT, "");
        }

        String newToken = createJwtToken(user, userName);
        UserResponse response = new UserResponse(user, newToken);

        return apiService.createSuccessResponse(response);
    }

    @GetMapping(path = "/get")
    public @ResponseBody ApiDTO getUser (@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_INCORRECT, "");
        }
        String subToken = token.substring(7);
        Claims claims = getTokenData(subToken);

        String userName = claims.getSubject();
        UserModel user = userRepository.findByUsername(userName);

        if (user == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, userName);
        }

        String newToken = createJwtToken(user, userName);
        UserResponse response = new UserResponse(user, newToken);

        return apiService.createSuccessResponse(response);
    }

}
