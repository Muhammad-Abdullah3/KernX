package controlpanel;

import javax.swing.*;
import java.awt.*;

public class ProcessManagementPanel {

    public void showProcessWindow() {
        JFrame processFrame = new JFrame("Process Management");
        processFrame.setSize(450, 700);
        processFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        processFrame.getContentPane().setBackground(new Color(35, 35, 35));
        processFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 15, 6, 15); // even spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 🔹 Small top spacer for balanced look
        gbc.gridy = 0;
        JPanel topSpacer = new JPanel();
        topSpacer.setOpaque(false);
        topSpacer.setPreferredSize(new Dimension(400, 10)); // small top gap
        processFrame.add(topSpacer, gbc);

        // 🔹 Title Label
        JLabel title = new JLabel("Process Management Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridy = 1;
        processFrame.add(title, gbc);

        Color buttonColor = new Color(28, 90, 81); // Teal shade

        // 🔹 Buttons for process operations
        String[] operations = {
                "Create a Process",
                "Destroy a Process",
                "Suspend a Process",
                "Resume a Process",
                "Block a Process",
                "Wakeup a Process",
                "Dispatch a Process",
                "Change Process Priority",
                "Process Communication"
        };

        int y = 2;
        for (String op : operations) {
            JButton btn = createButton(op, buttonColor, Color.WHITE);
            gbc.gridy = y++;
            processFrame.add(btn, gbc);

            btn.addActionListener(e -> JOptionPane.showMessageDialog(processFrame,
                    op + " functionality is not yet implemented.",
                    "Info", JOptionPane.INFORMATION_MESSAGE));
        }

        // 🔹 Bottom spacer — matches top gap
        gbc.gridy = y;
        gbc.weighty = 0; // no extra stretch
        JPanel bottomSpacer = new JPanel();
        bottomSpacer.setOpaque(false);
        bottomSpacer.setPreferredSize(new Dimension(400, 10)); // same as top
        processFrame.add(bottomSpacer, gbc);

        processFrame.setVisible(true);
    }

    // 🔹 Helper method for styled buttons
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }
}