# ConfigPanel.java Documentation

## Flowchart

![alt text](image-4.png)

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `1` | `package kernx.ui.panels;` | Defines the package this class belongs to. |
| `3` | `import kernx.os.memory.MemoryManager;` | Imports the MemoryManager to call `refreshConfig()`. |
| `4` | `import kernx.ui.prebuilt.UITheme;` | Imports the UI styling constants (Colors, Fonts). |
| `5` | `import kernx.utils.ConfigManager;` | Imports the ConfigManager to read/write persistent settings. |
| `11` | `public class ConfigPanel extends JPanel` | Defines the class, inheriting from Swing's `JPanel`. |
| `13` | `public ConfigPanel() {` | Constructor called when the panel is initialized. |
| `14` | `setLayout(new BorderLayout());` | Sets the top-level layout to BorderLayout. |
| `18` | `JPanel mainContainer = new JPanel(new GridBagLayout());` | Creates a wrapper panel using `GridBagLayout` to center content. |
| `24` | `JPanel card = new JPanel();` | Creates the inner "card" panel that holds the actual form fields. |
| `28` | `GridBagConstraints gbc = ...` | initializes constraints for positioning items in the GridBagLayout. |
| `35` | `JLabel titleLabel = ...` | Creates the header text "System Configuration". |
| `50` | `JTextField pageField = ...` | Creates input field pre-filled with current Page Size from `ConfigManager`. |
| `60` | `JTextField memField = ...` | Creates input field pre-filled with Total Memory from `ConfigManager`. |
| `72` | `JButton saveBtn = ...` | Creates the save button. |
| `85` | `saveBtn.addMouseListener(...)` | Adds a hover effect to brighten the button when mouse enters. |
| `94` | `saveBtn.addActionListener(e -> { ... })` | Defines what happens when the button is clicked. |
| `96` | `int pageSize = Integer.parseInt(...)` | Tries to convert text input to integers. Throws exception if invalid. |
| `99` | `ConfigManager.setInt(...)` | Saves the new values to the configuration storage. |
| `102` | `MemoryManager.getInstance().refreshConfig()` | Triggers the Memory Manager to resize its frame array based on new settings. |
| `104` | `JOptionPane.showMessageDialog(...)` | Shows a popup confirming success. |

## Code Flow & Dry Run Example

**Scenario**: User changes Page Size from `4` to `8`.

1.  **Initialization**:
    *   `ConfigPanel` is instantiated.
    *   `ConfigManager.getPageSize()` returns `4`.
    *   `pageField` is created with text "4".
    *   UI is rendered.
2.  **User Interaction**:
    *   User deletes "4" and types "8" in `pageField`.
    *   User clicks "Save & Restart".
3.  **Event Handling**:
    *   `actionPerformed` lambda is triggered.
    *   `pageField.getText()` returns "8".
    *   `Integer.parseInt("8")` returns integer `8`.
    *   `ConfigManager.setInt("pageSize", 8)` updates the global config.
    *   `MemoryManager.getInstance().refreshConfig()` is called. It recalculates the number of frames (`TotalMem / 8`) and re-initializes `MemoryFrame` list.
    *   Success Dialog appears: "Configuration Saved...".
