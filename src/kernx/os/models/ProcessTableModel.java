package kernx.ui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import kernx.os.data.PCB;
import kernx.os.data.ProcessState;

public class ProcessTableModel extends AbstractTableModel {

    private final String[] columns = {
            "PID", "Owner", "State", "Priority", "Memory"
    };

    private List<PCB> processes = new ArrayList<>();

    public void setProcesses(List<PCB> processes) {
        this.processes = processes;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return processes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        PCB pcb = processes.get(row);
        return switch (col) {
            case 0 -> pcb.getPid();
            case 1 -> pcb.getOwner();
            case 2 -> pcb.getState();
            case 3 -> pcb.getPriority();
            case 4 -> pcb.getMemoryRequirement();
            default -> null;
        };
    }
}
