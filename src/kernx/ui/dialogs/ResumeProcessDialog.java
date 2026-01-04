package kernx.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;
import kernx.ui.utils.UITheme;

import java.awt.*;

public class ResumeProcessDialog extends JDialog {

    public ResumeProcessDialog(Window parent, ProcessTablePanel tablePanel) {
        super(parent, "Resume Process", ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField pidField = createTextField();
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl = new JLabel("Process ID:");
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        formPanel.add(lbl, gbc);

        gbc.gridx = 1;
        formPanel.add(pidField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JButton actionBtn = UITheme.createStyledButton("Resume");
        JButton cancelBtn = UITheme.createStyledButton("Cancel");

        actionBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(pidField.getText().trim());
                Kernel.getProcessManager().resumeProcess(pid);
                tablePanel.refresh();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric PID.");
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(actionBtn);
        btnPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField f = new JTextField(10);
        f.setBackground(new Color(60, 60, 60));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        return f;
    }
}
