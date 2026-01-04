package kernx.ui.dialogs;

import javax.swing.*;
import java.awt.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class CreateProcessDialog extends JDialog {

    public CreateProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Create Process");
        setModal(true);
        setLayout(new GridLayout(4, 2, 10, 10));

        JTextField ownerField = new JTextField();
        JTextField memField = new JTextField();
        JTextField prField = new JTextField();

        add(new JLabel("Owner:"));
        add(ownerField);
        add(new JLabel("Memory Required (Kbs):"));
        add(memField);
        add(new JLabel("Priority:"));
        add(prField);

        JButton ok = new JButton("Create");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            Kernel.getProcessManager().createProcess(
                    ownerField.getText(),
                    Integer.parseInt(memField.getText()),
                    Integer.parseInt(prField.getText())
            );
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        add(ok);
        add(cancel);

        setSize(300, 220);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
