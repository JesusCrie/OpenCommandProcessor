package com.jesus_crie.open_command_processor.processor;

import com.jesus_crie.open_command_processor.CommandContext;

import java.util.regex.Pattern;

public class CommandProcessor {

    private static final char ESCAPE_CHARACTER = '\\';
    private static final char QUOTE_SINGLE = '\'';
    private static final char QUOTE_DOUBLE = '"';
    private static final char OPTION_PREFIX = '-';
    private static final Pattern PATTERN_CHAR_ALLOWED = Pattern.compile("[a-zA-Z0-9-_]");

    public ProcessedResult process(final CommandContext context, final String input) {
        if (input.length() == 0)
            throw new IllegalArgumentException("The input string is empty !");

        final Cursor cursor = new Cursor(input.trim());

        // First there must be the name of the command



        return null;
    }

    public String readWord(final Cursor cursor) {
        cursor.newEra();
        char c;
        while (cursor.hasNext()) {
            c = cursor.read();
            // TODO 31/08/18
        }


        return null;
    }

    public Object suggest(final CommandContext context, final String input) {
        return null;
    }

    private static class Cursor {

        private int position = -1;
        private int eraStart = 0;
        private String payload;

        public Cursor(final String payload) {
            this.payload = payload;
        }

        public boolean hasNext() {
            return position < payload.length();
        }

        public char read() {
            position++;
            return get();
        }

        public char get() {
            return payload.charAt(position);
        }

        public int getPosition() {
            return position;
        }

        public void newEra() {
            eraStart = position;
        }

        public void reset() {
            position = eraStart = 0;
        }
    }
}
