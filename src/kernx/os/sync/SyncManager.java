package kernx.os.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SyncManager {
    private static SyncManager instance;
    private final Map<Integer, Semaphore> semaphores;
    private int semaphoreCounter = 0;

    private SyncManager() {
        semaphores = new HashMap<>();
    }

    public static SyncManager getInstance() {
        if (instance == null) {
            instance = new SyncManager();
        }
        return instance;
    }

    public Semaphore createSemaphore(String name, int initialValue) {
        int id = semaphoreCounter++;
        Semaphore sem = new Semaphore(id, name, initialValue);
        semaphores.put(id, sem);
        return sem;
    }

    public Semaphore getSemaphore(int id) {
        return semaphores.get(id);
    }

    public List<Semaphore> getAllSemaphores() {
        return new ArrayList<>(semaphores.values());
    }
}
