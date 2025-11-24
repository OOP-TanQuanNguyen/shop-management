package edu.ptithcm.utils;

import edu.ptithcm.protocols.DTTP;
public class ReplyUtils {

    private ReplyUtils(){}

    public static void replyError(DTTP.DTTPArgs args, String eventType, Exception e) {
        args.reply(eventType, null, "ERROR", "Lỗi server: " + e.getMessage());
    }
}
