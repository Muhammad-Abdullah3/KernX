package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class BlockProcessDialog extends JDialog {

    public BlockProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Block Process");
        setModal(true);

        JTextField pidField = new JTextField();

        JButton block = new JButton("Block");
        JButton cancel = new JButton("Cancel");

        block.addActionListener(e -> {
            Kernel.getProcessManager()
                    .blockProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(block);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
