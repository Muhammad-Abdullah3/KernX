package kernx.ui.panels;

import javax.swing.*;
import java.awt.*;
import kernx.os.Kernel;
import kernx.ui.dialogs.*;
import kernx.ui.utils.Snackbar;
import kernx.ui.utils.UITheme;
import javax.swing.border.EmptyBorder;
import java.util.List;

public class ProcessManagementPanel extends JPanel {

    private final ProcessTablePanel processTablePanel;
    private final Timer cpuTimer;
    private final Snackbar snackbar;
    private final JLabel systemTimeLabel;

    public ProcessManagementPanel() {
        UITheme.applyGlobalTheme();
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Use a LayeredPane to overlay the snackbar
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());

        // Top: Buttons toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.setOpaque(false);

        JButton createBtn = UITheme.createStyledButton("Create");
        JButton destroyBtn = UITheme.createStyledButton("Destroy");
        JButton suspendBtn = UITheme.createStyledButton("Suspend");
        JButton resumeBtn = UITheme.createStyledButton("Resume");
        JButton blockBtn = UITheme.createStyledButton("Block");
        JButton wakeupBtn = UITheme.createStyledButton("Wakeup");
        JButton dispatchBtn = UITheme.createStyledButton("Dispatch");
        JButton changePriorityBtn = UITheme.createStyledButton("Priority");
        JButton communicationBtn = UITheme.createStyledButton("IPC");
        
        // Scheduler Selection
        String[] schedulers = {"FCFS", "Round Robin"};
        JComboBox<String> schedulerCombo = new JComboBox<>(schedulers);
        schedulerCombo.setPreferredSize(new Dimension(120, 30));
        
        // Simulation Controls
        JButton startBtn = UITheme.createStyledButton("Start CPU");
        JButton stopBtn = UITheme.createStyledButton("Stop CPU");
        startBtn.setForeground(UITheme.SUCCESS);
        stopBtn.setForeground(UITheme.DANGER);

        systemTimeLabel = new JLabel("Time: 0");
        systemTimeLabel.setForeground(UITheme.ACCENT);
        systemTimeLabel.setFont(UITheme.TITLE_FONT);
        systemTimeLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        
        toolbarPanel.add(createBtn);
        toolbarPanel.add(destroyBtn);
        toolbarPanel.add(suspendBtn);
        toolbarPanel.add(resumeBtn);
        toolbarPanel.add(blockBtn);
        toolbarPanel.add(wakeupBtn);
        toolbarPanel.add(dispatchBtn);
        toolbarPanel.add(changePriorityBtn);
        toolbarPanel.add(communicationBtn);
        
        toolbarPanel.add(new JSeparator(JSeparator.VERTICAL));
        toolbarPanel.add(new JLabel(" Scheduler: "));
        toolbarPanel.add(schedulerCombo);
        
        toolbarPanel.add(new JSeparator(JSeparator.VERTICAL));
        toolbarPanel.add(startBtn);
        toolbarPanel.add(stopBtn);
        toolbarPanel.add(new JSeparator(JSeparator.VERTICAL));
        toolbarPanel.add(systemTimeLabel);

        // Process Table
        processTablePanel = new ProcessTablePanel();
        QueueVisualizationPanel queuePanel = new QueueVisualizationPanel();
        processTablePanel.setQueueVisualizationPanel(queuePanel);

        // Snackbar
        snackbar = new Snackbar();
        snackbar.setPreferredSize(new Dimension(400, 50));
        Kernel.getProcessManager().setMessageListener(snackbar::showMessage);

        // Button Actions
        createBtn.addActionListener(e -> new CreateProcessDialog(SwingUtilities.getWindowAncestor(this), processTablePanel));
        
        destroyBtn.addActionListener(e -> handleBulkAction("Destroy", pids -> {
            pids.forEach(pid -> Kernel.getProcessManager().destroyProcess(pid));
        }));
        
        suspendBtn.addActionListener(e -> handleBulkAction("Suspend", pids -> {
            pids.forEach(pid -> Kernel.getProcessManager().suspendProcess(pid));
        }));
        
        resumeBtn.addActionListener(e -> handleBulkAction("Resume", pids -> {
            pids.forEach(pid -> Kernel.getProcessManager().resumeProcess(pid));
        }));
        
        blockBtn.addActionListener(e -> handleBulkAction("Block", pids -> {
            pids.forEach(pid -> Kernel.getProcessManager().blockProcess(pid));
        }));
        
