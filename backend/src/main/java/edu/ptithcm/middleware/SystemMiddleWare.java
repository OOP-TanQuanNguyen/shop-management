package edu.ptithcm.middleware;

import java.io.IOException;

import edu.ptithcm.protocols.DTTP;

public class SystemMiddleWare {
    public static void replyClientCheck(DTTP server){
        server.on("PING", args -> {
            try {
                server.send("PING_RESPONSE", null, "SUCCESS", "Server online...");
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}
