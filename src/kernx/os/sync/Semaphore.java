package kernx.os.sync;

import kernx.os.data.PCB;
import kernx.os.data.ProcessState;
import java.util.LinkedList;
import java.util.Queue;

public class Semaphore {
    private int value;
    private final int id;
    private final String name;
    private final Queue<PCB> waitingQueue;

    public Semaphore(int id, String name, int initialValue) {
        this.id = id;
        this.name = name;
        this.value = initialValue;
        this.waitingQueue = new LinkedList<>();
    }

    public synchronized void wait(PCB pcb) {
        value--;
        if (value < 0) {
            waitingQueue.add(pcb);
            pcb.setState(ProcessState.BLOCKED);
            // The kernel/dispatcher must handle the actual removing from ready queue context switch
            // But state change flags it.
        }
    }

    public synchronized PCB signal() {
        value++;
        if (value <= 0) {
            if (!waitingQueue.isEmpty()) {
                PCB aroused = waitingQueue.poll();
                aroused.setState(ProcessState.READY);
                return aroused;
            }
        }
        return null;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getValue() { return value; }
    public int getQueueSize() { return waitingQueue.size(); }
}
