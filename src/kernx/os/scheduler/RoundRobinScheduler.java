package kernx.os.scheduler;

import kernx.os.data.PCB;
import java.util.Queue;

public class RoundRobinScheduler implements Scheduler {

    private final int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public PCB selectNextProcess(Queue<PCB> readyQueue) {
        return readyQueue.poll(); // cycle through queue
    }

    @Override
    public String getName() {
        return "Round Robin (Quantum = " + timeQuantum + ")";
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }
}
