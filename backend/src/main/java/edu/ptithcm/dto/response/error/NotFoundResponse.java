package edu.ptithcm.dto.response.error;
import edu.ptithcm.dto.response.base.ResponseDTO;

public class NotFoundResponse<T> extends ResponseDTO<T> {
    public NotFoundResponse(String message) {
        super("NOT_FOUND", message, null);
    }
}