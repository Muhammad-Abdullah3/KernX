package kernx.os.manager;
import java.util.*;
import java.util.function.Consumer;

import kernx.os.data.PCB;
import kernx.os.data.ProcessState;
import kernx.os.scheduler.Dispatcher;
import kernx.os.scheduler.FCFSScheduler;
import kernx.os.scheduler.RoundRobinScheduler;
import kernx.os.scheduler.Scheduler;
import kernx.os.memory.MemoryManager;

public class ProcessManager {

    private Consumer<String> messageListener;

    public void setMessageListener(Consumer<String> messageListener) {
        this.messageListener = messageListener;
    }

    private void notify(String message) {
        if (messageListener != null) {
            messageListener.accept(message);
        }
    }

    /* =======================
       PROCESS STORAGE
       ======================= */

    private final List<PCB> processList = new ArrayList<>();

    /* =======================
       QUEUES (Scheduling)
       ======================= */

    private final Queue<PCB> readyQueue = new LinkedList<>();
    private final Queue<PCB> blockedQueue = new LinkedList<>();
    private final Queue<PCB> suspendedQueue = new LinkedList<>();

    private PCB runningProcess;
    private int systemTime = 0;

    /* =======================
       SCHEDULER COMPONENTS
       ======================= */

    private Scheduler scheduler = new FCFSScheduler();
    private final Dispatcher dispatcher = new Dispatcher();

    /* =======================
       PROCESS CREATION
       (Long-term scheduling)
       ======================= */

    // Original method retained for compatibility
    public void createProcess(String owner, int memoryRequirement, int priority) {
        createProcess(owner, memoryRequirement, priority, 0, 0);
    }

    // New overload accepting burst time and arrival time
    public void createProcess(String owner, int memoryRequirement, int priority, int burstTime, int arrivalOffset) {
        // Lazy Loading: Create process WITHOUT allocating memory
        // Memory will be allocated on-demand when process transitions to READY/RUNNING
        
        int absoluteArrival = systemTime + Math.max(0, arrivalOffset);
        
        PCB pcb = new PCB(owner, memoryRequirement, priority, burstTime, absoluteArrival);
        pcb.setState(ProcessState.NEW);
        
        // Always add to process list (no memory check here)
        processList.add(pcb);
        notify("Created Process PID: " + pcb.getPid() + " (Arriving at: " + absoluteArrival + ") - Memory will be allocated on demand");
    }

    /* =======================
       PROCESS DESTRUCTION
       ======================= */

