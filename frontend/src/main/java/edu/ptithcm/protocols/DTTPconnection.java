package edu.ptithcm.protocols;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DTTPconnection {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public DTTPconnection(Socket socket) throws IOException {
        this.socket = socket;

        this.in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
        );

        this.out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
        );
    }

    /** Gửi dữ liệu – GIỮ NGUYÊN API */
    public synchronized void send(String text) throws IOException {
        byte[] raw = text.getBytes(StandardCharsets.UTF_8);

        out.write(text);
        out.write("\n");
        out.flush();
    }

    /** Nhận dữ liệu – GIỮ NGUYÊN API */
    public String readJson() throws IOException {
        String line = in.readLine();
        return line;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
