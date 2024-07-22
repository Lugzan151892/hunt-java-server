package lugzan.co.huntjavaserver.models.user;

public class UserResponse {
    private UserModel user;
    private String token;

    public UserResponse(UserModel user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
