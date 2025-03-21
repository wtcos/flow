package za.co.wethinkcode.flow.bash;

import java.util.*;

public class BashResult {

    final String command;
    public final int code;
    public final LinesIterator stdout;
    public final LinesIterator stderr;

    BashResult(String command, int code, List<String> stdout, List<String> stderr) {
        this.command = command;
        this.code = code;
        this.stdout = new LinesIterator(stdout);
        this.stderr = new LinesIterator(stderr);
    }
}
