package edu.ptithcm.utils.handle_exception;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;

import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.ErrorResponse;
import edu.ptithcm.dto.response.error.InvalidResponse;
import jakarta.persistence.PersistenceException;

public class SafeExecutor {

    @FunctionalInterface
    public interface Action<T> {
        ResponseDTO<T> execute() throws Exception;
    }

    public static <T> ResponseDTO<T> run(Action<T> action) {
        try {
            return action.execute();

        } 
        // =========================
        // 1) BẮT TRỰC TIẾP HIBERNATE FK VIOLATION
        // =========================
        catch (ConstraintViolationException e) {
            return new InvalidResponse<>("Dữ liệu đang được liên kết, không thể thao tác.");
        }

        // =========================
        // 2) BẮT LỖI PERSISTENCE (Hibernate WRAP)
        // =========================
        catch (PersistenceException e) {
            Throwable root = unwrap(e);

            // Case 2.1: FK lỗi bên trong
            if (root instanceof ConstraintViolationException) {
                return new InvalidResponse<>("Dữ liệu đang được liên kết, không thể thao tác.");
            }

            // Case 2.2: MySQL lỗi UNIQUE hoặc FK
            if (root instanceof SQLException sqlEx) {
                int code = sqlEx.getErrorCode();

                if (code == 1062) { // duplicate entry
                    return new InvalidResponse<>("Dữ liệu đã tồn tại, vui lòng kiểm tra lại.");
                }

                if (code == 1451 || code == 1452) { 
                    return new InvalidResponse<>("Không thể xoá hoặc sửa vì dữ liệu đang được sử dụng.");
                }

                if (code == 1048) { // column cannot be null
                    return new InvalidResponse<>("Thiếu dữ liệu bắt buộc, vui lòng kiểm tra lại.");
                }
            }

            return new ErrorResponse<>("Có lỗi khi xử lý dữ liệu. Vui lòng kiểm tra lại.");
        }

        // =========================
        // 3) LỖI KHÁC
        // =========================
        catch (Exception e) {
            return new ErrorResponse<>("Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
        }
    }

    // =========================
    // HÀM UNWRAP ROOT CAUSE CHUẨN CHO HIBERNATE 6
    // =========================
    private static Throwable unwrap(Throwable e) {
        Throwable cause = e.getCause();
        if (cause == null) return e;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }
}
