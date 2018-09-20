package com.jesus_crie.open_command_processor.processor;

import com.jesus_crie.open_command_processor.CommandContext;
import com.jesus_crie.open_command_processor.CommandProcessingException;

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
        final StringBuilder out = new StringBuilder();

        cursor.newEra();
        char c;
        while (cursor.hasNext()) {
            c = cursor.read();

            if (c == ESCAPE_CHARACTER)
                out.append(readEscapedCharacter(cursor));
            else if (c == QUOTE_SINGLE || c == QUOTE_DOUBLE) {
                // TODO 9/20/18 quoted string
            }
            // TODO 31/08/18
        }


        return null;
    }

    public char readEscapedCharacter(final Cursor cursor) {
        try {
            return cursor.read();
        } catch (StringIndexOutOfBoundsException e) {
            throw new CommandProcessingException("Can't escape next character !, nothing left !", cursor.eraStart, cursor.position);
        }
    }

    public String readQuotedString(final Cursor cursor, final char endChar) {
        final StringBuilder out = new StringBuilder();

        char c;
        while ((c = cursor.read()) != endChar) {
            if (c == ESCAPE_CHARACTER)
                out.append(readEscapedCharacter(cursor));
            else
                out.append(c);
        }

        return out.toString();
    }

    public Object suggest(final CommandContext context, final String input) {
        return null;
    }

    static class Cursor {

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

        public int getEraStart() {
            return eraStart;
        }

        public void reset() {
            position = eraStart = 0;
        }
    }
}
