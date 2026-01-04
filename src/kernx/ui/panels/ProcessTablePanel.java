package kernx.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import kernx.os.Kernel;
import kernx.os.data.PCB;

public class ProcessTablePanel extends JPanel {

    private final ProcessTableModel model;

    public ProcessTablePanel() {
        setLayout(new BorderLayout());

        model = new ProcessTableModel();
        JTable table = new JTable(model);

        refresh();

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refresh() {
        List<PCB> list = Kernel.getProcessManager()
                .getAllProcesses()
                .stream()
                .toList();

        model.setProcesses(list);
    }
}
