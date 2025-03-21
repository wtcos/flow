package za.co.wethinkcode.flow.bash;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

class BashRunnerTest {

    private final List<String> NON_EXISTING_SH = Arrays.asList(BashRunner.bashPath(), "non-existing.sh");
    private final List<String> EXISTING_SH = Arrays.asList(BashRunner.bashPath(), "saysSomething.sh");
    private final List<String> FAILED_SH = Arrays.asList(BashRunner.bashPath(), "failing.sh");
    private final List<String> STDERR_SH = Arrays.asList(BashRunner.bashPath(), "stderr.sh");
    private final List<String> STDOUT_SH = Arrays.asList(BashRunner.bashPath(), "stdout.sh");
    private final Path shellFolder = Path.of("./testData/bash");
    private final Path exerciseFolder = Path.of("someExerciseFolder");

    @Test
    void handlesNonExistingShell() throws IOException {
        var bash = new BashRunner(NON_EXISTING_SH, shellFolder);
        var result = bash.bash(exerciseFolder);
        assertThat(result.code).isNotEqualTo(0);
    }

    @Test
    void handlesNonZeroShell() throws IOException {
        var bash = new BashRunner(FAILED_SH, shellFolder);
        var result = bash.bash(exerciseFolder);
        assertThat(result.code).isEqualTo(1);
    }


    @Test
    void handlesZeroShell() throws IOException {
        var bash = new BashRunner(EXISTING_SH, shellFolder);
        var result = bash.bash(exerciseFolder);
        System.out.println(result.stdout);
        System.out.println(result.stderr);
        assertThat(result.code).isEqualTo(0);
    }

    @Test
    void capturesStdErr() throws IOException {
        var bash = new BashRunner(STDERR_SH, shellFolder);
        var result = bash.bash(exerciseFolder);
        assertThat(result.stderr.next()).isEqualTo("This is on stderr.");
        assertThat(result.code).isEqualTo(0);
    }

    @Test
    void capturesStdOut() throws IOException {
        var bash = new BashRunner(STDOUT_SH, shellFolder);
        var result = bash.bash(exerciseFolder);
        assertThat(result.stdout.next()).isEqualTo("This is on stdout.");
        assertThat(result.code).isEqualTo(0);
    }
}