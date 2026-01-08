package kernx.ui.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Snackbar extends JPanel {

    private final JLabel messageLabel;
    private final Timer fadeTimer;
    private float alpha = 0.0f;
    private boolean showing = false;

    public Snackbar() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 40, 220));
        setBorder(new EmptyBorder(10, 25, 10, 25));
        setOpaque(false);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(UITheme.MAIN_FONT);
        add(messageLabel, BorderLayout.CENTER);

        fadeTimer = new Timer(50, null);
        fadeTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showing) {
                    alpha += 0.1f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        fadeTimer.stop();
                        new Timer(2500, ev -> hideSnackbar()).start();
                    }
                } else {
                    alpha -= 0.1f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        fadeTimer.stop();
                        setVisible(false);
                    }
                }
                repaint();
            }
        });

        setVisible(false);
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
        setVisible(true);
        showing = true;
        alpha = 0.0f;
        fadeTimer.start();
    }

    private void hideSnackbar() {
        showing = false;
        fadeTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        super.paintComponent(g2d);
        g2d.dispose();
    }
}
