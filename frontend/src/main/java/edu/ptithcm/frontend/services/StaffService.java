package edu.ptithcm.frontend.services;

import edu.ptithcm.frontend.protocols.DTTP;
import java.util.*;
import java.util.function.BiConsumer;

public class StaffService {

    private final DTTP client;
    private final BiConsumer<String, Map<String, Object>> callback;

    public StaffService(DTTP client, BiConsumer<String, Map<String, Object>> callback) {
        this.client = client;
        this.callback = callback;

        if (client != null) {
            client.on("INVOICE_LIST", data -> callback.accept("INVOICE_LIST", data));
            client.on("PING_RESPONSE", data -> callback.accept("PING_RESPONSE", data));
            client.listen();
        }
    }

    // Dữ liệu demo cho hóa đơn (Invoice)
    private List<Map<String, Object>> getDemoInvoices() {
        return List.of(
                Map.of("id", "HD001 (DEMO)", "customer", "Nguyễn Văn B", "total", "2,000,000", "status", "Đang xử lý", "date", "2025-10-25"),
                Map.of("id", "HD002 (DEMO)", "customer", "Trần Thị C", "total", "5,000,000", "status", "Hoàn thành", "date", "2025-10-23")
        );
    }

    public void getInvoices() {
        if (client == null) {
            // Dữ liệu demo
            callback.accept("INVOICE_LIST", Map.of("data", getDemoInvoices()));
            return;
        }

        try {
            client.send("GET_INVOICES", Map.of(), "REQ", "Yêu cầu danh sách hóa đơn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ping() {
        if (client == null) {
            callback.accept("PING_RESPONSE", Map.of("msg", "Server [OFFLINE]. Đang ở chế độ DEMO."));
            return;
        }
        try {
            client.send("PING", Map.of("from", "STAFF"), "REQ", "Ping test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
