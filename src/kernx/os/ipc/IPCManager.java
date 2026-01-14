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
    }

    public Message receiveMessage(int receiverPid) {
        Queue<Message> q = messageQueues.get(receiverPid);
        if (q != null && !q.isEmpty()) {
            return q.poll();
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
