package edu.ptithcm.frontend.admin;

import edu.ptithcm.frontend.protocols.DTTP;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

public class SystemPanel extends JPanel {

    private DTTP client;
    private JFrame parent;

    public SystemPanel(DTTP client, JFrame parent) {
        this.client = client;
        this.parent = parent;
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnBackup = new JButton("💾 Backup DB");
        JButton btnRestore = new JButton("📂 Restore");
        JButton btnConfig = new JButton("⚙️ Config");
        JButton btnLogs = new JButton("📜 Xem Log");
        JButton btnLogout = new JButton("🚪 Đăng xuất");

        ActionListener sendAction = e -> sendCommand(((JButton) e.getSource()).getText());
        btnBackup.addActionListener(sendAction);
        btnRestore.addActionListener(sendAction);
        btnConfig.addActionListener(sendAction);
        btnLogs.addActionListener(sendAction);
        btnLogout.addActionListener(e -> logout());

        add(btnBackup);
        add(btnRestore);
        add(btnConfig);
        add(btnLogs);
        add(btnLogout);
    }

    private void sendCommand(String cmd) {
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Không có kết nối server!");
            return;
        }
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put("command", cmd);
            client.send("SYSTEM_CMD", data, "REQUEST", cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        if (client != null) {
            client.stop();
        }
        parent.dispose();
        new edu.ptithcm.frontend.LoginForm_test().setVisible(true);
    }
}
