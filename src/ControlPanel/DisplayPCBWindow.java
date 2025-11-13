package ControlPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DisplayPCBWindow {

    public void showPCBTable() {
        JFrame pcbFrame = new JFrame("Process Control Blocks");
        pcbFrame.setSize(900, 400);
        pcbFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pcbFrame.setLayout(new BorderLayout());

        // Column headers
        String[] columnNames = {
                "Process ID", "Owner", "State", "Priority",
                "Memory Required", "Allocated Memory",
                "Processor", "CPU Register", "I/O State"
        };

        // ✅ Get actual PCB data
        List<PCB> pcbList = PCB.getAllPCBs();

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (pcbList.isEmpty()) {
            JOptionPane.showMessageDialog(pcbFrame,
                    "No processes available in the PCB table.",
                    "No Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (PCB pcb : pcbList) {
                Object[] row = {
                        pcb.getProcessID(),
                        pcb.getOwner(),
                        pcb.getCurrentState(),
                        pcb.getPriority(),
                        pcb.getMemoryRequired(),
                        pcb.getAllocatedMemory(),
                        pcb.getProcessor(),
                        pcb.getCpuRegister(),
                        pcb.getIoState()
                };
                model.addRow(row);
            }
        }

        JTable pcbTable = new JTable(model);
        pcbTable.setFillsViewportHeight(true);
        pcbTable.setBackground(new Color(240, 240, 240));
        pcbTable.setFont(new Font("Consolas", Font.PLAIN, 12));
        pcbTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(pcbTable);
        pcbFrame.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> pcbFrame.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        pcbFrame.add(bottomPanel, BorderLayout.SOUTH);

        pcbFrame.setLocationRelativeTo(null);
        pcbFrame.setVisible(true);
    }
}
