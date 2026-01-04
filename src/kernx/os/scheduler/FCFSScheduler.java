package kernx.os.scheduler;

import kernx.os.data.PCB;
import java.util.Queue;

public class FCFSScheduler implements Scheduler {

    @Override
    public PCB selectNextProcess(Queue<PCB> readyQueue) {
        return readyQueue.poll(); // FIFO
    }

    @Override
    public String getName() {
        return "First Come First Serve (FCFS)";
    }
}
