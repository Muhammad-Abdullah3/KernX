package kernx.os.models;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kernx.os.data.PCB;

public class ProcessTableModel extends AbstractTableModel {

    private final String[] columns = {
            "Select", "PID", "Owner", "State", "Priority", "Memory", "Burst", "Rem", "Arrival", "Comp"
    };

    private List<PCB> processes = new ArrayList<>();
    private final Set<Integer> selectedPids = new HashSet<>();

    public void setProcesses(List<PCB> processes) {
        this.processes = processes;
        fireTableDataChanged();
    }

    public List<Integer> getSelectedPids() {
        return new ArrayList<>(selectedPids);
    }

    public void clearSelection() {
        selectedPids.clear();
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
    public Class<?> getColumnClass(int col) {
        if (col == 0) return Boolean.class;
        return super.getColumnClass(col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (row >= processes.size()) return null;
        PCB pcb = processes.get(row);
        return switch (col) {
            case 0 -> selectedPids.contains(pcb.getPid());
            case 1 -> pcb.getPid();
            case 2 -> pcb.getOwner();
            case 3 -> pcb.getState();
            case 4 -> pcb.getPriority();
            case 5 -> pcb.getMemoryRequirement();
            case 6 -> pcb.getBurstTime();
            case 7 -> pcb.getRemainingBurstTime();
            case 8 -> pcb.getArrivalTime();
            case 9 -> pcb.getCompletionTime() == -1 ? "-" : pcb.getCompletionTime();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 0 && row < processes.size()) {
            PCB pcb = processes.get(row);
            if ((Boolean) value) {
                selectedPids.add(pcb.getPid());
            } else {
                selectedPids.remove(pcb.getPid());
            }
            fireTableCellUpdated(row, col);
        }
    }
}
