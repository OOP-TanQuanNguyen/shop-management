package edu.ptithcm.protocols;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DTTPconnection {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public DTTPconnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void send(String text) throws IOException {
        System.out.println("Text (DTTPConnection) : " + text);
        out.write(text + "\n");
        out.flush();
        System.out.println("✅ SENT SUCCESSFULLY to " + getAddress());
    }

    public String readJson() throws IOException {
        String line = in.readLine();
        // if (line == null) {
        //     System.out.println("[DEBUG] readLine() trả về null (client đóng kết nối)");
        // } else {
        //     System.out.println("[DEBUG] Nhận được: " + line);
        // }
        return line;
    }


    public String getAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
