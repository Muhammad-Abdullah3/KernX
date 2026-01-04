package kernx.os;

import kernx.os.manager.ProcessManager;

public class Kernel {

    private static final ProcessManager processManager = new ProcessManager();

    public static ProcessManager getProcessManager() {
        return processManager;
    }
}
