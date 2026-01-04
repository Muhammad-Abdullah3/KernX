package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class DestroyProcessDialog extends JDialog {

    public DestroyProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Destroy Process");
        setModal(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextField pidField = new JTextField();

        add(new JLabel("Process ID:"));
        add(pidField);

        JButton destroy = new JButton("Destroy");
        JButton cancel = new JButton("Cancel");

        destroy.addActionListener(e -> {
            Kernel.getProcessManager()
                    .destroyProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        add(destroy);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
