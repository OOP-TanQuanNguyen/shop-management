package edu.ptithcm.utils;

import java.util.concurrent.*;

public class DraftCacheUtil<T> {

    private final ConcurrentHashMap<String, T> draftMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final long timeout; // milliseconds
    private final DraftRollbackHandler<T> rollbackHandler;

    /**
     * @param timeoutMillis thời gian tối đa draft còn hiệu lực
     * @param rollbackHandler callback để rollback khi draft hết hạn
     */
    public DraftCacheUtil(long timeoutMillis, DraftRollbackHandler<T> rollbackHandler) {
        this.timeout = timeoutMillis;
        this.rollbackHandler = rollbackHandler;
    }

    /**
     * Thêm 1 draft vào cache
     */
    public void addDraft(String key, T draft) {
        draftMap.put(key, draft);

        scheduler.schedule(() -> {
            T expired = draftMap.remove(key);
            if (expired != null) {
                rollbackHandler.rollback(expired);
            }
        }, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * Lấy draft theo key (không xóa)
     */
    public T getDraft(String key) {
        return draftMap.get(key);
    }

    /**
     * Lấy và remove draft khi xác nhận thành công
     */
    public T confirmDraft(String key) {
        return draftMap.remove(key);
    }

    /**
     * Interface callback rollback
     */
    public interface DraftRollbackHandler<T> {
        void rollback(T draft);
    }
}
