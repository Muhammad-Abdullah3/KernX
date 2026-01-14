# MemoryManager.java Documentation

## Flowchart

```mermaid
graph TD
    A[Start: allocate(PCB)] --> B[Calculate NumPages Needed]
    B --> C[Create PageTable]
    C --> D{For Each Page}
    D --> E[findFreeFrame]
    E --> F{Is Frame Free?}
    F -- Yes --> G[Assign Frame to Page]
    F -- No --> H[lruReplace]
    H --> I[Find Victim Frame (LRU)]
    I --> J[Evict Victim Page]
    J --> G
    G --> K{More Pages?}
    K -- Yes --> D
    K -- No --> L[Return True]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `7` | `public class MemoryManager` | Singleton class managing physical memory frames. |
| `24` | `refreshConfig()` | Re-initializes memory frames based on ConfigManager settings. |
| `35` | `allocate(PCB pcb)` | Main entry point to allocate memory for a new process. |
| `37` | `int numPages = ceil(req / pageSize)` | Calculates how many logical pages the process needs. |
| `41` | `for (Page page : pt.getPages())` | Iterates through each required page to find a physical home. |
| `42` | `findFreeFrame()` | Scans the `frames` list for one with `isFree() == true`. |
| `44` | `lruReplace()` | Called if no free frames exist. Implements Least Recently Used eviction. |
| `59` | `deallocate(PCB pcb)` | Frees all frames owned by the process when it terminates. |
| `89` | `for (MemoryFrame frame : frames)` | Iterates all frames to find the one with the oldest timestamp. |

## Code Flow & Dry Run Example

**Scenario**: Total Memory 8KB, Page Size 4KB (Total 2 Frames).
*   **State**: Frame 0 (P1), Frame 1 (P2). Both full.

1.  **Request**: P3 needs 4KB (1 Page).
2.  `allocate(P3)` called.
3.  `findFreeFrame()` returns `null` (Frames 0, 1 occupied).
4.  `lruReplace()` Triggered:
    *   Checks P1's page timestamp vs P2's page timestamp.
    *   Assume P1 was accessed at T=5, P2 at T=10.
    *   **Victim**: P1 (Frame 0).
5.  **Eviction**:
    *   P1's Page Table updated: Page 0 `frameNumber` = -1.
    *   Frame 0 marked FREE, then immediately seized.
6.  **Allocation**:
    *   Frame 0 assigned to P3.
    *   Return `true`.
