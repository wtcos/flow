package za.co.wethinkcode.flow;

import org.junit.jupiter.api.*;
import za.co.wethinkcode.flow.bash.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static za.co.wethinkcode.flow.FileHelpers.JLTK_FOLDER;

public class CommitHooksTest {

    TestFolder folder = new TestFolder();

    @Test
    void commitHooksInvoked() throws IOException {
        folder.makeGitFolder();
        new Recorder(new GitInfo(folder.root)).logRun();
        preCommit();
        postCommit();
        Files.list(folder.root.resolve(JLTK_FOLDER)).forEach(file -> {
           assertThat(file.toString().endsWith(".flot")).isFalse();
        });
        folder.delete();
    }

    private BashResult postCommit() {
        return new BashRunner(List.of(BashRunner.bashPath(), ".git/hooks/post-commit"), folder.root).bash();
    }

    private BashResult preCommit() {
        return new BashRunner(List.of(BashRunner.bashPath(), ".git/hooks/pre-commit"), folder.root).bash();
    }
}
