package edu.ptithcm.benchmark.protocols;

public class DTTPBandwidthMonitor {

    private long totalReceivedBytes = 0;
    private long totalSentBytes = 0;

    private long receivedBytesThisSecond = 0;
    private long sentBytesThisSecond = 0;

    private long lastCheck = System.nanoTime();

    public synchronized void addReceived(int bytes) {
        totalReceivedBytes += bytes;
        receivedBytesThisSecond += bytes;
        checkBandwidth();
    }

    public synchronized void addSent(int bytes) {
        totalSentBytes += bytes;
        sentBytesThisSecond += bytes;
        checkBandwidth();
    }

    private void checkBandwidth() {
        long now = System.nanoTime();
        long diff = now - lastCheck;

        if (diff >= 1_000_000_000L) {
            double recvMbps = (receivedBytesThisSecond * 8 / 1_000_000.0);
            double sendMbps = (sentBytesThisSecond * 8 / 1_000_000.0);

            System.out.printf(
                "📊 DTTP Bandwidth | Download=%.2f Mbps | Upload=%.2f Mbps\n",
                recvMbps, sendMbps
            );

            receivedBytesThisSecond = 0;
            sentBytesThisSecond = 0;
            lastCheck = now;
        }
    }

    public long getTotalReceivedBytes() {
        return totalReceivedBytes;
    }

    public long getTotalSentBytes() {
        return totalSentBytes;
    }
}