        wakeupBtn.addActionListener(e -> handleBulkAction("Wakeup", pids -> {
            pids.forEach(pid -> Kernel.getProcessManager().wakeupProcess(pid));
        }));
        
        dispatchBtn.addActionListener(e -> {
            List<Integer> selected = processTablePanel.getModel().getSelectedPids();
            if (selected.size() == 1) {
                Kernel.getProcessManager().dispatchProcess(selected.get(0));
                processTablePanel.refresh();
                processTablePanel.getModel().clearSelection();
                snackbar.showMessage("Manually Dispatched PID: " + selected.get(0));
            } else if (selected.isEmpty()) {
                new DispatchProcessDialog(SwingUtilities.getWindowAncestor(this), processTablePanel);
            } else {
                snackbar.showMessage("Select exactly one process to dispatch.");
            }
        });

        changePriorityBtn.addActionListener(e -> {
            List<Integer> selected = processTablePanel.getModel().getSelectedPids();
            if (selected.isEmpty()) {
                new ChangePriorityDialog(SwingUtilities.getWindowAncestor(this), processTablePanel);
            } else {
                String val = JOptionPane.showInputDialog(this, "New Priority for " + selected.size() + " process(es):", "1");
                if (val != null) {
                    try {
                        int p = Integer.parseInt(val);
                        selected.forEach(pid -> Kernel.getProcessManager().changePriority(pid, p));
                        processTablePanel.refresh();
                        processTablePanel.getModel().clearSelection();
                        snackbar.showMessage("Priority updated to " + p + " for " + selected.size() + " process(es)");
                    } catch (NumberFormatException ex) {
                        snackbar.showMessage("Invalid priority value.");
                    }
                }
            }
        });
        
        communicationBtn.addActionListener(e -> new ProcessCommunicationDialog());

        schedulerCombo.addActionListener(e -> {
            String selected = (String) schedulerCombo.getSelectedItem();
            if ("FCFS".equals(selected)) {
                Kernel.getProcessManager().useFCFS();
                snackbar.showMessage("Switched to FCFS Scheduler");
            } else if ("Round Robin".equals(selected)) {
                String q = JOptionPane.showInputDialog(this, "Enter Time Quantum:", "2");
                if (q != null) {
                    try {
                        int quantum = Integer.parseInt(q);
                        Kernel.getProcessManager().useRoundRobin(quantum);
                        snackbar.showMessage("Switched to Round Robin (Quantum: " + quantum + ")");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid quantum! Switching back to FCFS.");
                        schedulerCombo.setSelectedItem("FCFS");
                    }
                } else {
                    schedulerCombo.setSelectedItem("FCFS");
                }
            }
        });

        cpuTimer = new Timer(1000, e -> {
            Kernel.getProcessManager().tick();
            systemTimeLabel.setText("Time: " + Kernel.getProcessManager().getSystemTime());
            processTablePanel.refresh();
        });

        startBtn.addActionListener(e -> {
            cpuTimer.start();
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            snackbar.showMessage("CPU Execution Started");
        });

        stopBtn.addActionListener(e -> {
            cpuTimer.stop();
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            snackbar.showMessage("CPU Execution Paused");
        });

        // Initial state
        stopBtn.setEnabled(false);

        // Layout assembly
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(processTablePanel, BorderLayout.CENTER);
        centerPanel.add(queuePanel, BorderLayout.SOUTH);

        // Add snackbar to a container at the bottom
        JPanel snackbarContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        snackbarContainer.setOpaque(false);
        snackbarContainer.add(snackbar);
        
        layeredPane.add(centerPanel, BorderLayout.CENTER);
        
        add(toolbarPanel, BorderLayout.NORTH);
        add(layeredPane, BorderLayout.CENTER);
        add(snackbarContainer, BorderLayout.SOUTH);
    }

    private void handleBulkAction(String actionName, java.util.function.Consumer<List<Integer>> action) {
        List<Integer> selectedPids = processTablePanel.getModel().getSelectedPids();
        if (selectedPids.isEmpty()) {
            snackbar.showMessage("No processes selected for " + actionName);
            return;
        }
        action.accept(selectedPids);
        processTablePanel.refresh();
        processTablePanel.getModel().clearSelection();
        snackbar.showMessage(actionName + " applied to " + selectedPids.size() + " process(es)");
    }
}
