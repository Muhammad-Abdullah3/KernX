package kernx.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import kernx.ui.panels.*;

import kernx.ui.utils.UITheme;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("KernXOS - Control Panel");

        UITheme.applyGlobalTheme();

        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout(20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 0, 10, 0));

        JLabel title = new JLabel("KernXOS Control Panel", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.ACCENT);
        header.add(title, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(5, 1, 15, 15));
        buttonPanel.setBorder(new EmptyBorder(20, 50, 50, 50));

        JButton processBtn = UITheme.createStyledButton("Process Management");
        JButton memoryBtn = UITheme.createStyledButton("Memory Management");
        JButton ioBtn = UITheme.createStyledButton("I/O Management");
        JButton otherBtn = UITheme.createStyledButton("Other Operations");
        JButton configBtn = UITheme.createStyledButton("Configuration");

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

        add(header, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setSize(500, 600);
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
