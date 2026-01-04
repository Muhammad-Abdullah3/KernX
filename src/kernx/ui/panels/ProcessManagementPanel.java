
package kernx.ui.panels;

import javax.swing.*;
import java.awt.*;

import kernx.os.Kernel;
import kernx.ui.dialogs.*;

public class ProcessManagementPanel extends JPanel {

    private final ProcessTablePanel processTablePanel;

    public ProcessManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        // LEFT: Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(9, 1, 10, 10));

        JButton createBtn = new JButton("Create Process");
        JButton destroyBtn = new JButton("Destroy Process");
        JButton suspendBtn = new JButton("Suspend Process");
        JButton resumeBtn = new JButton("Resume Process");
        JButton blockBtn = new JButton("Block Process");
        JButton wakeupBtn = new JButton("Wakeup Process");
        JButton dispatchBtn = new JButton("Dispatch Process");
        JButton changePriorityBtn = new JButton("Change Priority");
        JButton communicationBtn = new JButton("Process Communication");

        buttonPanel.add(createBtn);
        buttonPanel.add(destroyBtn);
        buttonPanel.add(suspendBtn);
        buttonPanel.add(resumeBtn);
        buttonPanel.add(blockBtn);
        buttonPanel.add(wakeupBtn);
        buttonPanel.add(dispatchBtn);
        buttonPanel.add(changePriorityBtn);
        buttonPanel.add(communicationBtn);

        // RIGHT: Process Table
        processTablePanel = new ProcessTablePanel();
        QueueVisualizationPanel queuePanel = new QueueVisualizationPanel();
        processTablePanel.setQueueVisualizationPanel(queuePanel);

        // Button Actions
        createBtn.addActionListener(e ->
                new CreateProcessDialog(processTablePanel));

        destroyBtn.addActionListener(e ->
                new DestroyProcessDialog(processTablePanel));

        suspendBtn.addActionListener(e ->
                new SuspendProcessDialog(processTablePanel));

        resumeBtn.addActionListener(e ->
                new ResumeProcessDialog(processTablePanel));

        blockBtn.addActionListener(e ->
                new BlockProcessDialog(processTablePanel));

        wakeupBtn.addActionListener(e ->
                new WakeupProcessDialog(processTablePanel));

        dispatchBtn.addActionListener(e ->
                new DispatchProcessDialog(processTablePanel));

        changePriorityBtn.addActionListener(e ->
                new ChangePriorityDialog(processTablePanel));

        communicationBtn.addActionListener(e ->
                new ProcessCommunicationDialog());
        Timer cpuTimer = new Timer(1000, e -> {
            Kernel.getProcessManager().tick();
            processTablePanel.refresh();
        });
        cpuTimer.start();

        // Add to main layout
        add(buttonPanel, BorderLayout.WEST);
        add(processTablePanel, BorderLayout.CENTER);
        add(queuePanel, BorderLayout.EAST);
    }
}