package za.co.wethinkcode.flow.bash;

import java.util.*;

public class LinesIterator {
    private final List<String> source;
    private int next;

    public LinesIterator(List<String> source) {
        this.source = source;
        this.next = 0;
    }

    public boolean hasNext() {
        return next < source.size();
    }

    public String next() {
        if (hasNext()) return source.get(next++);
        throw new NoNextLine();
    }

    public void backwards() {
        if (next > 0) next -= 1;
        else throw new NoPreviousLine();
    }

    public String peek() {
        if (hasNext()) return source.get(next);
        throw new NoNextLine();
    }
}
