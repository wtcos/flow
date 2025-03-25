package za.co.wethinkcode.flow;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * An ordinary LinkedHashMap with utility functions to add new keys indirectly
 * by calling an appender, and to convert to its contents to
 * well-formatted YAML string.
 */
public class YamlMap extends LinkedHashMap<String, Object> {
    /**
     * @return Properly formatted YAML string
     */
    public String asYamlString() {
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        return yaml.dump(this);
    }

    /**
     * Applies each of the given appenders to append their fields to this map.
     *
     * @param appenders a varargs list of appenders to be called.
     */
    public void append(MapAppender... appenders) {
        for (int appender = 0; appender < appenders.length; appender++) {
            appenders[appender].putTo(this);
        }
    }
}
