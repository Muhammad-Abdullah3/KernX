package kernx.ui.panels;

import kernx.os.memory.MemoryFrame;
import kernx.os.memory.MemoryManager;
import kernx.ui.utils.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MemoryManagementPanel extends JPanel {
    private JPanel gridPanel;
    private JLabel statusLabel;

    public MemoryManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Memory Management (Paging Simulation)");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.ACCENT);
        add(title, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        updateGrid();

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        statusLabel = new JLabel("Total Memory: " + MemoryManager.getInstance().getFrames().size() * 4 + "KB");
        footer.add(statusLabel, BorderLayout.WEST);

        JButton refreshBtn = UITheme.createStyledButton("Refresh Visualization");
        refreshBtn.addActionListener(e -> updateGrid());
        footer.add(refreshBtn, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);

        // Auto-refresh timer
        Timer timer = new Timer(1000, e -> updateGrid());
        timer.start();
    }

    private void updateGrid() {
        gridPanel.removeAll();
        List<MemoryFrame> frames = MemoryManager.getInstance().getFrames();
        
        // Calculate grid size based on number of frames
        int numFrames = frames.size();
        int cols = 8;
        int rows = (numFrames == 0) ? 1 : (int) Math.ceil((double) numFrames / cols);
        gridPanel.setLayout(new GridLayout(rows, cols, 5, 5));

        for (MemoryFrame frame : frames) {
            JPanel frameBox = new JPanel(new BorderLayout());
            frameBox.setBackground(frame.isFree() ? UITheme.SUCCESS : UITheme.DANGER);
            frameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            frameBox.setPreferredSize(new Dimension(80, 80));

            JLabel idLabel = new JLabel("#" + frame.getFrameNumber(), SwingConstants.CENTER);
            idLabel.setFont(new Font("Consolas", Font.BOLD, 10));
            idLabel.setForeground(Color.WHITE);
            frameBox.add(idLabel, BorderLayout.NORTH);

            if (!frame.isFree()) {
                JLabel ownerLabel = new JLabel("PID: " + frame.getOwningPid(), SwingConstants.CENTER);
                ownerLabel.setFont(new Font("Consolas", Font.PLAIN, 10));
                ownerLabel.setForeground(Color.WHITE);
                frameBox.add(ownerLabel, BorderLayout.CENTER);

                JLabel pageLabel = new JLabel("P: " + frame.getPageNumber(), SwingConstants.CENTER);
                pageLabel.setFont(new Font("Consolas", Font.PLAIN, 9));
                pageLabel.setForeground(Color.LIGHT_GRAY);
                frameBox.add(pageLabel, BorderLayout.SOUTH);
            }

            gridPanel.add(frameBox);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}
