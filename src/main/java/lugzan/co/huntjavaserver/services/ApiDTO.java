package lugzan.co.huntjavaserver.services;

public class ApiDTO {
    private Object data;
    private Boolean error;
    private Integer status = 200;
    private String errorMessage;
    private String message;

    public ApiDTO() {
    }

    public ApiDTO(Object data) {
        this.data = data;
    }

    public ApiDTO(Object data, Integer status) {
        this.data = data;
        setStatus(status);
        this.setError(this.getErrorByStatus(status));
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    private void setError(Boolean error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        this.setError(this.getErrorByStatus(status));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private Boolean getErrorByStatus(int status) {
        return switch (status) {
            case 400, 401, 500 -> true;
            default -> false;
        };
    }
}
