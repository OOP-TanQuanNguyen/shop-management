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
        out.write(text + "\n");
        out.flush();
    }

    public String readJson() throws IOException {
        return in.readLine();
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
