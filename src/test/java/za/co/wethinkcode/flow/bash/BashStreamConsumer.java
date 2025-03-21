package za.co.wethinkcode.flow.bash;

import org.buildobjects.process.*;

import java.io.*;

public class BashStreamConsumer implements StreamConsumer {

    Thread thread = null;
    StreamSaver saver = null;

    @Override
    public void consume(InputStream stream) throws IOException {
        saver = new StreamSaver(stream);
        thread = new Thread(saver);
//        thread.setDaemon(true);
        thread.start();
    }
}
