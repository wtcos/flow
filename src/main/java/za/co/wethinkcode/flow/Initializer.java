package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.*;

import java.io.*;
import java.nio.file.*;

import static za.co.wethinkcode.flow.FileHelpers.*;

/**
 * Specialist in initializing flow projects.
 */
public class Initializer {

    /**
     * The project's root folder, supplied by caller.
     * It is normally the folder containing pom.xml or gradle.build.*.
     */
    Path projectRoot;

    /**
     * The projects git root folder, deduced from the projectRoot.
     * This is the folder that owns the .git folder, which may be
     * the same as the projectRoot, or may be an ancestor folder. The
     * latter is normally the case in authoring projects.
     */
    Path gitRoot;

    /**
     * Create an initializer with the given projectRoot.
     * @param project the root of the project's make/pom/gradle files.
     */
    public Initializer(Path project) {
        projectRoot = project;
        gitRoot = requireGitRoot(projectRoot);
    }

    /**
     * Test the root to see if we need to initialize it.
     *
     * If the gitroot folder contains a file named "author.txt",
     * we do not need to initialize it.
     * if the projectroot contins .lms/.flow it is already initialized.
     * Otherwise, we will initialize it.
     * @return true if we should initialize.
     */
    public Boolean shouldInitialize() {
        Path authorPath = gitRoot.resolve("author.txt");
        if (authorPath.toFile().exists()) return false;
        Path flowFolder = projectRoot.resolve(FLOW_FOLDER);
        if (flowFolder.toFile().exists()) return false;
        return true;
    }

    /**
     * when we initizlize, we have to add two files to the
     * src/test/resources folder to make junit call our recorder.
     * This function adds those two files and quietly commits them to
     * the repo.
     *
     * @throws IOException
     */
    public void emitJunitFiles() throws IOException {
        projectRoot.resolve("src/test/resources/META-INF/services/").toFile().mkdirs();
        Path propertiesPath = projectRoot.resolve("src/test/resources/junit-platform.properties");
        BufferedWriter writer = Files.newBufferedWriter(propertiesPath);
        writer.write("junit.jupiter.extensions.autodetection.enabled=true\n");
        writer.flush();
        writer.close();
        Path metaPath = projectRoot.resolve("src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension");
        writer = Files.newBufferedWriter(metaPath);
        writer.write("za.co.wethinkcode.flow.WtcJunitExtension\n");
        writer.flush();
        writer.close();
        try {
            Git git = Git.open(gitRoot.toFile());
            git.add().addFilepattern("src/test/resources").call();
            git.commit().setMessage("Adding flow src/test/resources.").call();
            git.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When we initialize, we add two client-side hooks, one for
     * pre-commit, and one for post-commit.
     *
     * @throws IOException
     */
    public void emitCommitHooks() throws IOException {
        Path precommit = gitRoot.resolve(".git/hooks/pre-commit");
        writeAll(precommit, precommitText);
        precommit.toFile().setExecutable(true);
        Path postcommit = gitRoot.resolve(".git/hooks/post-commit");
        writeAll(postcommit, postcommitText);
        postcommit.toFile().setExecutable(true);
    }

    private void writeAll(Path path, String text) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write(text);
        writer.flush();
        writer.close();
    }


    private static final String precommitText = """
            #!/bin/bash

            echo "Pre-Commit"
            touch .commit
            """;

    private static final String postcommitText = """
            #!/bin/bash
            pwd
            if [ -e .commit ] ; then
              echo "Post-Commit"
              TIMESTAMP=`date +%Y%m%d%H%M%S`
              BRANCH=`git rev-parse --abbrev-ref HEAD`
              IFS="@" read -r NAME EMAIL <<< `git config user.email`
              TEMPNAME=".lms/.flow/${BRANCH}_${NAME}.flot"
              FINALNAME=".lms/.flow/${BRANCH}_${NAME}_${TIMESTAMP}.flol"
              rm .commit
              if [ -e $TEMPNAME ] ; then
                mv $TEMPNAME $FINALNAME
                git add -f $FINALNAME
                git commit --amend -C HEAD --no-verify
              fi
            fi
            """;
}
