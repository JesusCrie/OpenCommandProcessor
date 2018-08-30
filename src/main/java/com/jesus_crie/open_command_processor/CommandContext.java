package com.jesus_crie.open_command_processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandContext {

    public static CommandContext fromData(final Object[][] data) {
        return new CommandContext(Arrays.stream(data)
                .collect(HashMap::new, (d, el) -> d.put((String) el[0], el[1]), HashMap::putAll));
    }

    @SafeVarargs
    public static CommandContext fromData(final Pair<String, Object>... data) {
        final Map<String, Object> map = new HashMap<>();
        for (Pair<String, Object> pair : data) map.put(pair.getKey(), pair.getValue());
        return new CommandContext(map);
    }

    private final Map<String, Object> data;

    private CommandContext(Map<String, Object> data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(final String key) {
        return (T) data.get(key);
    }
}
