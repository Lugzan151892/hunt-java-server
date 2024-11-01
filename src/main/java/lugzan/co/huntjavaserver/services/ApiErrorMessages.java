package lugzan.co.huntjavaserver.services;

public class ApiErrorMessages {

    public static String getErrorMessage(ApiErrorMessageEnums type, String value) {
        switch (type) {
            case EXISTED_EMAIL -> {
                return "User with email " + value + " already exist";
            }
            case EXISTED_USERNAME -> {
                return "User with username " + value + " already exist";
            }
            case USER_NOT_FOUND -> {
                return "User with username " + value + " not found";
            }
            case TOKEN_EXPIRED -> {
                return "Token expired";
            }
            case PASSWORD_INCORRECT -> {
                return "Password incorrect";
            }
            case TOKEN_INCORRECT -> {
                return "Token incorrect";
            }
            case STEAM_PATH_INVALID -> {
                return "Steam path invalid";
            }
            case STEAM_ID_FINDER_FAILED -> {
                return "Steam id with this path not found. Check path and try again";
            }
            default -> {
                return "Unexpected error occurred. Please try again.";
            }
        }
    }
}
