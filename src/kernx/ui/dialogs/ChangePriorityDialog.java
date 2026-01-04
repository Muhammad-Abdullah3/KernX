package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class ChangePriorityDialog extends JDialog {

    public ChangePriorityDialog(ProcessTablePanel tablePanel) {
        setTitle("Change Priority");
        setModal(true);

        JTextField pidField = new JTextField();
        JTextField priorityField = new JTextField();

        JButton change = new JButton("Change");
        JButton cancel = new JButton("Cancel");

        change.addActionListener(e -> {
            Kernel.getProcessManager()
                    .changePriority(
                            Integer.parseInt(pidField.getText()),
                            Integer.parseInt(priorityField.getText())
                    );
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(new JLabel("New Priority:"));
        add(priorityField);
        add(change);
        add(cancel);

        setSize(300, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
