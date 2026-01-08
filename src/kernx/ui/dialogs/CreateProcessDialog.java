package kernx.ui.dialogs;

import javax.swing.*;
import java.awt.*;
import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;
import kernx.ui.prebuilt.UITheme;
import javax.swing.border.EmptyBorder;

public class CreateProcessDialog extends JDialog {

    public CreateProcessDialog(Window parent, ProcessTablePanel tablePanel) {
        super(parent, "Create Process", ModalityType.APPLICATION_MODAL);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JTextField ownerField = createTextField();
        JTextField memField = createTextField();
        JTextField prField = createTextField();
        JTextField burstField = createTextField();
        JTextField arrivalField = createTextField();

        // Default values for easier testing
        ownerField.setText("User-" + (Kernel.getProcessManager().getAllProcesses().size() + 1));
        memField.setText("100");
        prField.setText("1");
        burstField.setText("10");
        arrivalField.setText("0");

        addField(formPanel, "Owner:", ownerField, gbc, 0);
        addField(formPanel, "Memory (Kbs):", memField, gbc, 1);
        addField(formPanel, "Priority:", prField, gbc, 2);
        addField(formPanel, "Burst Time:", burstField, gbc, 3);
        addField(formPanel, "Arrival Delay (relative):", arrivalField, gbc, 4);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 15, 20, 15));

        JButton addBtn = UITheme.createStyledButton("Create");
        JButton addAnotherBtn = UITheme.createStyledButton("Create & Add Another");
        JButton cancelBtn = UITheme.createStyledButton("Cancel");

        addBtn.addActionListener(e -> {
            if (createProcess(ownerField, memField, prField, burstField, arrivalField, tablePanel)) {
                dispose();
            }
        });

        addAnotherBtn.addActionListener(e -> {
            if (createProcess(ownerField, memField, prField, burstField, arrivalField, tablePanel)) {
                // Keep some values, clear others or increment
                ownerField.setText("User-" + (Kernel.getProcessManager().getAllProcesses().size() + 1));
                ownerField.requestFocus();
                ownerField.selectAll();
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(addAnotherBtn);
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(450, 400));
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField f = new JTextField(15);
        f.setBackground(new Color(60, 60, 60));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        return f;
    }

    private void addField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.MAIN_FONT);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private boolean createProcess(JTextField o, JTextField m, JTextField p, JTextField b, JTextField a, ProcessTablePanel table) {
        try {
            Kernel.getProcessManager().createProcess(
                    o.getText(),
                    Integer.parseInt(m.getText().trim()),
                    Integer.parseInt(p.getText().trim()),
                    Integer.parseInt(b.getText().trim()),
                    Integer.parseInt(a.getText().trim())
            );
            table.refresh();
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Memory, Priority, Burst and Arrival.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
