package edu.ptithcm.protocols;

public class DTTPCompressedMsg {
    public boolean compressed;
    public String payload;

    public DTTPCompressedMsg(boolean compressed, String payload) {
        this.compressed = compressed;
        this.payload = payload;
    }
}
