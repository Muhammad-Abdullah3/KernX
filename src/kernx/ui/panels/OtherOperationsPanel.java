package kernx.ui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import kernx.os.Kernel;
import kernx.ui.prebuilt.UITheme;

public class OtherOperationsPanel extends JPanel {

    private JTextArea logArea;
    private JLabel uptimeLabel;
    private JLabel totalProcsLabel;
    private Timer refreshTimer;

    public OtherOperationsPanel() {
        UITheme.applyGlobalTheme();
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. System Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Information"));
        
        uptimeLabel = new JLabel("System Time: 0");
        uptimeLabel.setFont(UITheme.TITLE_FONT);
        
        totalProcsLabel = new JLabel("Total Processes: 0");
        totalProcsLabel.setFont(UITheme.TITLE_FONT);

        statsPanel.add(uptimeLabel);
        statsPanel.add(totalProcsLabel);

        // 2. Logs Panel
        JPanel logsPanel = new JPanel(new BorderLayout());
        logsPanel.setOpaque(false);
        logsPanel.setBorder(BorderFactory.createTitledBorder("System Logs"));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        logsPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. About Panel
        JPanel aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setOpaque(false);
        aboutPanel.setBorder(BorderFactory.createTitledBorder("About KernX"));
        JLabel aboutText = new JLabel("<html><center>KernX OS Simulation<br>Phase 1-4 Complete<br>Developed for Operating Systems Course</center></html>", SwingConstants.CENTER);
        aboutPanel.add(aboutText, BorderLayout.CENTER);


        // Assembly
        JPanel topContainer = new JPanel(new BorderLayout(10, 10));
        topContainer.setOpaque(false);
        topContainer.add(statsPanel, BorderLayout.CENTER);
        topContainer.add(aboutPanel, BorderLayout.EAST);

        add(topContainer, BorderLayout.NORTH);
        add(logsPanel, BorderLayout.CENTER);

        // Hook up listener
        Kernel.getProcessManager().setMessageListener(this::appendLog);

        // Refresh Timer
        refreshTimer = new Timer(1000, e -> updateStats());
        refreshTimer.start();
    }

    private void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void updateStats() {
        uptimeLabel.setText("System Time: " + Kernel.getProcessManager().getSystemTime());
        totalProcsLabel.setText("Total Processes Managed: " + Kernel.getProcessManager().getAllProcesses().size());
    }
}
