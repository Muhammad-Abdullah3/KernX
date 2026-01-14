# Page.java Documentation

## Flowchart

```mermaid
graph TD
    A[Page Created] --> B[Page Number Set]
    B --> C[Frame = -1 (Not in RAM)]
    
    D[Page Fault / Load] --> E[Assign Frame Number]
    E --> F[Set Present = true]
    F --> G[Update Timestamp (Touch)]
    
    H[Eviction / Swap Out] --> I[Set Frame = -1]
    I --> J[Set Present = false]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `3` | `public class Page` | Represents a logical block of memory seen by the process. |
| `5` | `private int frameNumber = -1;` | Stores physical location. -1 means it's not currently in RAM. |
| `7` | `private long lastReferencedTime;` | Timestamp for LRU (Least Recently Used) replacement algorithm. |
| `22` | `public void setFrameNumber(int frameNumber)` | Maps this logical page to a physical frame. |
| `25` | `touch()` | Updates the access time to "now", preventing eviction. |
| `40` | `public void touch()` | Helper method called whenever the page is accessed/executed. |

## Code Flow & Dry Run Example

**Scenario**: CPU executes an instruction on Page 0.

1.  **Access**: `ProcessManager` detects execution on Page 0.
2.  **Action**: Calls `page.touch()`.
    *   `lastReferencedTime` updates to `System.currentTimeMillis()`.
3.  **Benefit**: When `MemoryManager` looks for a victim page to remove, this page will have a very recent timestamp, so it will be saved (NOT evicted).
