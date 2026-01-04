package kernx.ui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Queue;

import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.ui.utils.UITheme;

public class QueueVisualizationPanel extends JPanel {

    private final JTextArea readyArea = createTextArea(UITheme.ACCENT);
    private final JTextArea blockedArea = createTextArea(UITheme.DANGER);
    private final JTextArea suspendedArea = createTextArea(new Color(255, 180, 0));
    private final JTextArea runningArea = createTextArea(UITheme.SUCCESS);

    public QueueVisualizationPanel() {
        setLayout(new GridLayout(1, 4, 10, 10));
        setBackground(UITheme.BACKGROUND);
        setOpaque(true);

        // Add panels
        add(createPanel("Ready Queue", readyArea));
        add(createPanel("Blocked Queue", blockedArea));
        add(createPanel("Suspended Queue", suspendedArea));
        add(createPanel("Running", runningArea));

        refresh();
    }

    private JTextArea createTextArea(Color accentColor) {
        JTextArea area = new JTextArea(5, 20); // Multiple rows allowed
        area.setEditable(false);
        area.setBackground(UITheme.SURFACE);
        area.setForeground(UITheme.TEXT_PRIMARY);
        area.setFont(UITheme.MONO_FONT);
        area.setMargin(new Insets(10, 10, 10, 10));
        return area;
    }

    private JPanel createPanel(String title, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(UITheme.createPanelBorder(title));
        panel.setPreferredSize(new Dimension(200, 120)); // Small "window" height

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getViewport().setBackground(UITheme.SURFACE);
        
        // Custom scrollbar UI (optional, inherited from UITheme usually)
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void refresh() {
        readyArea.setText(queueToString(Kernel.getProcessManager().getReadyQueue()));
        blockedArea.setText(queueToString(Kernel.getProcessManager().getBlockedQueue()));
        suspendedArea.setText(queueToString(Kernel.getProcessManager().getSuspendedQueue()));

        PCB running = Kernel.getProcessManager().getRunningProcess();
        runningArea.setText(running == null ? "IDLE" : formatPCB(running));
    }

    private String queueToString(Queue<PCB> queue) {
        if (queue.isEmpty()) return "(empty)";
        StringBuilder sb = new StringBuilder();
        for (PCB pcb : queue) {
            sb.append(formatPCB(pcb)).append("\n");
        }
        return sb.toString().trim();
    }

    private String formatPCB(PCB pcb) {
        return String.format("PID: %d | %s | B:%d", pcb.getPid(), pcb.getOwner(), pcb.getBurstTime());
    }
}
