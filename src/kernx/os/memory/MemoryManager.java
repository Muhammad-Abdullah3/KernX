package kernx.os.memory;

import kernx.os.data.PCB;
import kernx.utils.ConfigManager;
import java.util.*;

public class MemoryManager {
    private static MemoryManager instance;
    private List<MemoryFrame> frames;
    private int pageSize;
    private int totalMemory;

    private MemoryManager() {
        refreshConfig();
    }

    public static MemoryManager getInstance() {
        if (instance == null) {
            instance = new MemoryManager();
        }
        return instance;
    }

    public void refreshConfig() {
        this.pageSize = ConfigManager.getPageSize();
        this.totalMemory = ConfigManager.getTotalMemory();
        int numFrames = totalMemory / pageSize;
        
        this.frames = new ArrayList<>(numFrames);
        for (int i = 0; i < numFrames; i++) {
            frames.add(new MemoryFrame(i));
        }
    }

    public boolean allocate(PCB pcb) {
        int reqMemory = pcb.getMemoryRequirement();
        int numPages = (int) Math.ceil((double) reqMemory / pageSize);
        PageTable pt = new PageTable(numPages);
        pcb.setPageTable(pt);

        for (Page page : pt.getPages()) {
            MemoryFrame freeFrame = findFreeFrame();
            if (freeFrame == null) {
                freeFrame = lruReplace();
            }
            
            if (freeFrame != null) {
                freeFrame.setFree(false);
                freeFrame.setOwningPid(pcb.getPid());
                freeFrame.setPageNumber(page.getPageNumber());
                page.setFrameNumber(freeFrame.getFrameNumber());
            } else {
                return false; // Should not happen with LRU unless no frames exist
            }
        }
        return true;
    }

    public void deallocate(PCB pcb) {
        if (pcb.getPageTable() == null) return;
        
        for (Page page : pcb.getPageTable().getPages()) {
            if (page.isPresent()) {
                MemoryFrame frame = frames.get(page.getFrameNumber());
                frame.setFree(true);
                page.setFrameNumber(-1);
            }
        }
        pcb.setPageTable(null);
    }

    private MemoryFrame findFreeFrame() {
        for (MemoryFrame frame : frames) {
            if (frame.isFree()) return frame;
        }
        return null;
    }

    private MemoryFrame lruReplace() {
        // Priority-Based Page Replacement Algorithm
        // Priority Order (highest to lowest):
        // 1. RUNNING process - NEVER evict (must always be in memory)
        // 2. READY queue - FIFO preference (evict from end if needed)
        // 3. BLOCKED queue - lower priority
        // 4. SUSPENDED queue - lowest priority (evict first)
        
        kernx.os.manager.ProcessManager pm = kernx.os.Kernel.getProcessManager();
        
        // Build priority sets
        java.util.Set<Integer> runningPid = new java.util.HashSet<>();
        java.util.Set<Integer> readyPids = new java.util.LinkedHashSet<>(); // Preserve order
        java.util.Set<Integer> blockedPids = new java.util.HashSet<>();
        java.util.Set<Integer> suspendedPids = new java.util.HashSet<>();
        
        // 1. Running process (highest priority - never evict)
        if (pm.getRunningProcess() != null) {
            runningPid.add(pm.getRunningProcess().getPid());
        }
        
        // 2. Ready queue (FIFO order - front has higher priority)
        for (PCB pcb : pm.getReadyQueue()) {
            readyPids.add(pcb.getPid());
        }
        
        // 3. Blocked queue
        for (PCB pcb : pm.getBlockedQueue()) {
            blockedPids.add(pcb.getPid());
        }
        
        // 4. Suspended queue (lowest priority)
        for (PCB pcb : pm.getSuspendedQueue()) {
            suspendedPids.add(pcb.getPid());
        }
        
        MemoryFrame victimFrame = null;
        long oldestTime = Long.MAX_VALUE;
        
        // PRIORITY 4: Try to evict from SUSPENDED processes first
        for (MemoryFrame frame : frames) {
            if (!frame.isFree() && suspendedPids.contains(frame.getOwningPid())) {
                Page p = findPageByFrame(frame.getFrameNumber());
                if (p != null && p.getLastReferencedTime() < oldestTime) {
                    oldestTime = p.getLastReferencedTime();
                    victimFrame = frame;
                }
            }
        }
        
        // PRIORITY 3: If no suspended pages, evict from BLOCKED processes
        if (victimFrame == null) {
            oldestTime = Long.MAX_VALUE;
            for (MemoryFrame frame : frames) {
                if (!frame.isFree() && blockedPids.contains(frame.getOwningPid())) {
                    Page p = findPageByFrame(frame.getFrameNumber());
                    if (p != null && p.getLastReferencedTime() < oldestTime) {
                        oldestTime = p.getLastReferencedTime();
                        victimFrame = frame;
                    }
                }
            }
        }
        
        // PRIORITY 2: If still none, evict from READY queue (prefer later processes)
        // Convert to list to access by position
        if (victimFrame == null && !readyPids.isEmpty()) {
            java.util.List<Integer> readyList = new java.util.ArrayList<>(readyPids);
            
            // Start from END of ready queue (lower priority processes)
            for (int i = readyList.size() - 1; i >= 0; i--) {
                int pid = readyList.get(i);
                oldestTime = Long.MAX_VALUE;
                
                for (MemoryFrame frame : frames) {
                    if (!frame.isFree() && frame.getOwningPid() == pid) {
                        Page p = findPageByFrame(frame.getFrameNumber());
                        if (p != null && p.getLastReferencedTime() < oldestTime) {
                            oldestTime = p.getLastReferencedTime();
                            victimFrame = frame;
                        }
                    }
                }
                
                if (victimFrame != null) break; // Found victim from this ready process
            }
        }
        
        // PRIORITY 1: RUNNING process is NEVER evicted (should never reach here)
        // If we reach here with no victim, memory is critically full
        
        if (victimFrame != null) {
            // "Swap out" the victim page
            Page victimPage = findPageByFrame(victimFrame.getFrameNumber());
            if (victimPage != null) {
                victimPage.setFrameNumber(-1);
            }
            victimFrame.setFree(true);
        }

        return victimFrame;
    }

    private Page findPageByFrame(int frameNumber) {
        int pid = frames.get(frameNumber).getOwningPid();
        if (pid == -1) return null;

        for (PCB pcb : kernx.os.Kernel.getProcessManager().getAllProcesses()) {
            if (pcb.getPid() == pid && pcb.getPageTable() != null) {
                for (Page page : pcb.getPageTable().getPages()) {
                    if (page.getFrameNumber() == frameNumber) {
                        return page;
                    }
                }
            }
        }
        return null;
    }

    // Improved findPageByFrame after integration fixes
    public void setVictimPage(int frameNumber, Page page) {
        // Manual mapping if needed
    }

    public List<MemoryFrame> getFrames() {
        return frames;
    }
}
