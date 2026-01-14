# IPCManager.java Documentation

## Flowchart

```mermaid
graph LR
    A[Sender] --> B[sendMessage]
    B --> C[(Message Queues)]
    C --> D[receiveMessage]
    D --> E[Receiver]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `5` | `public class IPCManager` | Singleton manager for Inter-Process Communication. |
| `7` | `Map<Integer, Queue<Message>> messageQueues` | A map where each PID has its own mailbox (Queue). |
| `20` | `public void sendMessage(Message msg)` | Places a message into the receiver's mailbox. |
| `21` | `computeIfAbsent(...)` | Creates the mailbox if it doesn't already exist. |
| `24` | `public Message receiveMessage(int receiverPid)` | Retrieves (and removes) the next message for a process. |
| `32` | `public List<Message> peekAllMessages(...)` | Used by the UI to show the conversation history without deleting it. |

## Code Flow & Dry Run Example

**Scenario**: PID 1 sends "Hi" to PID 2.

1.  `sendMessage(new Message(1, 2, "Hi"))` is called.
2.  `messageQueues` looks for key `2`.
3.  The message is Added to the queue for PID 2.
4.  Later, PID 2 calls `receiveMessage(2)`.
5.  The "Hi" message is polled and returned to PID 2.
