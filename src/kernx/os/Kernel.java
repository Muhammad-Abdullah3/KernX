package kernx.os;

import kernx.os.manager.ProcessManager;

public class Kernel {

    private static final ProcessManager processManager = new ProcessManager();


    private static final kernx.os.ipc.IPCManager ipcManager = kernx.os.ipc.IPCManager.getInstance();
    private static final kernx.os.sync.SyncManager syncManager = kernx.os.sync.SyncManager.getInstance();

    public static ProcessManager getProcessManager() {
        return processManager;
    }

    public static kernx.os.ipc.IPCManager getIPCManager() {
        return ipcManager;
    }


    public static kernx.os.sync.SyncManager getSyncManager() {
        return syncManager;
    }

    public static kernx.os.io.IOManager getIOManager() {
        return kernx.os.io.IOManager.getInstance();
    }
}
