package kernx.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.os.ipc.Message;
import kernx.os.sync.Semaphore;
import kernx.ui.prebuilt.UITheme;

public class ProcessCommunicationDialog extends JDialog {

    private JTextArea chatArea;
    private JComboBox<String> senderCombo;
    private JComboBox<String> receiverCombo;
    private JTextField messageField;
    
    private DefaultTableModel semaphoreTableModel;
    private JTable semaphoreTable;

    public ProcessCommunicationDialog() {
        setTitle("Process Communication & Synchronization");
        setModal(true);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("IPC (Message Passing)", createIPCPanel());
        tabbedPane.addTab("Synchronization (Semaphores)", createSyncPanel());

        add(tabbedPane, BorderLayout.CENTER);

        JButton closeBtn = UITheme.createStyledButton("Close");
        closeBtn.addActionListener(e -> dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UITheme.BACKGROUND);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createIPCPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // Refresh Chat Log
        refreshChatLog();

        // Control Panel
        JPanel controls = new JPanel(new GridLayout(2, 1, 5, 5));
        controls.setOpaque(false);

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setOpaque(false);
        selectionPanel.add(new JLabel("From:"));
        senderCombo = new JComboBox<>();
        selectionPanel.add(senderCombo);
        selectionPanel.add(new JLabel("To:"));
        receiverCombo = new JComboBox<>();
        selectionPanel.add(receiverCombo);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setOpaque(false);
        messageField = new JTextField();
        JButton sendBtn = UITheme.createStyledButton("Send Message");
        inputPanel.add(new JLabel("Message: "), BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        controls.add(selectionPanel);
        controls.add(inputPanel);

        panel.add(new JLabel("Message Log:", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        // Populate Combos
        populateProcessCombos();

        sendBtn.addActionListener(e -> sendMessage());

        return panel;
    }

    private JPanel createSyncPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Semaphore Table
        String[] columns = {"ID", "Name", "Value", "Blocked Queue Size"};
        semaphoreTableModel = new DefaultTableModel(columns, 0);
        semaphoreTable = new JTable(semaphoreTableModel);
        JScrollPane scrollPane = new JScrollPane(semaphoreTable);

        // Controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controls.setOpaque(false);
        
        JButton createSemBtn = UITheme.createStyledButton("Create Semaphore");
        JButton waitBtn = UITheme.createStyledButton("Wait (P)");
        JButton signalBtn = UITheme.createStyledButton("Signal (V)");

        controls.add(createSemBtn);
        controls.add(waitBtn);
        controls.add(signalBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        refreshSemaphoreTable();

        createSemBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Semaphore Name:");
            if (name != null && !name.trim().isEmpty()) {
                 String valStr = JOptionPane.showInputDialog(this, "Initial Value:", "1");
                 try {
                     int val = Integer.parseInt(valStr);
                     Kernel.getSyncManager().createSemaphore(name, val);
                     refreshSemaphoreTable();
                 } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "Invalid Number");
                 }
            }
        });

        waitBtn.addActionListener(e -> {
            int row = semaphoreTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a semaphore first.");
                return;
            }
            int semId = (int) semaphoreTableModel.getValueAt(row, 0);
            Semaphore sem = Kernel.getSyncManager().getSemaphore(semId);
            
            // For simulation, we assume the user picks a "Running" process to perform the wait
            // Or we just pick the first selected process from a picker
            String[] pids = getActivePids();
            if (pids.length == 0) return;
            String pidStr = (String) JOptionPane.showInputDialog(this, 
                "Select Process to perform WAIT:", "Process Selection", 
                JOptionPane.QUESTION_MESSAGE, null, pids, pids[0]);
            
            if (pidStr != null) {
                int pid = Integer.parseInt(pidStr);
                PCB pcb = Kernel.getProcessManager().getProcess(pid);
                if (pcb != null) {
                    sem.wait(pcb);
                    refreshSemaphoreTable();
                    JOptionPane.showMessageDialog(this, "Process " + pid + " performed WAIT on " + sem.getName());
                }
            }
        });

        signalBtn.addActionListener(e -> {
            int row = semaphoreTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a semaphore first.");
                return;
            }
            int semId = (int) semaphoreTableModel.getValueAt(row, 0);
            Semaphore sem = Kernel.getSyncManager().getSemaphore(semId);
            
            PCB woken = sem.signal();
            refreshSemaphoreTable();
            if (woken != null) {
                JOptionPane.showMessageDialog(this, "Process " + woken.getPid() + " was woken up!");
            } else {
                JOptionPane.showMessageDialog(this, "Signal sent. No process was waiting.");
            }
        });

        return panel;
    }

    private void populateProcessCombos() {
        senderCombo.removeAllItems();
        receiverCombo.removeAllItems();
        List<PCB> procs = Kernel.getProcessManager().getAllProcesses();
        for (PCB p : procs) {
            String item = String.valueOf(p.getPid());
            senderCombo.addItem(item);
            receiverCombo.addItem(item);
        }
    }
    
    private String[] getActivePids() {
        List<PCB> procs = Kernel.getProcessManager().getAllProcesses();
        String[] pids = new String[procs.size()];
        for(int i=0; i<procs.size(); i++) {
            pids[i] = String.valueOf(procs.get(i).getPid());
        }
        return pids;
    }

    private void sendMessage() {
        String fromStr = (String) senderCombo.getSelectedItem();
        String toStr = (String) receiverCombo.getSelectedItem();
        String content = messageField.getText();

        if (fromStr != null && toStr != null && !content.isEmpty()) {
            int from = Integer.parseInt(fromStr);
            int to = Integer.parseInt(toStr);
            
            Message msg = new Message(from, to, content);
            Kernel.getIPCManager().sendMessage(msg);
            
            messageField.setText("");
            refreshChatLog();
        }
    }

    private void refreshChatLog() {
        StringBuilder sb = new StringBuilder();
        List<PCB> procs = Kernel.getProcessManager().getAllProcesses();
        for (PCB p : procs) {
            List<Message> msgs = Kernel.getIPCManager().peekAllMessages(p.getPid());
            for (Message m : msgs) {
                sb.append(m.toString()).append("\n");
            }
        }
        chatArea.setText(sb.toString());
    }

    private void refreshSemaphoreTable() {
        semaphoreTableModel.setRowCount(0);
        for (Semaphore s : Kernel.getSyncManager().getAllSemaphores()) {
            semaphoreTableModel.addRow(new Object[]{s.getId(), s.getName(), s.getValue(), s.getQueueSize()});
        }
    }
}
