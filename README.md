# KernX OS Simulation

KernX is a comprehensive Operating System Simulation project implemented in Java Swing. It demonstrates the core principles of OS design, from process management to memory paging and inter-process communication.

## 🚀 Key Features

### 1. Process Management & Scheduling
- **Lifecycle**: Create, Destroy, Suspend, Resume, Block, and Wakeup processes.
- **Schedulers**: Supports **FCFS** (First-Come First-Served) and **Round Robin** (Preemptive with custom time quantum).
- **Visualization**: Real-time ready, blocked, and suspended queue displays.

### 2. Memory Management
- **Paging**: Simulates memory allocation using a page-based system.
- **Replacement**: Implements the **LRU (Least Recently Used)** page replacement algorithm.
- **Grid View**: Visual map of physical memory frames showing occupancy and owner PIDs.

### 3. Synchronization & IPC
- **IPC**: Message passing system with sender/receiver routing and historically captured message logs.
- **Sync**: Counting **Semaphores** with **wait(P)** and **signal(V)** operations to manage critical sections and process blocking.

### 4. I/O Management
- **Hardware Simulation**: Keyboard, Mouse, Monitor, HDD, and Printer devices.
- **Resource Allocation**: Processes can request devices, causing them to enter a BLOCKED state until the device is released.

## 🛠️ Project Structure

- `kernx.os`: The "Hardware" and Core abstraction.
- `kernx.os.manager`: Central control for scheduling and process state transitions.
- `kernx.os.memory`: Logic for page tables and frame allocation.
- `kernx.ui`: Aesthetic dark-themed dashboard using custom Swing components.

## 📝 Constraints & Limitations
- **Simulation Time**: The system uses a simulated "Tick" (1hz by default) rather than real-time nanos for scheduling logic.
- **Memory Size**: Memory is limited to the frames defined in `config.properties`.
- **Single CPU**: The simulation acts as a single-core system where only one process is "RUNNING" at a time.

## 🏁 How to Run
1. Ensure you have **Java JDK 17+** installed.
2. Compile and run `kernx.ui.MainFrame.java`.
3. Use the **Configuration** panel to adjust memory/page sizes before starting.
4. Go to **Process Management**, create some processes, and click **Start CPU**.
