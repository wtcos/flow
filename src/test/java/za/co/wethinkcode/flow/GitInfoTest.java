package za.co.wethinkcode.flow;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GitInfoTest {


    @Test
    void literalConstructor() {
        GitInfo info = new GitInfo(Path.of("."), "branch", "committer", "email", "last", new ArrayList<>());
        // assumption: the working folder is the root of the git repo.
        assertEquals(Path.of("."), info.root);
        assertEquals("branch", info.branch);
        assertEquals("committer", info.username);
        assertEquals("email", info.email);
        assertEquals("last", info.last);
    }

    @Test
    void problemNoRepo() {
        // assumption: the user's home directory is not a valid repo
        GitInfo info = GitInfo.from(Path.of(System.getProperty("user.home")));
        assertThat(info.problems).isNotEmpty();
    }

    @Test
    void problemBrokenRepo() throws IOException {
        TestFolder folder = new TestFolder();
        Files.createDirectories(folder.root.resolve(".git"));
        GitInfo info = GitInfo.from(Path.of(System.getProperty("user.home")));
        assertThat(info.problems).isNotEmpty();
    }


    @Test
    void worksOnThisRepo() {
        GitInfo info = GitInfo.from();
        String expected = Path.of(".").toAbsolutePath().normalize().toString();
        assertEquals(expected, info.root.toAbsolutePath().normalize().toString());
    }

    @Test
    void knowsRepoFolder() {
        GitInfo info = GitInfo.from();
        String expected = Path.of(".").toAbsolutePath().normalize().toString();
        assertEquals(expected, info.root.toAbsolutePath().normalize().toString());
    }
}
