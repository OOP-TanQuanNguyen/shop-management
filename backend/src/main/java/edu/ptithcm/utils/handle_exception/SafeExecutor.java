package edu.ptithcm.utils.handle_exception;

import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.ErrorResponse;

public class SafeExecutor {

    @FunctionalInterface
    public interface Action<T> {
        ResponseDTO<T> execute() throws RuntimeException;
    }

    public static <T> ResponseDTO<T> run(Action<T> action) {
        try {
            return action.execute();
        } catch (RuntimeException e) {
            System.err.println("Lỗi hệ thống: " + e.getMessage());
            return new ErrorResponse<>("Lỗi hệ thống: " + e.getMessage(), null);
        }
    }
}
