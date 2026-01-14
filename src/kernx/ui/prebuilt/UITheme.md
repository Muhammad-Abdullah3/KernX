# UITheme.java Documentation

## Flowchart

```mermaid
graph TD
    A[Launch] --> B[applyGlobalTheme]
    B --> C[Set UIManager Backgrounds]
    B --> D[Set UIManager Fonts]
    B --> E[Set Border Presets]
    F[Button Creation] --> G[createStyledButton]
    G --> H[Apply Mouse Listeners (Hover/Click)]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `13-20` | `Color BACKGROUND, ACCENT...` | Central color palette for the dark mode theme. |
| `26` | `applyGlobalTheme()` | Global override for all Swing standard components. |
| `63` | `createStyledButton(...)` | Factory method to create consistent handcrafted buttons. |
| `72` | `addMouseListener(...)` | Implements smooth hover (lighten) and click (accent blue) effects. |

## Code Flow & Dry Run Example

**Scenario**: Button Hover.
1. `mouseEntered` triggered.
2. `btn.setBackground(new Color(60, 60, 60))` (slightly lighter than SURFACE).
3. `mouseExited` triggered.
4. Returns to `SURFACE` (45, 45, 45).
