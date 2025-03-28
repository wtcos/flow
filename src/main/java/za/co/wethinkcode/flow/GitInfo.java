package za.co.wethinkcode.flow;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;

import java.nio.file.*;
import java.util.*;

import static za.co.wethinkcode.flow.FileHelpers.*;

/**
 * Class used to hold all the information Flow needs that comes from the
 * git repo.
 */
public class GitInfo implements MapAppender {
    /**
     * The root folder of the current repo.
     * <p>
     * The root folder is the folder that is the immediate parent of the .git folder.
     */
    public final Path root;

    /**
     * The branch that is current in that repo, or NO_BRANCH if there is no current branch.
     */
    public final String branch;

    /**
     * The git username.
     */
    public final String username;

    /**
     * The git email
     */
    public final String email;

    /**
     * The repo's last commit hashtag
     */
    public final String last;

    /**
     * A list of any problems encountered during construction.
     */
    public final List<String> problems;

    /**
     * The primitive "all fields" constructor, used only for testing.
     */
    public GitInfo(Path root, String branch, String username, String email, String last, List<String> problems) {
        this.root = root;
        this.branch = branch;
        this.username = username;
        this.email = email;
        this.last = last;
        this.problems = problems;
    }

    /**
     * Computes a filename and path for the flow temporary file.
     * <p>
     * This is a combination of the git branch and the lefthand part of the git username.
     *
     * @return the resulting path.
     */
    public Path computeTemporaryPath() {
        String shortEmail = email.split("@")[0];
        String leafName = branch
                + "_" + shortEmail
                + FLOW_TMP_SUFFIX;
        return root.resolve(Path.of(FileHelpers.FLOW_FOLDER, leafName));
    }

    @Override
    public void putTo(YamlMap map) {
        map.put("branch", branch);
        map.put("committer", username);
        map.put("email", email);
        map.put("last", last);
    }

    private static String computeLastCommitHash(Repository localRepo) {
        try {
            Git git = new Git(localRepo);
            ObjectId branchId = localRepo.resolve("HEAD");
            Iterator<RevCommit> commits = git.log().add(branchId).call().iterator();
            if (commits.hasNext()) return commits.next().getName();
        } catch (Exception ignored) {
        }
        return "Unknown";
    }

    static public GitInfo from() {
        return from(Path.of("."));
    }

    static public GitInfo from(Path path) {
        var problems = new ArrayList<String>();
        Repository localRepo = tryRepo(path);
        if (localRepo == null) {
            problems.add("Could not find local repository from ["+path.toAbsolutePath().normalize()+"].");
            return new GitInfo(
                    path,
                    NO_BRANCH,
                    "N/A",
                    "N/A",
                    "N/A",
                    problems);
        }
        var last = computeLastCommitHash(localRepo);
        var root = localRepo.getWorkTree().toPath();
        Config configuration = localRepo.getConfig();
        var email = configuration.getString("user", null, "email");
        if(email==null || email.isEmpty() || !email.contains("@")) {
            problems.add("Invalid email address in git repo [" + email + "].");
        }
        var username = configuration.getString("user", null, "name");
        if(username==null || username.isEmpty()) {
            problems.add("Invalid user name  in git repo [" + username + "].");
        }
        var branch = tryBranch(localRepo);
        return new GitInfo(root, branch, username, email, last, problems);
    }

    static private String tryBranch(Repository localRepo) {
        try {
            var fullbranch = localRepo.getFullBranch();
            if(fullbranch.startsWith("refs/heads/")) {
                return localRepo.getBranch();
            }
            return NO_BRANCH;
        }
        catch (Exception ignored) {
            return NO_BRANCH;
        }
    }

    static private Repository tryRepo(Path path) {
        try {
            return new RepositoryBuilder()
                    .findGitDir(path.toFile()).build();
        } catch (Exception cause) {
            return null;
        }
    }

    public static final String NO_BRANCH = "none";
}
