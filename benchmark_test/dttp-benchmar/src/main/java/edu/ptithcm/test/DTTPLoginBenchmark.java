package edu.ptithcm.test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.RateLimiter;

import edu.ptithcm.test.protocols.DTTP;
import edu.ptithcm.test.protocols.DTTP.DTTPArgs;

/**
 * Benchmark thực tế mô phỏng LOGIN qua DTTP protocol.
 * Có giới hạn tốc độ gửi request (RateLimiter) + độ trễ ngẫu nhiên giữa các request.
 */
public class DTTPLoginBenchmark {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2025;

    // === CẤU HÌNH BENCHMARK ===
    private static final int THREADS = 50;            // số client song song (giả lập POS)
    private static final int REQUESTS_PER_CLIENT = 1000; // số request mỗi POS gửi
    private static final double MAX_RPS = 60.0;       // Giới hạn tốc độ tổng (request/giây)
    private static final int MIN_DELAY_MS = 100;      // Độ trễ ngẫu nhiên giữa 2 request (ms)
    private static final int MAX_DELAY_MS = 500;

    private static final List<Long> latencies = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger success = new AtomicInteger();
    private static final AtomicInteger fail = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        System.out.printf("🚀 Realistic Benchmarking LOGIN @ %s:%d%n", HOST, PORT);
        System.out.printf("Threads=%d | Req/client=%d | RPS limit=%.1f%n", THREADS, REQUESTS_PER_CLIENT, MAX_RPS);

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        RateLimiter limiter = RateLimiter.create(MAX_RPS); // ✅ Giới hạn tốc độ gửi

        long globalStart = System.currentTimeMillis();

        for (int i = 0; i < THREADS; i++) {
            final int clientId = i;
            pool.submit(() -> runClient(clientId, limiter));
            Thread.sleep(20); // khởi động lệch nhịp giữa các client
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.MINUTES);

        long totalTime = System.currentTimeMillis() - globalStart;
        printSummary(totalTime);
    }

    private static void runClient(int clientId, RateLimiter limiter) {
        Random random = new Random();

        try {
            DTTP client = new DTTP(HOST, PORT);

            // Thread lắng nghe phản hồi
            new Thread(() -> {
                try {
                    client.listen();
                } catch (Exception e) {
                    System.err.printf("[CLIENT-%d] ❌ Listener error: %s%n", clientId, e.getMessage());
                }
            }, "DTTP-Listener-" + clientId).start();

            // Route LOGIN
            client.on("LOGIN", (DTTPArgs args) -> {
                if ("SUCCESS".equalsIgnoreCase(args.status)) success.incrementAndGet();
                else fail.incrementAndGet();
            });

            // Gửi request có kiểm soát tốc độ
            for (int i = 0; i < REQUESTS_PER_CLIENT; i++) {
                limiter.acquire(); // ✅ chờ slot rảnh theo tốc độ RPS

                long start = System.nanoTime();

                Map<String, Object> data = new HashMap<>();
                data.put("username", "admin");
                data.put("password", "admin123");

                client.send("LOGIN", data, "", "");

                // Giả lập người dùng thao tác (100–400 ms)
                Thread.sleep(MIN_DELAY_MS + random.nextInt(MAX_DELAY_MS - MIN_DELAY_MS + 1));

                long latency = (System.nanoTime() - start) / 1_000_000;
                latencies.add(latency);
            }

            client.stop();
            System.out.printf("[CLIENT-%d] ✅ Done (%d/%d)%n", clientId, success.get(), THREADS * REQUESTS_PER_CLIENT);
        } catch (Exception e) {
            System.err.printf("[CLIENT-%d] ❌ Error: %s%n", clientId, e.getMessage());
        }
    }

    private static void printSummary(long totalTime) {
        System.out.println("\n✅ === LOGIN BENCHMARK RESULT ===");
        System.out.printf("Threads: %d%n", THREADS);
        System.out.printf("Requests per thread: %d%n", REQUESTS_PER_CLIENT);
        System.out.printf("Total sent: %d%n", THREADS * REQUESTS_PER_CLIENT);
        System.out.printf("Success: %d, Fail: %d%n", success.get(), fail.get());
        System.out.printf("Received responses: %d%n", latencies.size());
        System.out.printf("Total elapsed: %.2f s%n", totalTime / 1000.0);

        if (!latencies.isEmpty()) {
            List<Long> sorted = latencies.stream().sorted().collect(Collectors.toList());
            long p50 = sorted.get((int) (sorted.size() * 0.50));
            long p95 = sorted.get((int) (sorted.size() * 0.95));
            long p99 = sorted.get((int) (sorted.size() * 0.99));

            System.out.printf("P50: %d ms | P95: %d ms | P99: %d ms%n", p50, p95, p99);
            exportCSV(sorted);
        } else {
            System.out.println("⚠️ Không có dữ liệu latency (client chưa bắt được phản hồi).");
        }
    }

    private static void exportCSV(List<Long> sorted) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("target/login_benchmark_realistic.csv"))) {
            writer.println("latency_ms");
            for (Long v : sorted) writer.println(v);
            System.out.println("📊 Đã xuất dữ liệu: target/login_benchmark_realistic.csv (" + sorted.size() + " dòng)");
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi xuất CSV: " + e.getMessage());
        }
    }
}
