package kernx.ui.panels;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

import kernx.os.Kernel;
import kernx.os.data.PCB;

public class QueueVisualizationPanel extends JPanel {

    private final JTextArea readyArea = new JTextArea();
    private final JTextArea blockedArea = new JTextArea();
    private final JTextArea suspendedArea = new JTextArea();
    private final JTextArea runningArea = new JTextArea();

    public QueueVisualizationPanel() {
        setLayout(new GridLayout(1, 4, 10, 10));

        // Set background/foreground/fonts
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        readyArea.setBackground(new Color(240, 248, 255));
        readyArea.setForeground(Color.BLACK);
        readyArea.setFont(font);

        blockedArea.setBackground(new Color(255, 228, 225));
        blockedArea.setForeground(Color.black);
        blockedArea.setFont(font);

        suspendedArea.setBackground(new Color(255, 250, 205));
        suspendedArea.setForeground(Color.BLACK);
        suspendedArea.setFont(font);

        runningArea.setBackground(new Color(144, 238, 144));
        runningArea.setForeground(Color.black);
        runningArea.setFont(font);

        // Add panels
        add(createPanel("Ready Queue", readyArea));
        add(createPanel("Blocked Queue", blockedArea));
        add(createPanel("Suspended Queue", suspendedArea));
        add(createPanel("Running", runningArea));

        refresh();
    }


    private JPanel createPanel(String title, JTextArea area) {
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JPanel panel = new JPanel(new BorderLayout());

        // Create titled border with white text
        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(Color.WHITE);   // <-- title color set to white
        panel.setBorder(border);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }


    public void refresh() {
        readyArea.setText(queueToString(
                Kernel.getProcessManager().getReadyQueue()));

        blockedArea.setText(queueToString(
                Kernel.getProcessManager().getBlockedQueue()));

        suspendedArea.setText(queueToString(
                Kernel.getProcessManager().getSuspendedQueue()));

        PCB running = Kernel.getProcessManager().getRunningProcess();
        runningArea.setText(running == null ? "IDLE" : formatPCB(running));
    }

    private String queueToString(Queue<PCB> queue) {
        if (queue.isEmpty()) return "(empty)";

        StringBuilder sb = new StringBuilder();
        for (PCB pcb : queue) {
            sb.append(formatPCB(pcb)).append("\n");
        }
        return sb.toString();
    }

    private String formatPCB(PCB pcb) {
        return "ID:"+pcb.getPid() ;
    }
}
