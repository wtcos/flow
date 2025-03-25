package za.co.wethinkcode.flow;

/**
 * A MapAppender that appends a type field
 */
public class TypeAppender implements MapAppender {
    private final String type;

    /**
     *
     * @param type the type string
     */
    public TypeAppender(String type) {
        this.type = type;
    }

    @Override
    public void putTo(YamlMap map) {
        map.put("type", type);
    }
}
