package kernx.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;
import kernx.ui.utils.UITheme;

public class ChangePriorityDialog extends JDialog {

    public ChangePriorityDialog(Window parent, ProcessTablePanel tablePanel) {
        super(parent, "Change Priority", ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JTextField pidField = createTextField();
        JTextField priorityField = createTextField();
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPid = new JLabel("Process ID:");
        lblPid.setForeground(UITheme.TEXT_SECONDARY);
        formPanel.add(lblPid, gbc);

        gbc.gridx = 1;
        formPanel.add(pidField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPr = new JLabel("New Priority:");
        lblPr.setForeground(UITheme.TEXT_SECONDARY);
        formPanel.add(lblPr, gbc);

        gbc.gridx = 1;
        formPanel.add(priorityField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JButton changeBtn = UITheme.createStyledButton("Change");
        JButton cancelBtn = UITheme.createStyledButton("Cancel");

        changeBtn.addActionListener(e -> {
            try {
                Kernel.getProcessManager().changePriority(
                        Integer.parseInt(pidField.getText().trim()),
                        Integer.parseInt(priorityField.getText().trim())
                );
                tablePanel.refresh();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(changeBtn);
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
