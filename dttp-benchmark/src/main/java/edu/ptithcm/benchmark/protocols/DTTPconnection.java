package edu.ptithcm.benchmark.protocols;

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
    private final DTTPBandwidthMonitor monitor;

    public DTTPconnection(Socket socket, DTTPBandwidthMonitor monitor) throws IOException {
        this.socket = socket;
        this.monitor = monitor;

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
        monitor.addSent(raw.length);

        out.write(text);
        out.write("\n");
        out.flush();
    }

    /** Nhận dữ liệu – GIỮ NGUYÊN API */
    public String readJson() throws IOException {
        String line = in.readLine();
        if (line != null) {
            monitor.addReceived(line.getBytes(StandardCharsets.UTF_8).length);
        }
        return line;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public void close() {
        try { socket.close(); } catch (IOException ignored) {}
    }
}
