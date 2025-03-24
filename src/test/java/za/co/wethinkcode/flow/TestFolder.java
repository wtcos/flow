package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.*;
import za.co.wethinkcode.flow.bash.*;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static za.co.wethinkcode.flow.FileHelpers.*;

public class TestFolder {

    public final Path root;
    public final Path rootWtc;

    public TestFolder(Path root) {
        this.root = root;
        this.rootWtc = root.resolve(FileHelpers.FLOW_FOLDER);
        try {
            wipeRoot();
            Files.createDirectories(root);
        } catch (Exception wrapped) {
            throw new RuntimeException("TestFolder construction failed.", wrapped);
        }
    }

    public TestFolder() {
        this(gitRootTestingTemp());
    }

    public void assertRootTree() {
        assertTrue(root.toFile().exists() && root.toFile().isDirectory());
        assertTrue(rootWtc.toFile().exists() && rootWtc.toFile().isDirectory(), "Missing rootWtc [" + rootWtc.toString() + "]");
    }

    void wipeRoot() {
        recursivelyWipe(root.toFile());
    }

    void delete() {
        recursivelyDelete(root.toFile());
    }

    void recursivelyDelete(File directoryToBeDeleted) {
        recursivelyWipe(directoryToBeDeleted);
        directoryToBeDeleted.delete();
    }

    BashResult bash(String... args) {
        return new BashRunner(root,args).bash();
    }

    private void recursivelyWipe(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                recursivelyDelete(file);
            }
        }
    }

    public File[] temporaryFiles() {
        return FileHelpers.temporaryFiles(root);
    }

    public File[] finalFiles() {
        return FileHelpers.finalFiles(root);
    }

    public static Path gitRootTestingTemp() {
        try {
            Path gitRoot = requireGitRoot(Path.of("."));
            Path testing = gitRoot.resolve("testing");
            Files.createDirectories(testing);
            return Files.createTempDirectory(testing, "test");
        } catch (IOException wrapped) {
            throw new RuntimeException(wrapped);
        }
    }

    public void initGitRepo() {
        try {
            Git git = Git.init().setDirectory(root.toFile()).call();
            git.close();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Status gitStatus() {
        try {
            Git git = Git.open(root.toFile());
            Status status = git.status().call();
            git.close();
            return status;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


}
