package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
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
        assertThat(info.problems).isEmpty();
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
        folder.delete();
    }

    @Test
    void problemNoEmail() throws IOException {
        TestFolder folder = new TestFolder();
        folder.initGitRepo();
        Git git = Git.open(folder.root.toFile());
        var repo = git.getRepository();
        var config = repo.getConfig();
        config.setString("user",null,"email","");
        config.save();
        git.close();
        GitInfo info = GitInfo.from(folder.root);
        assertThat(info.problems).isNotEmpty();
        folder.delete();
    }

    @Test
    void problemNoUsername() throws IOException {
        TestFolder folder = new TestFolder();
        folder.initGitRepo();
        Git git = Git.open(folder.root.toFile());
        var repo = git.getRepository();
        var config = repo.getConfig();
        config.setString("user",null,"name","");
        config.save();
        git.close();
        GitInfo info = GitInfo.from(folder.root);
        assertThat(info.problems).isNotEmpty();
        folder.delete();
    }

    @Test
    void detachedHead() throws IOException, GitAPIException {
        TestFolder folder = new TestFolder();
        folder.initGitRepo();
        Git git = Git.open(folder.root.toFile());
        Files.writeString(folder.root.resolve("file.txt"),"Some string.");
        git.add().addFilepattern(folder.root.toString()).call();
        var commit = git.commit().setMessage("First").call();
        var hash = commit.getName();
        git.checkout().setName(hash).call();
        git.close();
        GitInfo info = GitInfo.from(folder.root);
        assertThat(info.branch).isEqualTo(GitInfo.NO_BRANCH);
        folder.delete();
    }



    @Test
    void worksOnThisRepo() {
        GitInfo info = GitInfo.from();
        String expected = Path.of(".").toAbsolutePath().normalize().toString();
        assertEquals(expected, info.root.toAbsolutePath().normalize().toString());
    }
}
