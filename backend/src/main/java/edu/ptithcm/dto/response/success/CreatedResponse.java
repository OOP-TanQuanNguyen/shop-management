package edu.ptithcm.dto.response.success;
import edu.ptithcm.dto.response.base.ResponseDTO;

public class CreatedResponse<T> extends ResponseDTO<T> {
    public CreatedResponse(String message, T data) {
        super("CREATED", message, data);
    }
}