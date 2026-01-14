# ProcessManager.java Documentation

## Flowchart

```mermaid
graph TD
    A[Start: tick] --> B{Inc System Time}
    B --> C[Check Arrivals]
    C --> D{Is Queue Empty?}
    D -- Yes --> E[Wait]
    D -- No --> F{Is RunningProcess Null?}
    F -- Yes --> G[DispatchNextProcess]
    G --> H[Scheduler.selectNextProcess]
    H --> I[Set RunningProcess]
    I --> J[Dispatcher.dispatch]
    F -- No --> K[runningProcess.consumeCpu]
    K --> L{Burst Time <= 0?}
    L -- Yes --> M[Terminate Process (State=TERMINATED)]
    M --> N[Notify Listeners]
    N --> G
    L -- No --> O{Round Robin & Quantum Expired?}
    O -- Yes --> P[Preempt Process]
    P --> Q[Add to Ready Queue]
    Q --> G
    O -- No --> R[Continue]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `1` | `package kernx.os.manager;` | Package declaration. |
| `13` | `public class ProcessManager` | Main class managing all process lifecycles. |
| `37` | `Queue<PCB> readyQueue` | Holds processes waiting for CPU. |
| `48` | `Scheduler scheduler` | The active scheduling strategy (FCFS or RR). |
| `62` | `createProcess(...)` | Creates a new PCB. |
| `64` | `absoluteArrival = systemTime + offset` | Ensures arrival time is relative to NOW. |
| `69` | `MemoryManager.getInstance().allocate(pcb)` | Tries to reserve memory frames. If fails, process isn't created. |
| `104` | `dispatchNextProcess()` | Asks scheduler for next PCB and switches context. |
| `267` | `tick()` | The main simulation loop method. |
| `268` | `systemTime++` | Advances the global clock. |
| `277` | `runningProcess.consumeCpu()` | Decrements remaining burst time of current process. |
| `295` | `if (scheduler instanceof RoundRobinScheduler)` | Special check for RR time slicing. |

## Code Flow & Dry Run Example

**Scenario**: A Process (P1) with Burst Time 5 runs under Round Robin (Quantum=2).

1.  **Tick 0**: `createProcess(P1, burst=5, quantum=2)`. P1 added to Ready Queue.
2.  **Tick 1**:
    *   `runningProcess` is null.
    *   `dispatchNextProcess()` calls Scheduler. Scheduler picks P1.
    *   `runningProcess` = P1.
    *   `P1.consumeCpu()` -> RemBurst=4. `QuantumCounter`=1.
3.  **Tick 2**:
    *   `P1.consumeCpu()` -> RemBurst=3. `QuantumCounter`=2.
    *   Check `QuantumCounter >= Quantum` (2 >= 2). **True**.
    *   `preempt(P1)`: P1 moved to Ready Queue. `runningProcess` = null.
    *   `dispatchNextProcess()` attempts to pick next. If P1 is only one, it picks P1 again.
4.  **Tick 3**:
    *   P1 resumes. `QuantumCounter` reset to 0.
