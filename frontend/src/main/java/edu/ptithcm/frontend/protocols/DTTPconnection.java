package edu.ptithcm.frontend.protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DTTPconnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public DTTPconnection(Socket socket) throws IOException  {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }
    

    public void send(String json) throws IOException{
        if (socket.isClosed()) return;
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String len  = String.format("%05d",bytes.length);
        out.write(len.getBytes(StandardCharsets.UTF_8));
        out.write(bytes);
        out.flush();
    }
    
    public String readJson() throws IOException{
        byte[] lenghBytes = in.readNBytes(5);
        if (lenghBytes.length < 5) return null;
        int length = Integer.parseInt(new  String(lenghBytes,StandardCharsets.UTF_8));
        byte[] bytes = in.readNBytes(length);
        if (bytes.length < length) return null;
        return new String(bytes,StandardCharsets.UTF_8);  
    }

    public Socket getConnection(){
        return this.socket;
    }

    public boolean isActive(){
        return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
    }

    public void close(){
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("[⚠] Error closing: " + e.getMessage());
        }
    }
}
