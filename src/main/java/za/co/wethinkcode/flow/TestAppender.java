package za.co.wethinkcode.flow;

import java.util.*;

/**
 * A MapAppender that knows what information to add for a
 * test run. It adds the type of the run and four lists of
 * passed, failed, disabled, or aborted tests.
 */
public class TestAppender implements MapAppender {

    private final String type;
    private final List<String> passes = new ArrayList<>();
    private final List<String> fails = new ArrayList<>();
    private final List<String> disables = new ArrayList<>();
    private final List<String> aborts = new ArrayList<>();

    /**
     * @param type     -- the type of the action
     * @param passes   -- list of class.test strings for passing tests
     * @param fails    -- list of class.test strings for failing tests
     * @param disables -- list of class.test strings for disabled tests
     * @param aborts   -- list of class.test strings aborted tests
     */
    public TestAppender(String type,
                        List<String> passes,
                        List<String> fails,
                        List<String> disables,
                        List<String> aborts
    ) {
        this.type = type;
        this.passes.addAll(passes);
        this.fails.addAll(fails);
        this.disables.addAll(disables);
        this.aborts.addAll(aborts);
    }

    @Override
    public void putTo(YamlMap map) {
        map.put("type", type);
        if (type == "test") {
            map.put("passes", passes);
            map.put("fails", fails);
            map.put("disables", disables);
            map.put("aborts", aborts);
        }
    }
}
