package kernx.ui.panels;

import javax.swing.*;

public class ConfigPanel extends JPanel {

    public ConfigPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Kernel Configuration (Page Size, CPU Count, etc.)"));
    }
}
