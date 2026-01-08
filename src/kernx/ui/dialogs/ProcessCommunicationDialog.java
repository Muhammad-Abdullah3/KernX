package kernx.ui.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import kernx.ui.prebuilt.UITheme;

public class ProcessCommunicationDialog extends JDialog {

    public ProcessCommunicationDialog() {
        setTitle("Process Communication");
        setModal(true);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel info = new JLabel("IPC will be implemented in Phase-4", SwingConstants.CENTER);
        info.setForeground(UITheme.TEXT_SECONDARY);
        mainPanel.add(info, BorderLayout.CENTER);

        JButton okBtn = UITheme.createStyledButton("OK");
        okBtn.addActionListener(e -> dispose());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UITheme.BACKGROUND);
        btnPanel.add(okBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
