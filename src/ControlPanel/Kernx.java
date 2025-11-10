package ControlPanel;

import javax.swing.*;
import java.awt.*;

public class Kernx {

    // Show main control panel window
    public void showMainWindow() {
        JFrame mainFrame = new JFrame("KernX-OS Control Panel");
        mainFrame.setSize(450, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(new Color(45, 45, 45)); // dark background
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 40, 15, 40); // spacing around buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("KernX-OS Control Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE); // text white for dark background
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainFrame.add(title, gbc);

        // Common button color
        Color buttonColor = new Color(70, 130, 180); // Steel Blue

        // Buttons only
        JButton processBtn = createButton("Process Management", buttonColor, Color.WHITE);
        gbc.gridy = 1;
        mainFrame.add(processBtn, gbc);
        processBtn.addActionListener(e -> new controlpanel.ProcessManagementPanel().showProcessWindow());


        JButton memoryBtn = createButton("Memory Management", buttonColor, Color.WHITE);
        gbc.gridy = 2;
        mainFrame.add(memoryBtn, gbc);

        JButton ioBtn = createButton("I/O Management", buttonColor, Color.WHITE);
        gbc.gridy = 3;
        mainFrame.add(ioBtn, gbc);

        JButton otherBtn = createButton("Other Operations", buttonColor, Color.WHITE);
        gbc.gridy = 4;
        mainFrame.add(otherBtn, gbc);

        JButton kernelBtn = createButton("Kernel Configuration", buttonColor, Color.WHITE);
        gbc.gridy = 5;
        mainFrame.add(kernelBtn, gbc);

        mainFrame.setVisible(true);
    }

    // Helper method to create styled buttons with increased width and height
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 50)); // wider buttons
        return btn;
}
}