    public void destroyProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null) return;

        readyQueue.remove(pcb);
        blockedQueue.remove(pcb);
        suspendedQueue.remove(pcb);

        if (pcb == runningProcess) {
            runningProcess = null;
            dispatchNextProcess();
        }

        MemoryManager.getInstance().deallocate(pcb);
        processList.remove(pcb);
        notify("Destroyed Process PID: " + pid);
    }

    /* =======================
       DISPATCHING
       (Short-term scheduling)
       ======================= */

    public void dispatchNextProcess() {
        if (runningProcess != null) return;

        PCB next = scheduler.selectNextProcess(readyQueue);
        if (next != null) {
            // CRITICAL: Ensure memory is allocated before running
            if (next.getPageTable() == null) {
                boolean allocated = MemoryManager.getInstance().allocate(next);
                if (!allocated) {
                    // If allocation fails, re-add to ready queue and try next process
                    readyQueue.add(next);
                    notify("Warning: Could not allocate memory for PID " + next.getPid() + ", trying next process");
                    dispatchNextProcess(); // Recursive call to try next process
                    return;
                }
            }
            
            runningProcess = next;
            dispatcher.dispatch(next);
        }
    }

    public void dispatchProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null || pcb.getState() != ProcessState.READY) return;

        readyQueue.remove(pcb);

        if (runningProcess != null) {
            dispatcher.preempt(runningProcess);
            readyQueue.add(runningProcess);
        }

        runningProcess = pcb;
        dispatcher.dispatch(pcb);
    }

    /* =======================
       BLOCK / WAKEUP
       ======================= */

    public void blockProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null || pcb != runningProcess) return;

        pcb.setState(ProcessState.BLOCKED);
        blockedQueue.add(pcb);
        runningProcess = null;

        dispatchNextProcess();
        notify("Blocked Process PID: " + pid);
    }

    public void wakeupProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null || !blockedQueue.remove(pcb)) return;

        pcb.setState(ProcessState.READY);
        readyQueue.add(pcb);
        notify("Woke up Process PID: " + pid);
    }

    /* =======================
       SUSPEND / RESUME
       (Medium-term scheduling)
       ======================= */

    public void suspendProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null) return;

        if (pcb == runningProcess) {
            runningProcess = null;
            dispatchNextProcess();
        }

        readyQueue.remove(pcb);
        blockedQueue.remove(pcb);

        pcb.setState(ProcessState.SUSPENDED);
        suspendedQueue.add(pcb);
        notify("Suspended Process PID: " + pid);
    }

    public void resumeProcess(int pid) {
        PCB pcb = findByPid(pid);
        if (pcb == null || !suspendedQueue.remove(pcb)) return;

        pcb.setState(ProcessState.READY);
        readyQueue.add(pcb);
        notify("Resumed Process PID: " + pid);
    }

    /* =======================
       PRIORITY MANAGEMENT
       ======================= */

    public void changePriority(int pid, int newPriority) {
        PCB pcb = findByPid(pid);
        if (pcb != null) {
            pcb.setPriority(newPriority);
        }
    }

    /* =======================
       SCHEDULER CONTROL
       ======================= */

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void useFCFS() {
        this.scheduler = new FCFSScheduler();
    }

    public void useRoundRobin(int quantum) {
        this.scheduler = new RoundRobinScheduler(quantum);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    /* =======================
       GETTERS FOR UI
       ======================= */

    public List<PCB> getAllProcesses() {
        return Collections.unmodifiableList(processList);
    }

    public Queue<PCB> getReadyQueue() {
        return new LinkedList<>(readyQueue);
    }

    public Queue<PCB> getBlockedQueue() {
        return new LinkedList<>(blockedQueue);
    }

    public Queue<PCB> getSuspendedQueue() {
        return new LinkedList<>(suspendedQueue);
    }

    public PCB getRunningProcess() {
        return runningProcess;
    }

    public int getSystemTime() {
        return systemTime;
    }

    /* =======================
       INTERNAL UTIL
       ======================= */


    public PCB getProcess(int pid) {
        return findByPid(pid);
    }

    private PCB findByPid(int pid) {
        for (PCB pcb : processList) {
            if (pcb.getPid() == pid) {
                return pcb;
            }
        }
        return null;
    }
    private void checkArrivals() {
        for (PCB pcb : processList) {
            if (pcb.getState() == ProcessState.NEW && pcb.getArrivalTime() <= systemTime) {
                pcb.setState(ProcessState.READY);
                readyQueue.add(pcb);
                
                // Lazy allocation: Try to allocate memory now that process is ready
                ensureMemoryForActiveProcesses();
                
                notify("Process PID: " + pcb.getPid() + " Arrived at time " + systemTime);
            }
        }
    }
    
    // Ensures memory is allocated for active processes in priority order
    private void ensureMemoryForActiveProcesses() {
        // Priority 1: Allocate to running process first
        if (runningProcess != null && runningProcess.getPageTable() == null) {
            MemoryManager.getInstance().allocate(runningProcess);
        }
        
        // Priority 2: Allocate to ready queue processes (FIFO order)
        for (PCB pcb : readyQueue) {
            if (pcb.getPageTable() == null) {
                // Try to allocate, if it fails, that's okay - will try again later
                MemoryManager.getInstance().allocate(pcb);
            }
        }
    }

    // Cpu ticking
    public void tick() {
        systemTime++;
        checkArrivals();
        
        // Continuously try to allocate memory to active processes
        ensureMemoryForActiveProcesses();

        if (runningProcess == null) {
            dispatchNextProcess();
            return;
        }

        // Simulate CPU execution
        runningProcess.consumeCpu();
        if (runningProcess.getPageTable() != null) {
            for (kernx.os.memory.Page p : runningProcess.getPageTable().getPages()) {
                p.touch();
            }
        }

        // Process finished
        if (runningProcess.getRemainingBurstTime() <= 0) {
            notify("Process PID: " + runningProcess.getPid() + " Finished");
            runningProcess.setState(ProcessState.TERMINATED);
            runningProcess.setCompletionTime(systemTime);
            runningProcess = null;
            dispatchNextProcess();
            return;
        }

        // Round Robin quantum expiry
        if (scheduler instanceof RoundRobinScheduler rr) {
            if (runningProcess.getQuantumCounter() >= rr.getTimeQuantum()) {

                runningProcess.resetQuantum();
                dispatcher.preempt(runningProcess);
                readyQueue.add(runningProcess);
                runningProcess = null;

                dispatchNextProcess();
            }
        }
    }

}


