package kernx.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.os.models.ProcessTableModel;
import kernx.ui.prebuilt.UITheme;

public class ProcessTablePanel extends JPanel {

    private final ProcessTableModel model;
    private final JTable table;

    // Link to queue visualization panel
    private QueueVisualizationPanel queuePanel;

    public ProcessTablePanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);

        model = new ProcessTableModel();
        table = new JTable(model);
        
        // Table styling
        table.setBackground(UITheme.SURFACE);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(UITheme.ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setFont(UITheme.MAIN_FONT);
        table.setIntercellSpacing(new Dimension(10, 0));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(UITheme.BACKGROUND);
        header.setForeground(UITheme.ACCENT);
        header.setFont(UITheme.TITLE_FONT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.ACCENT));

        // Center alignment for some columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BACKGROUND);
        
        add(scrollPane, BorderLayout.CENTER);

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // Select
        table.getColumnModel().getColumn(1).setPreferredWidth(50); // PID
    }

    public void setQueueVisualizationPanel(QueueVisualizationPanel queuePanel) {
        this.queuePanel = queuePanel;
    }

    public ProcessTableModel getModel() {
        return model;
    }

    public void refresh() {
        List<PCB> processes = Kernel.getProcessManager().getAllProcesses();
        model.setProcesses(processes);

        if (queuePanel != null) {
            queuePanel.refresh();
        }
    }
}
