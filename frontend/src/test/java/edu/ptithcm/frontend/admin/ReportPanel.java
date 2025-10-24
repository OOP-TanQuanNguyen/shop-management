package edu.ptithcm.frontend.admin;

import edu.ptithcm.frontend.protocols.DTTP;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class ReportPanel extends JPanel {

    private DTTP client;
    private JTextArea area;

    public ReportPanel(DTTP client) {
        this.client = client;
        setLayout(new BorderLayout(10, 10));

        JButton btnView = new JButton("📈 Xem thống kê");
        btnView.addActionListener(e -> sendReportRequest());
        add(btnView, BorderLayout.NORTH);

        area = new JTextArea("Biểu đồ / dữ liệu thống kê sẽ hiển thị tại đây...");
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    private void sendReportRequest() {
        if (client == null) {
            area.setText("Chế độ offline – thống kê mẫu\nDoanh thu tháng 10: 150 triệu VNĐ");
            return;
        }

        try {
            client.send("REPORT_REQUEST", new HashMap<>(), "REQUEST", "Yêu cầu thống kê doanh thu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
