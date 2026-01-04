package kernx.os.scheduler;

import kernx.os.data.PCB;
import java.util.Queue;

public interface Scheduler {

    PCB selectNextProcess(Queue<PCB> readyQueue);

    String getName();
}
