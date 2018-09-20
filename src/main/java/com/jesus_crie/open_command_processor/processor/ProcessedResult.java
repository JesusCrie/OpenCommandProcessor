package com.jesus_crie.open_command_processor.processor;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProcessedResult {

    private final String name;
    private final String[] path;
    private final String[] longOptions;
    private final Character[] shortOptions;

    private ProcessedResult(final String name, final String[] path, final String[] longOptions, final Character[] shortOptions) {
        this.name = name;
        this.path = path;
        this.longOptions = longOptions;
        this.shortOptions = shortOptions;
    }

    public void validateOption() {
        // TODO 9/20/18 validate options and throw error if unknown or invalid option
    }

    public String[] getCompletePath() {
        return path;
    }

    String[] getRawLOptions() {
        return longOptions;
    }

    Character[] getRawSOptions() {
        return shortOptions;
    }

    public boolean hasOption(Object todo) {
        // TODO 9/20/18 create options object & check if present
        return false;
    }

    /*public abstract boolean isErrored();

    public abstract boolean hasOption(String arg);

    public abstract boolean optionHasArgument(String arg);

    public abstract String[] getCompletePath();*/

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

        public ProcessedResult build() {
            return new ProcessedResult(cmdName, path.toArray(new String[0]), options.toArray(new String[0]), shortOptions.toArray(new Character[0]));
        }
    }
}
