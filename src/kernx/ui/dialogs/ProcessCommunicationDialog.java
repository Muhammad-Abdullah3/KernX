package kernx.ui.dialogs;

import javax.swing.*;

public class ProcessCommunicationDialog extends JDialog {

    public ProcessCommunicationDialog() {
        setTitle("Process Communication");
        setModal(true);

        add(new JLabel("IPC will be implemented in Phase-4"));

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dispose());
        add(ok);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
