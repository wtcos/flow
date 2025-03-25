package za.co.wethinkcode.flow;

import java.time.*;
import java.time.format.*;

/**
 * A MapAppender to append the local timestamp.
 */
public class TimestampAppender implements MapAppender {
    private final String timestamp;

    /**
     * Explicit constructor taking any LocalDateTime
     * @param now The LocalDateTime to be assigned.
     */
    TimestampAppender(LocalDateTime now) {
        timestamp = timestampFrom(now);
    }

    /**
     * Constructor assuming that the timestamp is 'now'.
     */
    TimestampAppender() {
        this(LocalDateTime.now());
    }

    @Override
    public void putTo(YamlMap map) {
        map.put("timestamp", timestamp);
    }

    /**
     * Convert a LocalDateTime into a timestamp w/o fractional seconds, i.e. YYYY-MM-DD-HH-MM-SS
     *
     * @param time
     * @return String representation of the timestamp.
     */
    static String timestampFrom(LocalDateTime time) {
        return time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("\\.")[0];
    }
}
