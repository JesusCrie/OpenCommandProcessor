package com.jesus_crie.open_command_processor.processor;

import com.jesus_crie.open_command_processor.CommandContext;
import com.jesus_crie.open_command_processor.CommandProcessingException;

import java.util.regex.Pattern;

public class CommandProcessor {

    private static final char SPACE_CHARACTER = ' ';
    private static final char ESCAPE_CHARACTER = '\\';
    private static final char QUOTE_SINGLE = '\'';
    private static final char QUOTE_DOUBLE = '"';
    private static final char OPTION_PREFIX = '-';
    private static final Pattern PATTERN_OPTION_ALLOWED = Pattern.compile("[a-zA-Z0-9-_]");
    private static final Pattern PATTERN_OPTION_SHORT_ALLOWED = Pattern.compile("[a-zA-Z0-9]");

    public ProcessedResult process(final CommandContext context, final String input) {
        if (input == null || input.length() == 0)
            throw new IllegalArgumentException("The input string is empty !");

        final Cursor cursor = new Cursor(input.trim());

        final ProcessedResult.Builder builder = new ProcessedResult.Builder();
        builder.setCommandName(readWord(cursor));

        char c;
        boolean wasPrefix = false;

        while (cursor.hasNext()) {
            c = cursor.read();

            // if there was a prefix before and now another one
            if (wasPrefix && c == OPTION_PREFIX) {
                builder.appendLongOption(readLongOption(cursor));
                wasPrefix = false;

                // if there was a single prefix before and now another character
            } else if (wasPrefix) {
                for (char op : readShortOptions(cursor))
                    builder.appendShortOption(op);
                wasPrefix = false;

                // An option prefix with nothing before
            } else if (c == OPTION_PREFIX) {
                wasPrefix = true;

                // The start of a quoted word
            } else if (c == QUOTE_DOUBLE || c == QUOTE_SINGLE) {
                builder.appendToPath(readQuotedString(cursor, c));
            }
        }

        return null;
    }

    /**
     * Read a simple word, until the next whitespace. No quote allowed, escape characters are working.
     *
     * @param cursor The current cursor.
     * @return The next word in the cursor.
     */
    public String readWord(final Cursor cursor) {
        final StringBuilder out = new StringBuilder();

        cursor.newEra();
        char c;

        while (cursor.hasNext()) {
            c = cursor.read();

            if (c == ESCAPE_CHARACTER) {
                out.append(readEscapedCharacter(cursor));
            } else if (c == SPACE_CHARACTER) {
                return out.toString();
            } else {
                out.append(c);
            }
        }

        return out.toString();
    }

    /**
     * Read and return the next character, ignoring it's nature.
     * Typically called for the character after the escape character (\).
     *
     * @param cursor The cursor.
     * @return The next character.
     * @throws CommandProcessingException If there is no character left to consume.
     */
    public char readEscapedCharacter(final Cursor cursor) {
        try {
            return cursor.read();
        } catch (StringIndexOutOfBoundsException e) {
            throw new CommandProcessingException("Can't escape next character !, nothing left !", cursor.eraStart, cursor.position);
        }
    }

    /**
     * Read and return the string contained between 2 characters, typically a quoted string.
     *
     * @param cursor  The cursor.
     * @param endChar The character denoting the end of the quote.
     * @return The entire string before the closing quote.
     */
    public String readQuotedString(final Cursor cursor, final char endChar) {
        final StringBuilder out = new StringBuilder();
        cursor.newEra();

        char c;
        while (cursor.hasNext()) {
            c = cursor.read();

            if (c == ESCAPE_CHARACTER)
                out.append(readEscapedCharacter(cursor));
            else if (c == endChar)
                return out.toString();
            else
                out.append(c);
        }

        return out.toString();
    }

    /**
     * Read a long option from the cursor, a word with only alphanumerical characters.
     *
     * @param cursor The targeted cursor.
     * @return The option that was parsed from the cursor.
     * @throws CommandProcessingException If the option contains illegal character(s).
     */
    public String readLongOption(final Cursor cursor) {
        final String option = readWord(cursor);

        if (!PATTERN_OPTION_ALLOWED.matcher(option).matches()) {
            throw new CommandProcessingException("Illegal character found in long option !", cursor.eraStart, cursor.position);
        }

        return option;
    }

    /**
     * Read one or more short options from the cursor.
     *
     * @param cursor The source cursor.
     * @return All of the short options contained in the cursor.
     * @throws CommandProcessingException If an illegal character is found in one of the options.
     */
    private char[] readShortOptions(final Cursor cursor) {
        char first = cursor.get();

        char[] options;
        if (cursor.hasNext() && cursor.getNext() != SPACE_CHARACTER) {
            final String ops = readLongOption(cursor); // Not very clean but it's the exact same thing that

            if (!PATTERN_OPTION_SHORT_ALLOWED.matcher(ops).matches()) {
                throw new CommandProcessingException("Illegal character found in short option !", cursor.eraStart, cursor.position);
            }

            options = new char[ops.length() + 1];
            options[0] = first;
            for (int i = 0; i < ops.toCharArray().length; i++)
                options[i + 1] = ops.charAt(i);

        } else {
            options = new char[]{first};
        }

        return options;
    }

    public Object suggest(final CommandContext context, final String input) {
        return null;
    }

    /**
     * A internal class to walk through a string step by step and eventually divide it in eras.
     */
    static class Cursor {

        private int position = -1;
        private int eraStart = 0;
        private String payload;

        public Cursor(final String payload) {
            this.payload = payload;
        }

        /**
         * @return True if {@link #read()} can be called without risk.
         */
        public boolean hasNext() {
            return position < payload.length() - 1;
        }

        /**
         * @return The next character of the payload.
         * @throws StringIndexOutOfBoundsException If there is no character left.
         */
        public char read() {
            position++;
            return get();
        }

        /**
         * Used to retrieve the character under the previously selected position.
         * Mainly used by {@link #read()}.
         *
         * @return The character under the current position.
         * @throws StringIndexOutOfBoundsException If the cursor is positioned after or before the payload.
         */
        public char get() {
            return payload.charAt(position);
        }

        /**
         * @return The character just before the position of the cursor.
         */
        public char getPrevious() {
            return payload.charAt(position - 1);
        }

        /**
         * @return The character just ahead of the current cursor.
         */
        public char getNext() {
            return payload.charAt(position + 1);
        }

        /**
         * @return The current position of the cursor on the payload.
         */
        public int getPosition() {
            return position;
        }

        /**
         * Start a new era on the current cursor. Typically when we switch from a word to another.
         * Used to mark the start of the part that is currently parsed.
         * <p>
         * The era will start one character ahead of the current position.
         */
        public void newEra() {
            eraStart = position + 1;
        }

        /**
         * @return The starting position of the current era.
         */
        public int getEraStart() {
            return eraStart;
        }

        /**
         * Reset the cursor and the era to the starting position.
         */
        public void reset() {
            position = -1;
            eraStart = 0;
        }
    }
}
