package edu.ptithcm.dto.response.error;
import edu.ptithcm.dto.response.base.ResponseDTO;

public class InvalidResponse<T> extends ResponseDTO<T> {
    public InvalidResponse(String message) {
        super("INVALID", message, null);
    }
}