package kernx.ui.prebuilt;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITheme {

    // Colors
    public static final Color BACKGROUND = new Color(30, 30, 30);
    public static final Color SURFACE = new Color(45, 45, 45);
    public static final Color ACCENT = new Color(53, 116, 240); // Qt Blue
    public static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    public static final Color TEXT_SECONDARY = new Color(160, 160, 160);
    public static final Color SUCCESS = new Color(73, 156, 84);
    public static final Color DANGER = new Color(192, 57, 43);

    // Fonts
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font MONO_FONT = new Font("Consolas", Font.PLAIN, 12);

    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", MAIN_FONT);
        UIManager.put("Button.font", MAIN_FONT);
        UIManager.put("Button.background", SURFACE);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        
        // TextField styling
        UIManager.put("TextField.background", new Color(40, 40, 40));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretColor", Color.WHITE);
        UIManager.put("TextField.selectionBackground", ACCENT);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // OptionPane (Dialogs) styling
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.buttonFont", MAIN_FONT);
        
        // ScrollBar styling
        UIManager.put("ScrollBar.thumb", SURFACE);
        UIManager.put("ScrollBar.track", BACKGROUND);
        UIManager.put("ScrollBar.width", 10);

        // ComboBox styling
        UIManager.put("ComboBox.background", SURFACE);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", ACCENT);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(new Color(80, 80, 80)));
    }

    public static JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(SURFACE);
        btn.setForeground(TEXT_PRIMARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(5, 15, 5, 15));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SURFACE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(ACCENT);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(new Color(60, 60, 60));
            }
        });

        return btn;
    }

    public static Border createPanelBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                title,
                0,
                0,
                TITLE_FONT,
                TEXT_SECONDARY
        );
    }
}
