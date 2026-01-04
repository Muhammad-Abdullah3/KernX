package kernx.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.os.models.ProcessTableModel;

public class ProcessTablePanel extends JPanel {

    private final ProcessTableModel model;
    private final JTable table;

    // Link to queue visualization panel
    private QueueVisualizationPanel queuePanel;

    public ProcessTablePanel() {
        setLayout(new BorderLayout());

        model = new ProcessTableModel();
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setQueueVisualizationPanel(QueueVisualizationPanel queuePanel) {
        this.queuePanel = queuePanel;
    }

    public void refresh() {
        // Refresh the process table
        List<PCB> processes = Kernel.getProcessManager().getAllProcesses();
        model.setProcesses(processes);

        // Refresh the queue panel if linked
        if (queuePanel != null) {
            queuePanel.refresh();
        }
    }
}
