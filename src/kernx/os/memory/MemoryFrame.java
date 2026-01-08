package kernx.os.memory;

public class MemoryFrame {
    private final int frameNumber;
    private boolean free = true;
    private int owningPid = -1;
    private int pageNumber = -1;

    public MemoryFrame(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
        if (free) {
            this.owningPid = -1;
            this.pageNumber = -1;
        }
    }

    public int getOwningPid() {
        return owningPid;
    }

    public void setOwningPid(int owningPid) {
        this.owningPid = owningPid;
        this.free = (owningPid == -1);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
