package lugzan.co.huntjavaserver.exception;

import org.jsoup.helper.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lugzan.co.huntjavaserver.services.ApiDTO;
import lugzan.co.huntjavaserver.services.ApiService;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final static ApiService apiService = new ApiService();

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiDTO> handleValidationException(ValidationException ex) {
        apiService.setStatus(500);
        return apiService.createMessageResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDTO> handleGenericException(Exception ex) {
        apiService.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return apiService.createMessageResponse("An unexpected error occurred");
    }
}
