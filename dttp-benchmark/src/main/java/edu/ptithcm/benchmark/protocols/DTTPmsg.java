package edu.ptithcm.benchmark.protocols;

import java.util.Map;

import com.google.gson.Gson;

public class DTTPmsg {
    private String type;
    private Map<String,Object> data;
    private String status;
    private String message;

    public DTTPmsg(String type, Map<String,Object> data, String status, String message) {
        this.type = type;
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public String getType() { return type; }
    public Map<String,Object> getData() { return data; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }

    public String toJson() { return new Gson().toJson(this); }
    public static DTTPmsg fromJson(String json) { return new Gson().fromJson(json, DTTPmsg.class); }
}
