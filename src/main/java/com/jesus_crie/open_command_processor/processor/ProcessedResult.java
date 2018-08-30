package com.jesus_crie.open_command_processor.processor;


public abstract class ProcessedResult {

    public abstract boolean isErrored();

    public abstract boolean hasOption(String arg);

    public abstract boolean optionHasArgument(String arg);

    public abstract String[] getCompletePath();
}
