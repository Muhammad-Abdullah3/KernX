package kernx.ui.dialogs;

import javax.swing.*;

import kernx.os.Kernel;
import kernx.ui.panels.ProcessTablePanel;

public class ResumeProcessDialog extends JDialog {

    public ResumeProcessDialog(ProcessTablePanel tablePanel) {
        setTitle("Resume Process");
        setModal(true);

        JTextField pidField = new JTextField();

        JButton resume = new JButton("Resume");
        JButton cancel = new JButton("Cancel");

        resume.addActionListener(e -> {
            Kernel.getProcessManager()
                    .resumeProcess(Integer.parseInt(pidField.getText()));
            tablePanel.refresh();
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel("Process ID:"));
        add(pidField);
        add(resume);
        add(cancel);

        setSize(250, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
