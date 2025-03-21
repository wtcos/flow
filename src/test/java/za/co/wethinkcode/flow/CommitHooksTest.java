package za.co.wethinkcode.flow;

import org.junit.jupiter.api.*;
import za.co.wethinkcode.flow.bash.*;

import java.io.*;
import java.nio.file.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static za.co.wethinkcode.flow.FileHelpers.JLTK_FOLDER;

public class CommitHooksTest {

    TestFolder folder = new TestFolder();

    @Test
    void postCommitCreatesFlolFile() throws IOException {
        folder.makeGitFolder();
        new Recorder(new GitInfo(folder.root)).logRun();
        folder.bash(".git/hooks/pre-commit");
        folder.bash( ".git/hooks/post-commit");
        var files = Files.list(folder.root.resolve(JLTK_FOLDER)).toArray();
        assertThat(files.length).isEqualTo(1);
        assertTrue(files[0].toString().endsWith(".flol"));
        folder.delete();
    }

}
