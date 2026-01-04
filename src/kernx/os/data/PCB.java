package kernx.os.data;

import kernx.utils.IDGenerator;
import java.util.ArrayList;
import java.util.List;

public class PCB {

    // Identification
    private final int pid;
    private String owner;

    // State & priority
    private ProcessState state;
    private int priority;

    // Process hierarchy
    private PCB parent;
    private List<PCB> children;

    // Memory
    private int memoryRequirement;
    private Object memoryPointer;   // Placeholder (Phase-3)

    // CPU
    private RegisterSet registerSet;
    private String processor;

    // I/O
    private IOState ioState;
    // CPU burst simulation
    private int totalBurstTime;
    private int remainingBurstTime;
    private int quantumCounter;


    public PCB(String owner, int memoryRequirement, int priority) {
        this.pid = IDGenerator.generatePID();
        this.owner = owner;
        this.memoryRequirement = memoryRequirement;
        this.priority = priority;

        this.state = ProcessState.NEW;
        this.children = new ArrayList<>();
        this.registerSet = new RegisterSet();
        this.processor = "CPU-0";
        this.ioState = new IOState();
        this.totalBurstTime = 10 + (int)(this.memoryRequirement/10);
        this.remainingBurstTime = totalBurstTime;
        this.quantumCounter = 0;

    }

    // Getters
    public int getPid() { return pid; }
    public ProcessState getState() { return state; }
    public int getPriority() { return priority; }
    public PCB getParent() { return parent; }
    public List<PCB> getChildren() { return children; }
    public int getMemoryRequirement() {return memoryRequirement;}
    public String getOwner() {return owner;}
    // State updates
    public void setState(ProcessState state) {
        this.state = state;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setParent(PCB parent) {
        this.parent = parent;
    }

    public void addChild(PCB child) {
        this.children.add(child);
    }

    public void setAssignedProcessor(String processorName) { this.processor= processorName;
    }
    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public void consumeCpu() {
        remainingBurstTime--;
        quantumCounter++;
    }

    public void resetQuantum() {
        quantumCounter = 0;
    }

    public int getQuantumCounter() {
        return quantumCounter;
    }

}
