package kernx.os.io;

import java.util.ArrayList;
import java.util.List;
import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.os.data.ProcessState;

public class IOManager {
    private static IOManager instance;
    private final List<IODevice> devices;

    private IOManager() {
        devices = new ArrayList<>();
        // Initialize some standard devices
        devices.add(new IODevice("Keyboard", "INPUT"));
        devices.add(new IODevice("Mouse", "INPUT"));
        devices.add(new IODevice("Monitor", "OUTPUT"));
        devices.add(new IODevice("Printer", "OUTPUT"));
        devices.add(new IODevice("HDD", "STORAGE"));
    }

    public static IOManager getInstance() {
        if (instance == null) {
            instance = new IOManager();
        }
        return instance;
    }

    public List<IODevice> getDevices() {
        return devices;
    }

    public boolean requestDevice(int pid, String deviceName) {
        for (IODevice dev : devices) {
            if (dev.getName().equals(deviceName)) {
                if (!dev.isBusy()) {
                    dev.allocate(pid);
                    
                    PCB pcb = Kernel.getProcessManager().getProcess(pid);
                    if (pcb != null) {
                        pcb.getIOState().setWaitingForIO(true);
                        pcb.getIOState().setDeviceName(deviceName);
                        Kernel.getProcessManager().blockProcess(pid); // Block process when it starts I/O
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void releaseDevice(int pid, String deviceName) {
        for (IODevice dev : devices) {
            if (dev.getName().equals(deviceName) && dev.getCurrentOwnerPid() == pid) {
                dev.release();
                
                PCB pcb = Kernel.getProcessManager().getProcess(pid);
                if (pcb != null) {
                    pcb.getIOState().setWaitingForIO(false);
                    pcb.getIOState().setDeviceName("NONE");
                    Kernel.getProcessManager().wakeupProcess(pid); // Wakeup when I/O finished
                }
                return;
            }
        }
    }
}
