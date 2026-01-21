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
        
        kernx.os.Kernel.getProcessManager().notify("[MEMORY] Allocated " + numPages + " page(s) (" + reqMemory + " KB) for PID=" + pcb.getPid());
        return true;
    }

    public void deallocate(PCB pcb) {
        if (pcb.getPageTable() == null) return;
        
        int pageCount = 0;
        for (Page page : pcb.getPageTable().getPages()) {
            if (page.isPresent()) {
                MemoryFrame frame = frames.get(page.getFrameNumber());
                frame.setFree(true);
                page.setFrameNumber(-1);
                pageCount++;
            }
        }
        pcb.setPageTable(null);
        
        kernx.os.Kernel.getProcessManager().notify("[MEMORY] Deallocated " + pageCount + " page(s) from PID=" + pcb.getPid());
    }

    private MemoryFrame findFreeFrame() {
        for (MemoryFrame frame : frames) {
            if (frame.isFree()) return frame;
        }
        return null;
    }

    private MemoryFrame lruReplace() {
        // Simple LRU: find the page that was touched longest ago
        // We need to find all allocated pages across all processes
        // For simulation, we'll iterate through all frames and find the one whose page has the oldest lastReferencedTime
        
        MemoryFrame victimFrame = null;
        long oldestTime = Long.MAX_VALUE;

        // Note: In a real OS, we'd have a list of active pages or use a clock algorithm.
        // Here we'll just check all frames.
        for (MemoryFrame frame : frames) {
            if (!frame.isFree()) {
                // We need to find the Page object associated with this frame.
                // This is a bit inefficient here but works for simulation.
                Page p = findPageByFrame(frame.getFrameNumber());
                if (p != null && p.getLastReferencedTime() < oldestTime) {
                    oldestTime = p.getLastReferencedTime();
                    victimFrame = frame;
                }
            }
        }

        if (victimFrame != null) {
            // "Swap out" the victim page
            Page victimPage = findPageByFrame(victimFrame.getFrameNumber());
            if (victimPage != null) {
                int victimPid = victimFrame.getOwningPid();
                victimPage.setFrameNumber(-1);
                kernx.os.Kernel.getProcessManager().notify("[MEMORY] LRU Replacement: Evicted frame #" + victimFrame.getFrameNumber() + " from PID=" + victimPid);
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
