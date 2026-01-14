# RegisterSet.java Documentation

## Explanation

Simulates the CPU registers saved during a Context Switch.

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `4` | `public class RegisterSet` | Holds AX, BX, CX, DX, PC, SP. |
| `6` | `private int AX, BX, ...` | General purpose registers. |
| `15` | `save()` | Snapshot current CPU state (Context Save). |
| `20` | `restore()` | Load saved state back to CPU (Context Restore). |

## Code Flow
1.  **Context Switch**: `Dispatcher` calls `oldProcess.getRegisterSet().save()`.
2.  **Logic**: Stores random/simulated values into AX, BX.
3.  **Restore**: `newProcess.getRegisterSet().restore()` puts those values back.
