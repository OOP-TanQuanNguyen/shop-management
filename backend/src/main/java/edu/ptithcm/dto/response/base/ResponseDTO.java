package edu.ptithcm.dto.response.base;

public abstract class ResponseDTO<T> {
    protected final String status;    // SUCCESS / ERROR / INVALID / NOT_FOUND
    protected final String message;
    protected final T data;
    
    protected ResponseDTO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
