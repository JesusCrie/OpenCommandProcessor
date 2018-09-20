package com.jesus_crie.open_command_processor.processor;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ProcessedResult {

    public abstract boolean isErrored();

    public abstract boolean hasOption(String arg);

    public abstract boolean optionHasArgument(String arg);

    public abstract String[] getCompletePath();

    static class Builder {

        private String cmdName;
        private final LinkedList<String> path = new LinkedList<>();
        private final List<String> options = new ArrayList<>();
        private final List<Character> shortOptions = new ArrayList<>();

        public Builder setCommandName(final String cmdName) {
            this.cmdName = cmdName;
            return this;
        }

        public Builder appendToPath(final String path) {
            return this;
        }

        public Builder appendLongOption(final String longOption) {
            options.add(longOption);
            // TODO 9/20/18 check if the option take an argument and use the next path as the argument
            return this;
        }

        public Builder appendShortOption(final char op) {
            shortOptions.add(op);
            return this;
        }
    }
}
