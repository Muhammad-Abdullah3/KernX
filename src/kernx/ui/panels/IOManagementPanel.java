package kernx.ui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import kernx.os.Kernel;
import kernx.os.data.PCB;
import kernx.os.io.IODevice;
import kernx.ui.prebuilt.UITheme;

public class IOManagementPanel extends JPanel {

    private DefaultTableModel deviceTableModel;
    private JTable deviceTable;
    private JComboBox<String> processCombo; 
    private JComboBox<String> deviceCombo;

    public IOManagementPanel() {
        UITheme.applyGlobalTheme();
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header / Title
        JLabel title = new JLabel("I/O Device Management");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.ACCENT);
        add(title, BorderLayout.NORTH);

        // Device Table
        String[] columns = {"Device Name", "Type", "Status", "Owner (PID)"};
        deviceTableModel = new DefaultTableModel(columns, 0);
        deviceTable = new JTable(deviceTableModel);
        JScrollPane scrollPane = new JScrollPane(deviceTable);

        // Control Panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Simulate I/O Request"));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionPanel.setOpaque(false);
        
        processCombo = new JComboBox<>();
        deviceCombo = new JComboBox<>();
        
        selectionPanel.add(new JLabel("Process (PID):"));
        selectionPanel.add(processCombo);
        selectionPanel.add(new JLabel("Device:"));
        selectionPanel.add(deviceCombo);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        
        JButton requestBtn = UITheme.createStyledButton("Request Device");
        JButton releaseBtn = UITheme.createStyledButton("Release Device");
        JButton refreshBtn = UITheme.createStyledButton("Refresh");

        btnPanel.add(requestBtn);
        btnPanel.add(releaseBtn);
        btnPanel.add(refreshBtn);

        controlPanel.add(selectionPanel);
        controlPanel.add(btnPanel);

        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Populate
        refreshData();

        // Actions
        requestBtn.addActionListener(e -> handleRequest());
        releaseBtn.addActionListener(e -> handleRelease());
        refreshBtn.addActionListener(e -> refreshData());
    }

    private void refreshData() {
        // Table
        deviceTableModel.setRowCount(0);
        List<IODevice> devices = Kernel.getIOManager().getDevices();
        
        deviceCombo.removeAllItems();

        for (IODevice dev : devices) {
            String owner = (dev.getCurrentOwnerPid() == -1) ? "-" : String.valueOf(dev.getCurrentOwnerPid());
            String status = dev.isBusy() ? "BUSY" : "FREE";
            deviceTableModel.addRow(new Object[]{dev.getName(), dev.getType(), status, owner});
            
            deviceCombo.addItem(dev.getName());
        }

        // Process Combo
        processCombo.removeAllItems();
        List<PCB> procs = Kernel.getProcessManager().getAllProcesses();
        for (PCB p : procs) {
            processCombo.addItem(String.valueOf(p.getPid()));
        }
    }

    private void handleRequest() {
        String pidStr = (String) processCombo.getSelectedItem();
        String devName = (String) deviceCombo.getSelectedItem();

        if (pidStr == null || devName == null) return;

        int pid = Integer.parseInt(pidStr);
        boolean success = Kernel.getIOManager().requestDevice(pid, devName);

        if (success) {
            JOptionPane.showMessageDialog(this, "Device allocated to PID " + pid + ". Process blocked.");
        } else {
            JOptionPane.showMessageDialog(this, "Device is BUSY or unavailable.");
        }
        refreshData();
    }

    private void handleRelease() {
        String pidStr = (String) processCombo.getSelectedItem();
        String devName = (String) deviceCombo.getSelectedItem();

        if (pidStr == null || devName == null) return;
        
        int pid = Integer.parseInt(pidStr);
        Kernel.getIOManager().releaseDevice(pid, devName);
        
        JOptionPane.showMessageDialog(this, "Release command sent.");
        
        refreshData();
    }
}
