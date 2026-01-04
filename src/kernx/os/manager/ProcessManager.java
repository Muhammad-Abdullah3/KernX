package kernx.os;

import kernx.os.data.*;
import java.util.*;

public class ProcessManager {

    private final Map<Integer, PCB> processTable;

    public ProcessManager() {
        processTable = new HashMap<>();
    }

    // CREATE
    public PCB createProcess(String owner, int memory, int priority) {
        PCB pcb = new PCB(owner, memory, priority);
        pcb.setState(ProcessState.READY);
        processTable.put(pcb.getPid(), pcb);
        return pcb;
    }

    // DESTROY
    public void destroyProcess(int pid) {
        PCB pcb = processTable.remove(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.TERMINATED);
        }
    }

    // SUSPEND
    public void suspendProcess(int pid) {
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.SUSPENDED);
        }
    }

    // RESUME
    public void resumeProcess(int pid) {
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.READY);
        }
    }

    // BLOCK
    public void blockProcess(int pid) {
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.BLOCKED);
        }
    }

    // WAKEUP
    public void wakeupProcess(int pid) {
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.READY);
        }
    }

    // DISPATCH
    public void dispatchProcess(int pid) {
        for (PCB p : processTable.values()) {
            if (p.getState() == ProcessState.RUNNING) {
                p.setState(ProcessState.READY);
            }
        }
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setState(ProcessState.RUNNING);
        }
    }

    // CHANGE PRIORITY
    public void changePriority(int pid, int newPriority) {
        PCB pcb = processTable.get(pid);
        if (pcb != null) {
            pcb.setPriority(newPriority);
        }
    }

    // FETCH
    public Collection<PCB> getAllProcesses() {
        return processTable.values();
    }
}
