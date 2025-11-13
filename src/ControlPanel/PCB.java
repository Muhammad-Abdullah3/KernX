package ControlPanel;

import java.util.ArrayList;
import java.util.List;

public class PCB {
    // Attributes according to the specification
    private final int processID;                // Unique identification of the process
    private ProcessState currentState;          // Current state of the process (7-state model)
    private String owner;                       // Owner of the process
    private int priority;                       // Process priority
    private PCB parent;                         // Pointer to process parent
    private List<PCB> children;                 // Child process list
    private int memoryRequired;                 // Memory requirements (KB/MB)
    private int allocatedMemory;                // Memory allocated
    private String cpuRegister;                 // Register save area
    private String processor;                   // Processor name or core ID
    private String ioState;                     // I/O state information

    // ✅ Shared list to keep track of all PCBs
    private static final List<PCB> pcbTable = new ArrayList<>();

    // Constructor
    private static int idCounter = 1;

    public PCB(String owner, int priority, int memoryRequired) {
        this.processID = idCounter++;
        this.owner = owner;
        this.priority = priority;
        this.memoryRequired = memoryRequired;
        this.currentState = ProcessState.NEW;
        this.children = new ArrayList<>();

        // ✅ Automatically register this PCB in global table
        pcbTable.add(this);
    }

    // ✅ Static getter for all PCBs
    public static List<PCB> getAllPCBs() {
        return pcbTable;
    }

    // Getters and Setters
    public int getProcessID() { return processID; }
    public ProcessState getCurrentState() { return currentState; }
    public void setCurrentState(ProcessState state) { this.currentState = state; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public PCB getParent() { return parent; }
    public void setParent(PCB parent) { this.parent = parent; }
    public List<PCB> getChildren() { return children; }
    public void addChild(PCB child) { children.add(child); }
    public int getMemoryRequired() { return memoryRequired; }
    public void setMemoryRequired(int memoryRequired) { this.memoryRequired = memoryRequired; }
    public int getAllocatedMemory() { return allocatedMemory; }
    public void setAllocatedMemory(int allocatedMemory) { this.allocatedMemory = allocatedMemory; }
    public String getCpuRegister() { return cpuRegister; }
    public void setCpuRegister(String cpuRegister) { this.cpuRegister = cpuRegister; }
    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }
    public String getIoState() { return ioState; }
    public void setIoState(String ioState) { this.ioState = ioState; }

    @Override
    public String toString() {
        return "Process ID: " + processID +
                "\nState: " + currentState +
                "\nOwner: " + owner +
                "\nPriority: " + priority +
                "\nMemory Required: " + memoryRequired +
                "\nAllocated Memory: " + allocatedMemory +
                "\nCPU Register: " + cpuRegister +
                "\nProcessor: " + processor +
                "\nI/O State: " + ioState;
    }
}
