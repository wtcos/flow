package za.co.wethinkcode.flow;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static za.co.wethinkcode.flow.FileHelpers.*;
import static za.co.wethinkcode.flow.GitInfo.NO_BRANCH;

/**
 * The primary entry point to the Flow system. The key api's are logRun() to
 * indicate a run of the app's main, and logTests() to indicate a run of some or
 * all of the app's JUnit tests.
 *
 * Recorder is self-initialized. The
 */
public class Recorder {
    private final GitInfo gitInfo;
    private final Runnable exit;

    /**
     * Explicit constructor, normally used only in tests, which takes a GitInfo,
     * which may be hand-built.
     *
     * @param gitInfo The GitInfo needed for the Recorder's other functions.
     * @param exit A runnable telling us what to do if there are show-stopper problems
     */
    public Recorder(GitInfo gitInfo, Runnable exit) {
        this.gitInfo = gitInfo;
        this.exit = exit;
    }

    /**
     * Constructor based on an already configured GitInfo. Normally only used in tests.
     *
     * @param info The necessary GitInfo
     */
    public Recorder(GitInfo info) {
        this(info,() -> System.exit(-1));
    }

    /**
     * Constructor that assumes that the current working directory can supply the
     * required GitInfo.
     */
    public Recorder() {
        this(GitInfo.from(),() -> System.exit(-1));
    }

    /**
     * Add an entry to the temporary log file indicating that main was run.
     *
     * This method is called from the static initializer block in the hosting
     * applications main file.
     */
    public void logRun() {
        if(Objects.equals(gitInfo.branch, NO_BRANCH)) return;
        if(!gitInfo.problems.isEmpty()) {
            gitInfo.problems.forEach( problem -> System.err.println(problem));
            exit.run();
        }
        initializeIfNeeded();
        writeToLog(gitInfo, new TimestampAppender(), new RunAppender());
    }

    /**
     * Add an entry to the temporary log file indicating that some or all JUnit
     * tests were run.
     *
     * This method is called by the WtcJUnitExtension, indicating that some or all
     * of the JUnit tests have been run.
     *
     * @param passes The list of test names that passed.
     * @param fails The list of test names that failed.
     * @param disables The list of test names that were disabled.
     * @param aborts The list of test names that were aborted.
     */
    public void logTest(List<String> passes, List<String> fails, List<String> disables, List<String> aborts) {
        if(Objects.equals(gitInfo.branch, NO_BRANCH)) return;
        if(!gitInfo.problems.isEmpty()) {
            gitInfo.problems.forEach( problem -> System.err.println(problem));
            exit.run();
        }
        initializeIfNeeded();
        writeToLog(gitInfo,
                new TimestampAppender(),
                new TestAppender("test", passes, fails, disables, aborts));
    }

    private void initializeIfNeeded() {
        var initializer = new Initializer(gitInfo.root);
        if(initializer.shouldInitialize()) {
            try {
                initializer.emitCommitHooks();
                initializer.emitJunitFiles();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeToLog(MapAppender... appenders) {
        try {
            forceJltkFolder();
            YamlMap map = new YamlMap();
            map.append(appenders);
            String yaml = map.asYamlString();
            String encoded = Base64.getEncoder().encodeToString(yaml.getBytes());
            appendToLogFile(encoded);
        } catch (Exception e) {
            System.err.println("Error: Could not record run!");
            e.printStackTrace();
            exit.run();
        }
    }

    private void forceJltkFolder() throws IOException {
        Files.createDirectories(gitInfo.root.resolve(FileHelpers.FLOW_FOLDER));
    }

    private void appendToLogFile(String yaml) throws IOException {
        PrintWriter log = new PrintWriter(
                Files.newBufferedWriter(
                        gitInfo.computeTemporaryPath(),
                        StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE)
        );
        log.println(yaml);
        log.flush();
        log.close();
    }
}
