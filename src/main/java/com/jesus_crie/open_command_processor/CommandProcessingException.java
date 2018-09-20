package com.jesus_crie.open_command_processor;

public class CommandProcessingException extends RuntimeException {

    private final int start;
    private final int end;

    public CommandProcessingException(final String message, final int start, final int end) {
        super(message);
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
