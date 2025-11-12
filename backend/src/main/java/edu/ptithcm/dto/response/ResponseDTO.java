package edu.ptithcm.dto.response;

public class ResponseDTO<T> {
    private final String type;
    private final String status;
    private final String message;
    private final T data;

    private ResponseDTO(Builder<T> builder) {
        this.type = builder.type;
        this.status = builder.status;
        this.message = builder.message;
        this.data = builder.data;
    }

    public String getType() { return this.type; }
    public String getStatus() { return this.status; }
    public String getMessage() { return this.message; }
    public T getData() { return this.data; }

    public enum STATUS {
        ERROR("ERROR"),
        SUCCESS("SUCCESS"),
        INVALID("INVALID");

        private final String value;

        STATUS(String value) { this.value = value; }

        public String getValue() { return value; }

        @Override
        public String toString() { return value; }
    }


    public static class Builder<T> {
        private String type;
        private String status;
        private String message;
        private T data;

        public Builder<T> type(String type) {
            this.type = type;
            return this;
        }

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseDTO<T> build() {
            return new ResponseDTO<>(this);
        }
    }
}
