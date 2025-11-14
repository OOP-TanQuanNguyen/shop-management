package edu.ptithcm.middleware;

import java.io.IOException;

import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.protocols.DTTP;

public class SystemMiddleWare {

    private SystemMiddleWare() {
    }
    
    public static void replyClientCheck(DTTP server){
        server.on(TypeDTTP.PING.getValue(), args -> {
            try {
                server.send(TypeDTTP.PING_RESPONSE.getValue(), null, "SUCCESS", "Server online...");
            }catch (IOException e){
                System.err.println("Lỗi : "+e.getMessage());
            }
        });
    }
}
