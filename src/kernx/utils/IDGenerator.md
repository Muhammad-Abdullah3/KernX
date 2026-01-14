# IDGenerator.java Documentation

## Flowchart

```mermaid
graph LR
    A[Request PID] --> B[Atomic Counter]
    B --> C[incrementAndGet]
    C --> D[Return Unique ID]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `7` | `AtomicInteger counter = new AtomicInteger(1000);` | Starts Process IDs from 1000 to differentiate from system tasks. |
| `10` | `counter.incrementAndGet()` | Thread-safe atomic increment ensures no two processes get the same PID. |

## Code Flow & Dry Run Example

**Scenario**: Creating two processes.
1. `generatePID()` called. `1000 -> 1001`. Returns 1001.
2. `generatePID()` called. `1001 -> 1002`. Returns 1002.
3. Even if called simultaneously by two threads, the PID is guaranteed unique.
