package edu.ptithcm.utils;

import java.text.Normalizer;

public class SearchUtil {
    // số ký tự tối thiểu để cho phép search
    public static final int MIN_SEARCH_LENGTH = 2;

    // giới hạn số lượng kết quả
    public static final int MAX_SEARCH_RESULTS = 50;

    /**
     * Chuẩn hóa chuỗi để tìm kiếm:
     * - trim
     * - lowercase
     * - bỏ dấu tiếng Việt
     */
    public static String normalize(String text) {
        if (text == null) return "";

        // lowercase + trim
        text = text.trim().toLowerCase();

        // remove accents
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        return text.replaceAll("\\p{M}", "");
    }

    /**
     * Kiểm tra keyword có hợp lệ hay không
     */
    public static boolean isKeywordValid(String keyword) {
        return keyword != null
                && !keyword.isBlank()
                && normalize(keyword).length() >= MIN_SEARCH_LENGTH;
    }
}
