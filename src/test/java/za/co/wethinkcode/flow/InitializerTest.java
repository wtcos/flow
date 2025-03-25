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
        Path authorPath = folder.root.resolve(FLOW_AUTHOR_FILE);
        Files.createFile(authorPath);
        folder.initGitRepo();
        Initializer initializer = new Initializer(folder.root);
        assertFalse(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void noAuthorFileButFlowFolderMeansNoInitialization() throws IOException {
        folder.initGitRepo();
        Path flowFolder = folder.root.resolve(FLOW_FOLDER);
        Files.createDirectories(flowFolder);
        Initializer initializer = new Initializer(folder.root);
        assertFalse(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void noAuthorFileNoFlowShouldInitialize() throws IOException {
        folder.initGitRepo();
        Initializer initializer = new Initializer(folder.root);
        assertTrue(initializer.shouldInitialize());
        folder.delete();
    }

    @Test
    public void initializeEmitsJunitFiles() throws IOException {
        folder.initGitRepo();
        Initializer initializer = new Initializer(folder.root);
        initializer.emitJunitFiles();
        Path junitProperties = folder.root
                .resolve("src/test/resources/junit-platform.properties");
        assertTrue(Files.exists(junitProperties));
        Path metaInf = folder.root.resolve("src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension");
        assertTrue(Files.exists(metaInf));
        folder.delete();
    }

    @Test
    public void initializeEmitsCommitHooks() throws GitAPIException, IOException {
        folder.initGitRepo();
        Initializer initializer = new Initializer(folder.root);
        initializer.emitCommitHooks();
        Path hooksPath = folder.root.resolve(".git/hooks");
        Path preCommitHook = hooksPath.resolve("pre-commit");
        Path postCommitHook = hooksPath.resolve("post-commit");
        assertTrue(preCommitHook.toFile().exists());
        assertTrue(postCommitHook.toFile().exists());
        folder.delete();
    }
}
