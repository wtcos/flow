package za.co.wethinkcode.flow;

import java.io.*;
import java.nio.file.*;

/**
 * A class holding several static file manipulators.
 */
public class FileHelpers {
    /**
     * The path from the root project to the .flow folder.
     */
    public static final String FLOW_FOLDER = ".lms/.flow";
    /**
     * The suffix to the temporary flow log file.
     */
    public static final String FLOW_LOG_SUFFIX = ".flol";
    /**
     * The suffix to the permanent flow log files.
     */
    public static final String FLOW_TMP_SUFFIX = ".flot";
    /**
     * The name of the authoring project opt-out file.
     */
    public static final String FLOW_AUTHOR_FILE = "author.txt";

    /**
     * Given a folder, return a list of files within it that end in
     * the FLOW_TMP_SUFFIX suffix.
     * @param root the target folder.
     * @return list of matching File objects.
     */
    public static File[] temporaryFiles(Path root) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(FLOW_TMP_SUFFIX);
            }
        };
        File[] result = root.resolve(FLOW_FOLDER).toFile().listFiles(filter);
        if (result == null) return new File[0];
        return result;
    }

    /**
     * Given a folder, return a list of files within it that end in
     * the FLOW_LOG_SUFFIX suffix.
     * @param root the given folder
     * @return list of matching File objects.
    */
    public static File[] finalFiles(Path root) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(FLOW_LOG_SUFFIX);
            }
        };
        File[] result = root.resolve(FLOW_FOLDER).toFile().listFiles(filter);
        if (result == null) return new File[0];
        return result;
    }

    /**
     * Given a path, find the folder containing that path's git repo.
     * <p>
     * Throws NoGitWorkingFolder exception if no such repo exists.
     *
     * @param from the given folder
     * @return The path, possibly the same as the input, or possible an "uncle".
     */
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
