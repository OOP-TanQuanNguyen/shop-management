package edu.ptithcm.services.authentication;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import edu.ptithcm.app.actions.AuthAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.protocols.DTTP;

public class AuthService {
    private final DTTP client;
    private final Store store = Store.getInstance();

    public AuthService(DTTP client) throws IOException {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() throws IOException {
        // Lắng nghe phản hồi LOGIN từ server
        client.on("LOGIN", args -> {
            if ("SUCCESS".equals(args.status)) {
                Map<String, Object> data = (Map<String, Object>) args.data;

                Object branchIdObj = data.get("branch_id");
                int branchId = 0;
                if (branchIdObj instanceof Number) {
                    branchId = ((Number) branchIdObj).intValue();
                }

                UserModel user = new UserModel.Builder()
                    .id((String) data.get("id"))
                    .branch((String) data.get("branch"))
                    .branchId(branchId)
                    .phone((String) data.get("phone"))
                    .role((String) data.get("role"))
                    .username((String) data.get("username"))
                    .build();

                store.dispatch(AuthAction.LOGIN_SUCCESS, user);
            } else {
                store.dispatch(AuthAction.LOGIN_FAIL, args.message);
            }
        });
    }

    public void login(String username, String password) throws IOException {
        System.out.println("Username : "+username);
        System.out.println("Password : "+password);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        LocalTime now = LocalTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("⏱ Time send login = " + time);
        client.send("LOGIN", data, "REQUEST", "Login request");

        // Lưu timestamp vào store để callback đọc lạiz
    }
}
