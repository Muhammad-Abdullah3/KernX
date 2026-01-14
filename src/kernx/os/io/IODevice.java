package kernx.os.io;

public class IODevice {
    private final String name;
    private final String type; // INPUT, OUTPUT, STORAGE
    private boolean isBusy;
    private int currentOwnerPid;

    public IODevice(String name, String type) {
        this.name = name;
        this.type = type;
        this.isBusy = false;
        this.currentOwnerPid = -1;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public boolean isBusy() { return isBusy; }
    
    public int getCurrentOwnerPid() { return currentOwnerPid; }

    public void allocate(int pid) {
        this.isBusy = true;
        this.currentOwnerPid = pid;
    }

    public void release() {
        this.isBusy = false;
        this.currentOwnerPid = -1;
    }
}
