package edu.ptithcm.dto.response.error;

import edu.ptithcm.dto.response.base.ResponseDTO;

public class ErrorResponse<T> extends ResponseDTO<T> {
    public ErrorResponse(String message) {
        super("ERROR", message, null);
    }
}