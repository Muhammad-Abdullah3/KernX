package kernx.os.data;

import java.util.HashMap;
import java.util.Map;

public class RegisterSet {

    private Map<String, Integer> registers;

    public RegisterSet() {
        registers = new HashMap<>();
        registers.put("PC", 0);
        registers.put("SP", 0);
        registers.put("ACC", 0);
    }

    public Map<String, Integer> getRegisters() {
        return registers;
    }
}
