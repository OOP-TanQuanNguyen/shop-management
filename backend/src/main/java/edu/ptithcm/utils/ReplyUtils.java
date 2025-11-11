package edu.ptithcm.utils;

import java.io.IOException;

import edu.ptithcm.protocols.DTTP;
public class ReplyUtils {

    private ReplyUtils(){}

    public static void replyError(DTTP.DTTPArgs args, String eventType, Exception e) {
        try {
            args.reply(eventType, null, "ERROR", "Lỗi server: " + e.getMessage());
        } catch (IOException ignored) {
            System.err.println("Lỗi");
        }
    }
}
