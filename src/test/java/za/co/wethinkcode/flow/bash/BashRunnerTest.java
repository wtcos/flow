package za.co.wethinkcode.flow.bash;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

class BashRunnerTest {

    private final List<String> NON_EXISTING_SH = Arrays.asList("non-existing.sh");
    private final List<String> EXISTING_SH = Arrays.asList("saysSomething.sh");
    private final List<String> FAILED_SH = Arrays.asList("failing.sh");
    private final List<String> STDERR_SH = Arrays.asList("stderr.sh");
    private final List<String> STDOUT_SH = Arrays.asList("stdout.sh");
    private final Path shellFolder = Path.of("./testData/bash");
    private final Path exerciseFolder = Path.of("someExerciseFolder");

    @Test
    void handlesNonExistingShell() throws IOException {
        var bash = new BashRunner(shellFolder, NON_EXISTING_SH);
        var result = bash.bash();
        assertThat(result.code).isNotEqualTo(0);
    }

    @Test
    void handlesNonZeroShell() throws IOException {
        var bash = new BashRunner(shellFolder, FAILED_SH);
        var result = bash.bash();
        assertThat(result.code).isEqualTo(1);
    }


    @Test
    void handlesZeroShell() throws IOException {
        var bash = new BashRunner(shellFolder, EXISTING_SH);
        var result = bash.bash();
        assertThat(result.code).isEqualTo(0);
    }

    @Test
    void capturesStdErr() throws IOException {
        var bash = new BashRunner(shellFolder, STDERR_SH);
        var result = bash.bash();
        assertThat(result.stderr.next()).isEqualTo("This is on stderr.");
        assertThat(result.code).isEqualTo(0);
    }

    @Test
    void capturesStdOut() throws IOException {
        var bash = new BashRunner(shellFolder, STDOUT_SH);
        var result = bash.bash();
        assertThat(result.stdout.next()).isEqualTo("This is on stdout.");
        assertThat(result.code).isEqualTo(0);
    }
}