package edu.ptithcm.middleware;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;
<<<<<<< HEAD
import edu.ptithcm.views.components.AppMessageBox;
=======
>>>>>>> 179bce8e7583fd747eda0e28b5de8d2397de3efc

public class SystemMiddleWare {
    private static final long PING_INTERVAL = 5000;   // 5 giây gửi 1 lần
    private static final long TIMEOUT_LIMIT = 10000;  // 10 giây không phản hồi => mất kết nối
    private static long lastPongTime = System.currentTimeMillis();

    private static Timer pingTimer;

    public static void start(DTTP client) {
        client.on("PING_RESPONSE", args -> {
            lastPongTime = System.currentTimeMillis();
        });

        // Gửi ping định kỳ
        pingTimer = new Timer(true);
        pingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    long now = System.currentTimeMillis();
                    if (now - lastPongTime > TIMEOUT_LIMIT) {
                        System.err.println("[CHECK] Server không phản hồi ❌");
                        handleServerTimeout(client);
                    } else {
                        client.send("PING", null, "REQUEST", "Heartbeat check");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, PING_INTERVAL);
    }

    private static void handleServerTimeout(DTTP client) {
        try {
            pingTimer.cancel();
            client.stop();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "Mất kết nối đến máy chủ!\nỨng dụng sẽ tự động đăng xuất.",
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                Store.getInstance().dispatch("LOSS_CONNECTION_SERVER", null);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD

    public static void handleDifferenceLogin(DTTP client){
        client.on("FORCE_KICK",args -> {
            SwingUtilities.invokeLater(() -> {
                AppMessageBox.showError(args.message);
            });
            Store.getInstance().dispatch("DOUBLE_CONNECTION", args.message);
        });
    }
=======
>>>>>>> 179bce8e7583fd747eda0e28b5de8d2397de3efc
}
