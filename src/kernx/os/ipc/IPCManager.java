package kernx.os.ipc;

import java.util.*;

public class IPCManager {
    private static IPCManager instance;
    private final Map<Integer, Queue<Message>> messageQueues;

    private IPCManager() {
        messageQueues = new HashMap<>();
    }

    public static IPCManager getInstance() {
        if (instance == null) {
            instance = new IPCManager();
        }
        return instance;
    }

    public void sendMessage(Message msg) {
        messageQueues.computeIfAbsent(msg.getReceiverPid(), k -> new LinkedList<>()).add(msg);
        kernx.os.Kernel.getProcessManager().notify("[IPC] Message sent from PID=" + msg.getSenderPid() + " to PID=" + msg.getReceiverPid() + ": \"" + msg.getContent() + "\"");
    }

    public Message receiveMessage(int receiverPid) {
        Queue<Message> q = messageQueues.get(receiverPid);
        if (q != null && !q.isEmpty()) {
            Message msg = q.poll();
            kernx.os.Kernel.getProcessManager().notify("[IPC] PID=" + receiverPid + " received message from PID=" + msg.getSenderPid() + ": \"" + msg.getContent() + "\"");
            return msg;
        }
        return null;
    }
    
    public List<Message> peekAllMessages(int receiverPid) {
         Queue<Message> q = messageQueues.get(receiverPid);
         if (q == null) return Collections.emptyList();
         return new ArrayList<>(q);
    }

    public void clearMessagesForProcess(int pid) {
        messageQueues.remove(pid);
    }
}
