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
            kernx.os.Kernel.getProcessManager().notify("[SEMAPHORE] PID=" + pcb.getPid() + " waiting on '" + name + "' (value=" + value + ") - Process blocked");
            // The kernel/dispatcher must handle the actual removing from ready queue context switch
            // But state change flags it.
        } else {
            kernx.os.Kernel.getProcessManager().notify("[SEMAPHORE] PID=" + pcb.getPid() + " acquired '" + name + "' (value=" + value + ")");
        }
    }

    public synchronized PCB signal() {
        value++;
        if (value <= 0) {
            if (!waitingQueue.isEmpty()) {
                PCB aroused = waitingQueue.poll();
                aroused.setState(ProcessState.READY);
                kernx.os.Kernel.getProcessManager().notify("[SEMAPHORE] Signal on '" + name + "' (value=" + value + ") - PID=" + aroused.getPid() + " woken up");
                return aroused;
            }
        } else {
            kernx.os.Kernel.getProcessManager().notify("[SEMAPHORE] Signal on '" + name + "' (value=" + value + ")");
        }
        return null;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getValue() { return value; }
    public int getQueueSize() { return waitingQueue.size(); }
}
