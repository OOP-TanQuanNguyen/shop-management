package edu.ptithcm.dto.response.success;

import edu.ptithcm.dto.response.base.ResponseDTO;

public class DeletedResponse<T> extends ResponseDTO<T> {
    public DeletedResponse(String message,T data){
        super("DELETED", message, data);
    }
}
