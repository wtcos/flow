package za.co.wethinkcode.flow;

import org.junit.jupiter.api.extension.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * A custom JUnitExtension that overrides JUnit's TestWatcher
 * to remember test runs and add them to a Flow temp file on exit.
 */
public class WtcJunitExtension implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        if (context == null) return;
        passes.add(niceTestName(context));
    }

    private String niceTestName(ExtensionContext context) {
        Method test = context.getRequiredTestMethod();
        return test.getDeclaringClass().getSimpleName() + "." + test.getName();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        if (context == null) return;
        disables.add(niceTestName(context));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        if (context == null) return;
        aborts.add(niceTestName(context));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (context == null) return;
        fails.add(niceTestName(context));
    }

    private static final Recorder recorder = new Recorder();
    private static final List<String> passes = new ArrayList<>();
    private static final List<String> fails = new ArrayList<>();
    private static final List<String> disables = new ArrayList<>();
    private static final List<String> aborts = new ArrayList<>();

    private static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("Shutdown");
            recorder.logTest(passes, fails, disables, aborts);
        }
    };
    private static final Thread hook = new Thread(runnable);
    private static final Runtime runtime = Runtime.getRuntime();

    static {
        runtime.addShutdownHook(hook);
    }
}
