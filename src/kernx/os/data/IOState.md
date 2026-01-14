# IOState.java Documentation

## Explanation

Simple data class to store I/O information for a process.

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `5` | `public class IOState` | Container for I/O devices (Disk, Network, Print). |
| `8` | `private List<String> openFiles;` | Tracks file handles. |
| `12` | `addFile(String path)` | Simulates opening a file resource. |

## Use Case
When a process requests `BlockProcessDialog`, this state can be used to simulate *why* it is blocked (e.g., "Waiting for D:\data.txt").
