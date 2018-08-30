package com.jesus_crie.open_command_processor;

public interface CommandPath {

    boolean hasSubPath(final CommandContext context, final String path);

    String describePath();
}
