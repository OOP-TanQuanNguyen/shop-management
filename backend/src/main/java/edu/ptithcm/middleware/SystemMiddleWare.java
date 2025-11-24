package edu.ptithcm.middleware;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class SystemMiddleWare {

    private final DTTP server;
    private final DTTPStateManager manager;

    private long lastPong = System.currentTimeMillis();
    private static final long TIMEOUT = 10_000; // 10 seconds

    public SystemMiddleWare(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
    }

    public void check() throws IOException {
        server.send(TypeDTTP.PING.getValue(), null, "REQUEST", "");
        System.out.println("PING TO CLIENT : ");
    }

    public void init() {

        // PING_RESPONSE Handler
        server.on(TypeDTTP.PING_RESPONSE.getValue(), args -> {
            lastPong = System.currentTimeMillis();
        });

        // Start timeout monitor
        startTimerChecker();

        // initial ping
        try {
            check();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTimerChecker() {
        Timer timer = new Timer(true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    long diff = System.currentTimeMillis() - lastPong;

                    if (diff > TIMEOUT) {
                        System.out.println("PING TIMEOUT → disconnect client");
                        handleTimeout();
                        return;
                    }

                    check();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100000); // send PING every 3 sec
    }

    private void handleTimeout() {
        manager.removeConnection(this.server);
        server.stop();
    }
}
