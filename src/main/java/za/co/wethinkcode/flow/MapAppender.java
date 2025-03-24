package za.co.wethinkcode.flow;

/**
 * An interface describing a class that knows how to
 * add fields to a YamlMap
 */
public interface MapAppender {
    /**
     * Add whatever fields this appender needs to add to the
     * passed in YamlMap.
     * @param map the destination map
     */
    void putTo(YamlMap map);
}
