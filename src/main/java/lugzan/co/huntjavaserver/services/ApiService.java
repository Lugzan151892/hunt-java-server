package lugzan.co.huntjavaserver.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public ResponseEntity<ApiDTO> createSuccessResponse(Object data) {
        this.status = 200;
        
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(data));
    }

    public ResponseEntity<ApiDTO> createErrorResponse(ApiErrorMessageEnums errorType, String value) {
        ApiDTO response = new ApiDTO();
        response.setStatus(this.status);
        response.setErrorMessage(ApiErrorMessages.getErrorMessage(errorType, value));
        return ResponseEntity.status(this.status).body(response);
    }

    public ResponseEntity<ApiDTO> createMessageResponse(String message) {
        ApiDTO response = new ApiDTO();
        response.setStatus(this.status);
        response.setMessage(message);
        return ResponseEntity.status(this.status).body(response);
    }
}