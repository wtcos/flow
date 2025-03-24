package za.co.wethinkcode.flow;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecorderTest {

    TestFolder folder = new TestFolder();
    GitInfo info = new GitInfo(folder.root, "branch", "committer", "email@somewhere.com", "last");

    @Test
    void roundTripTemporaryLog() throws IOException {
        folder.initGitRepo();
        Recorder first = new Recorder(info);
        first.logRun();
        File[] temps = folder.temporaryFiles();
        assertEquals(1, temps.length);
        List<String> lines = Files.readAllLines(temps[0].toPath());
        assertEquals(1, lines.size());
        String firstEntry = new String(Base64.getDecoder().decode(lines.get(0)));
        String[] resultYaml = firstEntry.split("\n");
        assertEquals(resultYaml[6], "type: run");
        folder.delete();
    }

    @Test
    void roundTripTwiceTemporaryLog() throws IOException {
        folder.initGitRepo();
        Recorder first = new Recorder(info);
        first.logRun();
        Recorder second = new Recorder(info);
        second.logTest(emptyList(), emptyList(), emptyList(), emptyList());
        File[] temps = folder.temporaryFiles();
        assertEquals(1, temps.length);
        List<String> lines = Files.readAllLines(temps[0].toPath());
        assertEquals(2, lines.size());
        String firstEntry = new String(Base64.getDecoder().decode(lines.get(0)));
        String[] firstYaml = firstEntry.split("\n");
        assertEquals(firstYaml[6], "type: run");
        String secondEntry = new String(Base64.getDecoder().decode(lines.get(1)));
        String[] secondYaml = secondEntry.split("\n");
        assertEquals(secondYaml[6], "type: test");
        folder.delete();
    }
}
