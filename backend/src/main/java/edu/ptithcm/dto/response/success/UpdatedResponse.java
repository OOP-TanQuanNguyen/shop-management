package edu.ptithcm.dto.response.success;
import edu.ptithcm.dto.response.base.ResponseDTO;

public class UpdatedResponse<T> extends ResponseDTO<T> {
    public UpdatedResponse(String message, T data) {
        super("UPDATED", message, data);
    }
}