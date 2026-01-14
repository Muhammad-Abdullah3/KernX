# ProcessState.java Documentation

## Diagram

```mermaid
stateDiagram-v2
    [*] --> NEW
    NEW --> READY : Admitted
    READY --> RUNNING : Dispatch
    RUNNING --> READY : Preempt (Time Quantum)
    RUNNING --> BLOCKED : I/O Request
    BLOCKED --> READY : I/O Complete
    READY --> SUSPENDED : Suspend
    SUSPENDED --> READY : Resume
    RUNNING --> TERMINATED : Finished
    TERMINATED --> [*]
```

## Explanation

| Enum Value | Meaning |
| :--- | :--- |
| `NEW` | Process is being created, memory not yet fully stable. |
| `READY` | In memory, waiting for CPU. |
| `RUNNING` | Currently executing instructions on the CPU. |
| `BLOCKED` | Waiting for external event (I/O, Semaphore). |
| `SUSPENDED` | Swapped out to disk (Medium Term Scheduling). |
| `TERMINATED` | Execution finished, waiting to be cleared. |
