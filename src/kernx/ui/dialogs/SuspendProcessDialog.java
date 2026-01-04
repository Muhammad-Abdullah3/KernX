package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class SuspendProcessDialog extends JDialog {

    public SuspendProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Suspend Process");
        setModal(true);

        JTextField pidField = new JTextField();

        JButton suspend = new JButton("Suspend");
        JButton cancel = new JButton("Cancel");

        suspend.addActionListener(e -> {
            Kernel.getProcessManager()
                    .suspendProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(suspend);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
