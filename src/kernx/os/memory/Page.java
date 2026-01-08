package kernx.os.memory;

public class Page {
    private final int pageNumber;
    private int frameNumber = -1; // -1 means not in memory (swapped or not allocated)
    private boolean present = false;
    private long lastReferencedTime;

    public Page(int pageNumber) {
        this.pageNumber = pageNumber;
        this.lastReferencedTime = System.currentTimeMillis();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
        this.present = (frameNumber != -1);
        touch();
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public long getLastReferencedTime() {
        return lastReferencedTime;
    }

    public void touch() {
        this.lastReferencedTime = System.currentTimeMillis();
    }
}
