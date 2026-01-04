package kernx.os.data;

public class IOState {

    private boolean waitingForIO;
    private String deviceName;

    public IOState() {
        this.waitingForIO = false;
        this.deviceName = "NONE";
    }

    public boolean isWaitingForIO() {
        return waitingForIO;
    }

    public void setWaitingForIO(boolean waitingForIO) {
        this.waitingForIO = waitingForIO;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
