package za.co.wethinkcode.flow;

import java.io.*;
import java.nio.file.*;

public class FileHelpers {
    public static final String JLTK_FOLDER = ".lms/.flow";
    public static final String JLTK_LOG_SUFFIX = ".flol";
    public static final String JLTK_TMP_SUFFIX = ".flot";
    public static final String JLTK_AUTHOR_FILE = "author.txt";

    public static File[] temporaryFiles(Path root) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(JLTK_TMP_SUFFIX);
            }
        };
        File[] result = root.resolve(JLTK_FOLDER).toFile().listFiles(filter);
        if (result == null) return new File[0];
        return result;
    }

    public static File[] finalFiles(Path root) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(JLTK_LOG_SUFFIX);
            }
        };
        File[] result = root.resolve(JLTK_FOLDER).toFile().listFiles(filter);
        if (result == null) return new File[0];
        return result;
    }

    public static Path requireGitRoot(Path from) {
        Path candidate = from.toAbsolutePath();
        while (candidate.getNameCount() > 1) {
            Path candidateGit = candidate.resolve(".git");
            if (candidateGit.toFile().exists() && candidateGit.toFile().isDirectory()) return candidate;
            candidate = candidate.resolve("..").toAbsolutePath().normalize();
        }
        throw new NoGitWorkingFolder(from, null);
    }
}
