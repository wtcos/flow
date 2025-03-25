package za.co.wethinkcode.flow;

import java.nio.file.*;

/**
 * Exception to indicate that a git root can not be found. It prints the path
 * where the search started, and the underlying cause, if any.
 */
public class NoGitWorkingFolder extends RuntimeException {
    NoGitWorkingFolder(Path path, Throwable cause) {
        super("Not a valid git working folder: " + path.toAbsolutePath().toString(), cause);
    }
}
