package lugzan.co.huntjavaserver.models.user.dto;

import lugzan.co.huntjavaserver.models.user.UserModel;

public class UserTokenDto {
    private String username;

    public UserTokenDto(UserModel user) {
        setUsername(user.getUsername());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
