package za.co.wethinkcode.flow.bash;

import org.buildobjects.process.*;

import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class BashRunner {

    public static final String GIT_BASH_PATH = "C:/Program Files/Git/bin/bash.exe";
    public static final String LINUX_BASH_PATH = "/usr/bin/bash";
    public static final String MACOS_BASH_PATH = "/bin/bash";

    public static final long TIMEOUT_MS = 60000L;

    public final List<String> args;
    final Path workingFolder;

    public BashRunner(List<String> args, Path workingFolder) {
        this.args = args;
        this.workingFolder = workingFolder;
    }

    public BashResult bash() {
        BashStreamConsumer stdout = new BashStreamConsumer();
        BashStreamConsumer stderr = new BashStreamConsumer();

        var builder = new ProcBuilder(args.get(0))
                .ignoreExitStatus()
                .withTimeoutMillis(TIMEOUT_MS)
                .withErrorConsumer(stderr)
                .withOutputConsumer(stdout)
                .withWorkingDirectory(workingFolder.toFile());
        IntStream.range(1, args.size()).mapToObj(args::get).forEach(builder::withArg);
        try {
            System.out.println("Working [" + workingFolder.toString() + "]");
            var result = builder.run();
            stderr.thread.join();
            stdout.thread.join();
            return new BashResult(result.getCommandLine(), result.getExitValue(), stdout.saver.lines, stderr.saver.lines);
        } catch (TimeoutException ignored) {
            throw new RuntimeException("Timed out: "+builder.getCommandLine());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String bashPath() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) return GIT_BASH_PATH;
        if (os.startsWith("Mac")) return MACOS_BASH_PATH;
        return LINUX_BASH_PATH;
    }
}
