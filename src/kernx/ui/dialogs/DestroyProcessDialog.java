package kernx.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;
import kernx.ui.utils.UITheme;

public class DestroyProcessDialog extends JDialog {

    public DestroyProcessDialog(Window parent, ProcessTablePanel tablePanel) {
        super(parent, "Destroy Process", ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField pidField = new JTextField(10);
        pidField.setBackground(new Color(60, 60, 60));
        pidField.setForeground(Color.WHITE);
        pidField.setCaretColor(Color.WHITE);
        pidField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl = new JLabel("Process ID:");
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        formPanel.add(lbl, gbc);

        gbc.gridx = 1;
        formPanel.add(pidField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JButton destroyBtn = UITheme.createStyledButton("Destroy");
        JButton cancelBtn = UITheme.createStyledButton("Cancel");

        destroyBtn.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(pidField.getText().trim());
                Kernel.getProcessManager().destroyProcess(pid);
                tablePanel.refresh();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric PID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(destroyBtn);
        btnPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
