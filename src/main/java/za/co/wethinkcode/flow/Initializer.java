package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.*;

import java.io.*;
import java.nio.file.*;

import static za.co.wethinkcode.flow.FileHelpers.*;

public class Initializer {

    Path projectRoot;
    Path gitRoot;

    public Initializer(Path path) {
        projectRoot = path;
        gitRoot = requireGitRoot(projectRoot);
    }

    public Boolean shouldInitialize() {
        Path authorPath = gitRoot.resolve("author.txt");
        if (authorPath.toFile().exists()) return false;
        Path flowFolder = projectRoot.resolve(JLTK_FOLDER);
        if (flowFolder.toFile().exists()) return false;
        return true;
    }

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

    public void emitCommitHooks() throws IOException {
        Path precommit = gitRoot.resolve(".git/hooks/pre-commit");
        writeAll(precommit, precommitText);
        precommit.toFile().setExecutable(true);
        Path postcommit = gitRoot.resolve(".git/hooks/post-commit");
        writeAll(postcommit, postcommitText);
        postcommit.toFile().setExecutable(true);
    }

    public void writeAll(Path path, String text) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        writer.write(text);
        writer.flush();
        writer.close();
    }


    static String precommitText = """
            #!/bin/bash

            echo "Pre-Commit"
            touch .commit
            """;

    static String postcommitText = """
            #!/bin/bash
            pwd
            if [ -e .commit ] ; then
              echo "Post-Commit"
              TIMESTAMP=`date +%Y%m%d%H%M%S`
              BRANCH=`git rev-parse --abbrev-ref HEAD`
              IFS="@" read -r NAME EMAIL <<< `git config user.email`
              TEMPNAME=".lms/.flow/${BRANCH}_${NAME}.flot"
              FINALNAME="./lms/.flow/${BRANCH}_${NAME}_${TIMESTAMP}.flol"
              rm .commit
              if [ -e $TEMPNAME ] ; then
                mv $TEMPNAME $FINALNAME
                git add -f $FINALNAME
                git commit --amend -C HEAD --no-verify
              fi
            fi
            """;
}
