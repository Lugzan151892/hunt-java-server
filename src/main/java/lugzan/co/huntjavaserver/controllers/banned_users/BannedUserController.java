package lugzan.co.huntjavaserver.controllers.banned_users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lugzan.co.huntjavaserver.controllers.banned_users.dto.AddBannedRequest;
import lugzan.co.huntjavaserver.controllers.banned_users.dto.DeleteBannedRequest;
import lugzan.co.huntjavaserver.models.banned_users.BannedUser;
import lugzan.co.huntjavaserver.models.user.UserModel;
import lugzan.co.huntjavaserver.repository.BannedUserRepository;
import lugzan.co.huntjavaserver.repository.UserRepository;
import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiErrorMessageEnums;
import lugzan.co.huntjavaserver.services.ApiService;

@Controller
@RequestMapping(path = "/api", produces = "text/plain;charset=UTF-8")
public class BannedUserController {

    @Autowired
    private BannedUserRepository bannedUserRepository;
    @Autowired
    private UserRepository userRepository;
    private final static ApiService apiService = new ApiService();

    @PostMapping(path = "/banned/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> addBannedUser(@RequestBody AddBannedRequest request) {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, "");
        }

        Optional<BannedUser> existedBannedUser = bannedUserRepository.findBySteamIdAndUser(request.getSteamId(), user);

        if (existedBannedUser.isPresent()) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.BANNED_USER_EXIST, "");
        }

        BannedUser bannedUser = new BannedUser(request, user);
        bannedUserRepository.save(bannedUser);

        return apiService.createSuccessResponse(null);
    }

    @Transactional
    @DeleteMapping(path = "/banned/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<ApiDTO> deleteBannedUser(@RequestBody DeleteBannedRequest request) {
        UserModel user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            user = (UserModel) authentication.getPrincipal();
        }

        if (user == null) {
            apiService.setStatus(403);
            return apiService.createErrorResponse(ApiErrorMessageEnums.USER_NOT_FOUND, "");
        }

        Optional<BannedUser> deletedBannedUser = bannedUserRepository.findById(request.getId());

        if (deletedBannedUser.isEmpty()) {
            apiService.setStatus(400);
            return apiService.createErrorResponse(ApiErrorMessageEnums.BANNED_ID_NOT_FOUND, request.getId().toString());
        }

        BannedUser bannedUser = deletedBannedUser.get();

        boolean removed = user.getBannedUsers().removeIf(bu -> bu.getId().equals(bannedUser.getId()));
        if (removed) {
            userRepository.save(user);
            bannedUserRepository.delete(bannedUser);
            return apiService.createSuccessResponse(null);
        } else {
            apiService.setStatus(500);
            return apiService.createErrorResponse(ApiErrorMessageEnums.BANNED_ID_NOT_FOUND, request.getId().toString());
        }
    }

}
