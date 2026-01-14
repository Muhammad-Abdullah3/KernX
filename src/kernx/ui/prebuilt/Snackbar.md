# Snackbar.java Documentation

## Flowchart

```mermaid
graph TD
    A[showMessage] --> B[Set Alpha = 0.0]
    B --> C[Set Visible = True]
    C --> D[Start fadeTimer]
    D --> E{Showing?}
    E -- Yes --> F[Alpha += 0.1]
    F --> G{Alpha >= 1.0?}
    G -- Yes --> H[Start Hide Timer (2.5s)]
    H --> I[Showing = False]
    I --> J[Alpha -= 0.1]
    J --> K{Alpha <= 0.0?}
    K -- Yes --> L[Visible = False]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `9` | `public class Snackbar` | Android-style popup notification for Swing. |
| `32` | `alpha += 0.1f;` | Increases opacity for fade-in effect. |
| `36` | `new Timer(2500, ev -> hideSnackbar())` | Waits for 2.5 seconds before starting fade-out. |
| `73` | `fillRoundRect(...)` | Custom painting with rounded corners and transparency. |

## Code Flow & Dry Run Example

**Scenario**: Process Created successfully.
1. `processManager` notifies listener.
2. `snackbar.showMessage("Created PID 101")` called.
3. Timer starts; opacity goes 0 -> 0.1 -> ... -> 1.0 (Fade In).
4. After 2.5s holding opaque, opacity goes 1.0 -> ... -> 0.0 (Fade Out).
5. Snackbar disappears.
