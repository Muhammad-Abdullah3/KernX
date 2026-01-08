package kernx.ui.panels;

import kernx.os.memory.MemoryManager;
import kernx.ui.utils.UITheme;
import kernx.utils.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfigPanel extends JPanel {

    public ConfigPanel() {
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        
        // Main container with padding to center the content
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(UITheme.BACKGROUND);
        // Add outer padding to frame the card nicely
        mainContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        // The "Card" panel holding the form
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(UITheme.SURFACE);
        // Create a subtle border and padding for the card
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            new EmptyBorder(30, 50, 40, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 25, 0); // Bottom padding for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("System Configuration");
        titleLabel.setFont(UITheme.TITLE_FONT.deriveFont(22f)); // Larger header font
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        card.add(titleLabel, gbc);

        // Reset constraints for form fields
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 8, 0); // Spacing between label and field
        
        // Row 1: Page Size
        gbc.gridy++;
        JLabel pageLabel = new JLabel("Page Size (KB)");
        pageLabel.setFont(UITheme.MAIN_FONT);
        pageLabel.setForeground(UITheme.TEXT_SECONDARY);
        card.add(pageLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0); // Spacing after field
        JTextField pageField = new JTextField(String.valueOf(ConfigManager.getPageSize()));
        pageField.setFont(UITheme.MONO_FONT.deriveFont(14f));
        pageField.setCaretColor(UITheme.ACCENT);
        // Add a preferred size to ensure it's not too small
        pageField.setPreferredSize(new Dimension(250, 35));
        card.add(pageField, gbc);

        // Row 2: Total Memory
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel memLabel = new JLabel("Total Physical Memory (KB)");
        memLabel.setFont(UITheme.MAIN_FONT);
        memLabel.setForeground(UITheme.TEXT_SECONDARY);
        card.add(memLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0); // Spacing after field
        JTextField memField = new JTextField(String.valueOf(ConfigManager.getTotalMemory()));
        memField.setFont(UITheme.MONO_FONT.deriveFont(14f));
        memField.setCaretColor(UITheme.ACCENT);
        memField.setPreferredSize(new Dimension(250, 35));
        card.add(memField, gbc);

        // Save Button
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton saveBtn = new JButton("Save & Restart Memory Manager");
        saveBtn.setFont(UITheme.MAIN_FONT.deriveFont(Font.BOLD));
        saveBtn.setBackground(UITheme.ACCENT);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(250, 40));

        // Hover effect for primary button
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(UITheme.ACCENT.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(UITheme.ACCENT);
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                int pageSize = Integer.parseInt(pageField.getText());
                int totalMem = Integer.parseInt(memField.getText());
                
                ConfigManager.setInt("pageSize", pageSize);
                ConfigManager.setInt("totalMemory", totalMem);
                
                MemoryManager.getInstance().refreshConfig();
                
                JOptionPane.showMessageDialog(this, "Configuration Saved and Memory Manager Reset.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(saveBtn, gbc);

        // Add card to main container
        mainContainer.add(card);
        
        // Add main container to panel
        add(mainContainer, BorderLayout.CENTER);
    }
}
