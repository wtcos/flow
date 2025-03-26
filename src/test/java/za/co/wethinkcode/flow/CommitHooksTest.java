package za.co.wethinkcode.flow;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static za.co.wethinkcode.flow.FileHelpers.FLOW_FOLDER;

public class CommitHooksTest {

    TestFolder folder = new TestFolder();

    @Test
    void postCommitCreatesFlolFile() throws IOException {
        folder.initGitRepo();
        new Recorder(GitInfo.from(folder.root)).logRun();
        folder.bash(".git/hooks/pre-commit");
        folder.bash( ".git/hooks/post-commit");
        var files = Files.list(folder.root.resolve(FLOW_FOLDER)).toArray();
        assertThat(files.length).isEqualTo(1);
        assertTrue(files[0].toString().endsWith(".flol"));
        var status = folder.gitStatus();
        assertThat(status.hasUncommittedChanges()).isFalse();
        folder.delete();
    }

}
