package edu.ptithcm.dto.response.success;
import edu.ptithcm.dto.response.base.ResponseDTO;

public class SuccessResponse<T> extends ResponseDTO<T> {
    public SuccessResponse(String message, T data) {
        super("SUCCESS", message, data);
    }
}