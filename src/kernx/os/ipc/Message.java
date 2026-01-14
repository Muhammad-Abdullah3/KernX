package kernx.os.ipc;

public class Message {
    private final int senderPid;
    private final int receiverPid;
    private final String content;
    private final long timestamp;

    public Message(int senderPid, int receiverPid, String content) {
        this.senderPid = senderPid;
        this.receiverPid = receiverPid;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public int getSenderPid() { return senderPid; }
    public int getReceiverPid() { return receiverPid; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%d -> %d]: %s", senderPid, receiverPid, content);
    }
}
