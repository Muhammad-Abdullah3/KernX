# ProcessTableModel.java Documentation

## Flowchart

```mermaid
graph TD
    A[UI Refresh Request] --> B[getValueAt(row, col)]
    B --> C{Col Index?}
    C -- 0 --> D[Return Selection Box]
    C -- 1 --> E[Return PID]
    C -- 3 --> F[Return State]
    C -- 7 --> G[Return Remaining Burst]
    
    H[User Clicks Checkbox] --> I[setValueAt(true, row, 0)]
    I --> J[Add PID to Selection Set]
    J --> K[Fire Table Event]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `11` | `extends AbstractTableModel` | Standard Swing class for binding data to a JTable. |
| `13` | `String[] columns` | Defines headers: PID, Owner, State, etc. |
| `20` | `setProcesses(List<PCB>)` | Updates the entire data source whenever `ProcessManager` adds/removes processes. |
| `61` | `getValueAt(...)` | Maps row index to `PCB` object, and column index to `PCB.field`. |
| `80` | `setValueAt(...)` | Handles checkbox toggling for multi-select operations. |

## Code Flow & Dry Run Example

**Scenario**: Rendering 1 Process in Table.
1.  **Input**: List containing `PCB(PID=1, State=READY)`.
2.  `getRowCount()` returns 1.
3.  `getValueAt(0, 1)` called (PID Column).
    *   Gets PCB at index 0.
    *   Returns `pcb.getPid()` -> 1.
4.  `getValueAt(0, 3)` called (State Column).
    *   Returns `pcb.getState()` -> READY.
5.  **Result**: UI shows "1 | READY".
