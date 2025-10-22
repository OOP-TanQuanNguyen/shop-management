package edu.ptithcm.frontend.protocols;
import java.util.Map;

import com.google.gson.Gson;

public class DTTPmsg {
    private String protocol = "DTTP/1.0";
    private String type;
    private String status;
    private String message;
    private Map<String,Object> data;

    public DTTPmsg(String type,Map<String,Object> data,String status,String message){
        this.type = type;
        this.data = data;
        this.status = status;
        this.message = message;
    }
    
    public DTTPmsg(){

    }


    public String toJson(){
        return new Gson().toJson(this);
    }

    public static DTTPmsg fromJson(String json){
        return new Gson().fromJson(json,DTTPmsg.class);
    }

        public boolean isValid() {
        return protocol != null
                && protocol.equals("DTTP/1.0")
                && type != null
                && !type.isEmpty();
    }

    // ==== Getters & Setters ====
    public String getProtocol() { return protocol; }
    public String getType() { return type; }
    public Map<String, Object> getData() { return data; }

    public void setType(String type) { this.type = type; }
    public void setData(Map<String, Object> data) { this.data = data; }


    @Override
    public String toString() {
        return "DTTPmsg{" +
                "protocol='" + protocol + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
