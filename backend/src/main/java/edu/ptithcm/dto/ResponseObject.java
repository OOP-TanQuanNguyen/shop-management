package edu.ptithcm.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseObject {
    private String status;      // SUCCESS, ERROR
    private String message;     // Thông báo
    private int code;           // Optional code
    private Object data;        // DTO hoặc list DTO

    public ResponseObject() {}

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() { return status; }
    public ResponseObject setStatus(String status) { this.status = status; return this; }

    public String getMessage() { return message; }
    public ResponseObject setMessage(String message) { this.message = message; return this; }

    public int getCode() { return code; }
    public ResponseObject setCode(int code) { this.code = code; return this; }

    public Object getData() { return data; }
    public ResponseObject setData(Object data) { this.data = data; return this; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("code", code);
        map.put("data", data);
        return map;
    }
}
