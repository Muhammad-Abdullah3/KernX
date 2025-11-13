package ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessManagementPanel {

    // 🔹 Store created processes
    private List<PCB> processList = new ArrayList<>();

    public void showProcessWindow() {
        JFrame processFrame = new JFrame("Process Management");
        processFrame.setSize(450, 700);
        processFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        processFrame.getContentPane().setBackground(new Color(35, 35, 35));
        processFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 15, 6, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 🔹 Spacer
        gbc.gridy = 0;
        JPanel topSpacer = new JPanel();
        topSpacer.setOpaque(false);
        topSpacer.setPreferredSize(new Dimension(400, 10));
        processFrame.add(topSpacer, gbc);

        // 🔹 Title
        JLabel title = new JLabel("Process Management Panel", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridy = 1;
        processFrame.add(title, gbc);

        Color buttonColor = new Color(28, 90, 81); // Teal

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

            if (op.equals("Create a Process")) {
                // ✅ Real functionality for Create a Process
                btn.addActionListener(e -> openCreateProcessDialog(processFrame));
            } else {
                // Placeholder for other features
                btn.addActionListener(e -> JOptionPane.showMessageDialog(processFrame,
                        op + " functionality is not yet implemented.",
                        "Info", JOptionPane.INFORMATION_MESSAGE));
            }
        }

        // 🔹 Bottom spacer
        gbc.gridy = y;
        JPanel bottomSpacer = new JPanel();
        bottomSpacer.setOpaque(false);
        bottomSpacer.setPreferredSize(new Dimension(400, 10));
        processFrame.add(bottomSpacer, gbc);

        processFrame.setVisible(true);
    }

    // 🔹 Popup dialog for creating a process
    private void openCreateProcessDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Create a New Process", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;


        JLabel ownerLabel = createLabel("Owner:");
        JTextField ownerField = new JTextField();

        JLabel priorityLabel = createLabel("Priority:");
        JTextField priorityField = new JTextField();

        JLabel memLabel = createLabel("Memory Required (KB):");
        JTextField memField = new JTextField();


        gbc.gridy = 2; dialog.add(ownerLabel, gbc);
        gbc.gridy = 3; dialog.add(ownerField, gbc);
        gbc.gridy = 4; dialog.add(priorityLabel, gbc);
        gbc.gridy = 5; dialog.add(priorityField, gbc);
        gbc.gridy = 6; dialog.add(memLabel, gbc);
        gbc.gridy = 7; dialog.add(memField, gbc);

        JButton createBtn = new JButton("Create Process");
        createBtn.setBackground(new Color(28, 90, 81));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Arial", Font.BOLD, 14));
        createBtn.setFocusPainted(false);

        gbc.gridy = 8;
        dialog.add(createBtn, gbc);

        // ✅ Button logic
        createBtn.addActionListener(e -> {
            try {
                String owner = ownerField.getText().trim();
                int priority = Integer.parseInt(priorityField.getText().trim());
                int memReq = Integer.parseInt(memField.getText().trim());

                PCB pcb = new PCB(owner, priority, memReq);
                processList.add(pcb);

                JOptionPane.showMessageDialog(dialog,
                        "Process Created Successfully!\n\n" + pcb,
                        "Process Created", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Please enter valid numeric values for ID, Priority, and Memory.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    // 🔹 Helper methods
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        return lbl;
    }
}
