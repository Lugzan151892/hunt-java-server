package lugzan.co.huntjavaserver.services;

public class ApiService {

    private ApiDTO apiDTO;
    private int status = 200;

    public void setStatus(int status) {
        this.status = status;
    }

    private ApiDTO createResponse(Object data) {
        apiDTO = new ApiDTO(data, status);
        return apiDTO;
    }

    public ApiDTO createSuccessResponse(Object data) {
        this.status = 200;
        return createResponse(data);
    }

    public ApiDTO createErrorResponse(ApiErrorMessageEnums errorType, String value) {
        ApiDTO response = new ApiDTO();
        response.setStatus(this.status);
        response.setErrorMessage(ApiErrorMessages.getErrorMessage(errorType, value));
        return response;
    }

    public ApiDTO createMessageResponse(String message) {
        ApiDTO response = new ApiDTO();
        response.setStatus(this.status);
        response.setMessage(message);
        return response;
    }
}