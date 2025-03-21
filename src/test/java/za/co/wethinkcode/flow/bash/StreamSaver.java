package za.co.wethinkcode.flow.bash;

import java.io.*;
import java.util.*;

public class StreamSaver implements Runnable {

    final InputStream stream;
    final ArrayList<String> lines = new ArrayList<>();

    StreamSaver(InputStream i) {
        stream = i;
    }

    @Override
    public void run() {
        try {
            Scanner s = new Scanner(stream);
            while (s.hasNextLine()) {
                lines.add(s.nextLine());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
