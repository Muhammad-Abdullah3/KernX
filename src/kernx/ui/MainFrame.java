package kernx.ui;

import javax.swing.*;
import java.awt.*;
import kernx.ui.panels.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("KernXOS - Control Panel");

        // Linux styled dark theme (simple custom colors)
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Button.background", new Color(60, 60, 60));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Label.foreground", Color.WHITE);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("KernXOS Control Panel", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 22));
        title.setForeground(Color.GREEN);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton processBtn = new JButton("Process Management");
        JButton memoryBtn = new JButton("Memory Management");
        JButton ioBtn = new JButton("I/O Management");
        JButton otherBtn = new JButton("Other Operations");
        JButton configBtn = new JButton("Configuration");

        // Add action listeners to open different panels
        processBtn.addActionListener(e -> openPanel(new ProcessManagementPanel()));
        memoryBtn.addActionListener(e -> openPanel(new MemoryManagementPanel()));
        ioBtn.addActionListener(e -> openPanel(new IOManagementPanel()));
        otherBtn.addActionListener(e -> openPanel(new OtherOperationsPanel()));
        configBtn.addActionListener(e -> openPanel(new ConfigPanel()));

        buttonPanel.add(processBtn);
        buttonPanel.add(memoryBtn);
        buttonPanel.add(ioBtn);
        buttonPanel.add(otherBtn);
        buttonPanel.add(configBtn);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void openPanel(JPanel panel) {
        JFrame frame = new JFrame(panel.getClass().getSimpleName());
        frame.setContentPane(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
