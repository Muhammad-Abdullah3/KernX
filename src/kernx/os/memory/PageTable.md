# PageTable.java Documentation

## Flowchart

```mermaid
graph TD
    A[Process Creation] --> B[Calculate Required Pages]
    B --> C[Initialize PageTable]
    C --> D[Loop 0 to N]
    D --> E[Create Page(i)]
    E --> F[Add to List]
    
    G[Memory Access] --> H[Get Page(i)]
    H --> I[Return Page Object]
```

## Line-by-Line Explanation

| Line | Code Snippet | Explanation |
| :--- | :--- | :--- |
| `6` | `public class PageTable` | Container holding all pages for a single process. |
| `7` | `private final List<Page> pages;` | The list of logical pages. |
| `9` | `public PageTable(int numPages)` | Constructor. Creates `numPages` empty Page objects immediately. |
| `20` | `public Page getPage(int pageNumber)` | Safe accessor. Returns null if out of bounds. |

## Code Flow & Dry Run Example

**Scenario**: Process needs 10KB. Page Size is 4KB.

1.  **Calculation**: `ceil(10 / 4) = 3` pages needed.
2.  **Init**: `new PageTable(3)`.
    *   Loop `i=0` to `2`.
    *   Creates `Page(0)`, `Page(1)`, `Page(2)`.
    *   All stored in `pages` list.
3.  **Access**: `getPage(1)` returns the second page object, which can then be checked for its `frameNumber`.
