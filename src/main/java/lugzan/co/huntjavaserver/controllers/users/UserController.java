package lugzan.co.huntjavaserver.controllers.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.ApiService;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import lugzan.co.huntjavaserver.utils.PrivateVariables;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping(path = "/api/user", produces = "text/plain;charset=UTF-8")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final static ApiService apiService = new ApiService();

    static public String createJwtToken(Object data, String userName) {
        return Jwts.builder()
                .claim("user", data)
                .subject(userName)
                .signWith(PrivateVariables.getSecretKey())
                .compact();
    }

    @PostMapping(path="/registration")
    public @ResponseBody String addNewUser (@RequestBody SignUpRequest request) {
        UserModel userExistName = userRepository.findByUsername(request.getEmail());

        if (userExistName != null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.EXISTED_USERNAME, request.getEmail());
        }

        UserModel newUser = new UserModel(request);
        userRepository.save(newUser);

        String token = createJwtToken(newUser, request.getEmail());

        JSONObject response = new JSONObject();
        response.put("user", newUser);
        response.put("token", token);

        return response.toString();
    }

    @PostMapping(path = "/login")
    public @ResponseBody String login (@RequestBody SignUpRequest request) {
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
        JSONObject response = new JSONObject();
        response.put("user", user);
        response.put("token", token);

        return response.toString();
    }

    @GetMapping(path = "/auth")
    public @ResponseBody String check (@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.TOKEN_INCORRECT, "");
        }
        String subToken = token.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(PrivateVariables.getSecretKey())
                .build()
                .parseSignedClaims(subToken)
                .getPayload();

        String userName = claims.getSubject();
        UserModel user = userRepository.findByUsername(userName);

        if (user == null) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, userName);
        }

        String newToken = createJwtToken(user, userName);
        JSONObject response = new JSONObject();
        response.put("user", user);
        response.put("token", token);

        return response.toString();
    }

}
