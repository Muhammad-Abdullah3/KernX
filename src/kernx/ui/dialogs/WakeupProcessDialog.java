package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class WakeupProcessDialog extends JDialog {

    public WakeupProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Wakeup Process");
        setModal(true);

        JTextField pidField = new JTextField();

        JButton wakeup = new JButton("Wakeup");
        JButton cancel = new JButton("Cancel");

        wakeup.addActionListener(e -> {
            Kernel.getProcessManager()
                    .wakeupProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(wakeup);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
