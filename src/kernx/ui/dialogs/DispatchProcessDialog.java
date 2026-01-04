package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class DispatchProcessDialog extends JDialog {

    public DispatchProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Dispatch Process");
        setModal(true);

        JTextField pidField = new JTextField();

        JButton dispatch = new JButton("Dispatch");
        JButton cancel = new JButton("Cancel");

        dispatch.addActionListener(e -> {
            Kernel.getProcessManager()
                    .dispatchProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(dispatch);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
