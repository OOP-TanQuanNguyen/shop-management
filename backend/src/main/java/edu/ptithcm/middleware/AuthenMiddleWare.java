package edu.ptithcm.middleware;

import java.io.IOException;

import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class AuthenMiddleWare {

    private AuthenMiddleWare() {
        // private constructor để không thể instantiate
    }

    public static boolean hasPermission(DTTPStateManager manager, DTTP server, String requiredRole,
                                        DTTP.DTTPArgs args, String eventType) throws IOException {
        String username = manager.getUsername(server);

        if (username == null) {
            args.reply(eventType, null, "UNAUTHORIZED", "Bạn cần đăng nhập để thực hiện thao tác này!");
            return false;
        }

        String userRole = (String) manager.getUserMeta(username).get("role");

        System.out.println("UserRole: " + userRole);

        if (userRole == null || !userRole.equalsIgnoreCase(requiredRole)) {
            args.reply(eventType, null, "UNAUTHORIZED", "Bạn không có quyền thực hiện thao tác này!");
            return false;
        }

        return true;
    }

}
