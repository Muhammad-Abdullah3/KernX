package kernx.os.scheduler;

import kernx.os.data.PCB;
import kernx.os.data.ProcessState;

public class Dispatcher {

    public void dispatch(PCB pcb) {
        pcb.setState(ProcessState.RUNNING);
        pcb.setAssignedProcessor("CPU-0");
        pcb.resetQuantum();
    }

    public void preempt(PCB pcb) {
        pcb.setState(ProcessState.READY);
        pcb.setAssignedProcessor(null);
    }
}
