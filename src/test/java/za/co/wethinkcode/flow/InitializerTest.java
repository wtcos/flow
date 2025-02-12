package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.errors.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static za.co.wethinkcode.flow.FileHelpers.*;

public class InitializerTest {

    TestFolder folder = new TestFolder();

    @Test
    public void authorFilePresentMeansNoInitialization() throws IOException {
        Path authorPath = folder.root.resolve("author.txt");
        Files.createFile(authorPath);
        Path gitFolder = folder.root.resolve(".git");
        Files.createDirectories(gitFolder);
        Initializer initializer = new Initializer(folder.root);
        assertFalse(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void noAuthorFileButFlowFolderMeansNoInitialization() throws IOException {
        Path gitFolder = folder.root.resolve(".git");
        Files.createDirectories(gitFolder);
        Path flowFolder = folder.root.resolve(JLTK_FOLDER);
        Files.createDirectories(flowFolder);
        Initializer initializer = new Initializer(folder.root);
        assertFalse(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void noAuthorFileNoFlowShouldInitialize() throws IOException {
        Path gitFolder = folder.root.resolve(".git");
        Files.createDirectories(gitFolder);
        Initializer initializer = new Initializer(folder.root);
        assertTrue(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void initializeEmitsJunitFiles() throws IOException {
        Path gitFolder = folder.root.resolve(".git");
        Files.createDirectories(gitFolder);
        Initializer initializer = new Initializer(folder.root);
        initializer.emitJunitFiles();
        Path junitProperties = folder.root
                .resolve("src/test/resources/junit-platform.properties");
        assertTrue(Files.exists(junitProperties));
        Files.lines(junitProperties).forEach(System.out::println);
        Path metaInf = folder.root.resolve("src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension");
        assertTrue(Files.exists(metaInf));
        folder.delete();
    }

    @Test
    public void initializeEmitsCommitHooks() throws GitAPIException, IOException {
        folder.makeGitFolder();
        Initializer initializer = new Initializer(folder.root);
        initializer.emitCommitHooks();
        Path hooksPath = folder.root.resolve(".git/hooks");
        Path preCommitHook = hooksPath.resolve("pre-commit");
        Path postCommitHook = hooksPath.resolve("post-commit");
        assertTrue(preCommitHook.toFile().exists());
        assertTrue(postCommitHook.toFile().exists());
    }
}
