package kernx.os.memory;

import java.util.ArrayList;
import java.util.List;

public class PageTable {
    private final List<Page> pages;

    public PageTable(int numPages) {
        this.pages = new ArrayList<>(numPages);
        for (int i = 0; i < numPages; i++) {
            pages.add(new Page(i));
        }
    }

    public List<Page> getPages() {
        return pages;
    }

    public Page getPage(int pageNumber) {
        if (pageNumber >= 0 && pageNumber < pages.size()) {
            return pages.get(pageNumber);
        }
        return null;
    }
}
