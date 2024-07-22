package lugzan.co.huntjavaserver.services;

import org.json.JSONObject;

public class ApiService {

    private int status = 200;

    public void setStatus(int status) {
        this.status = status;
    }

    private Boolean getErrorByStatus(int status) {
        return switch (status) {
            case 400, 401, 500 -> true;
            default -> false;
        };
    }

    private JSONObject createResponse(Object data) {
        JSONObject response = new JSONObject();
        response.put("error", getErrorByStatus(this.status));
        response.put("status", this.status);
        response.put("data", new JSONObject(data));
        return response;
    }

    public String createSuccessResponse(Object data) {
        return createResponse(data).toString();
    }

    public String createSuccessResponse(Object data, String accessToken) {
        this.status = 200;
        JSONObject response = createResponse(data);
        response.put("accessToken", accessToken);
        return response.toString();
    }

    public String createErrorResponse(ApiErrorMessageEnums errorType, String value) {
        JSONObject response = new JSONObject();
        response.put("error", getErrorByStatus(this.status));
        response.put("status", this.status);
        response.put("errorMessage", ApiErrorMessages.getErrorMessage(errorType, value));

        return response.toString();
    }

    public String createMessageResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("error", getErrorByStatus(this.status));
        response.put("status", this.status);
        response.put("message", message);

        return response.toString();
    }
}