package com.jesus_crie.open_command_processor.processor;

import com.jesus_crie.open_command_processor.CommandContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CommandProcessorTest {

    private static CommandProcessor processor;

    @BeforeAll
    static void beforeAll() {
        processor = new CommandProcessor();
    }

    @ParameterizedTest
    @ArgumentsSource(value = WordProvider.class)
    void test_readWord(final String data, final String expected) {
        final CommandProcessor.Cursor cursor = new CommandProcessor.Cursor(data);
        final String nextWord = processor.readWord(cursor);

        assertThat(nextWord, equalTo(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(value = QuoteProvider.class)
    void test_quotedString(final String value, final String expected) {
        final CommandProcessor.Cursor cursor = new CommandProcessor.Cursor(value);
        final String quote = processor.readQuotedString(cursor, cursor.read());

        assertThat(quote, equalTo(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(value = FullCommandProvider.class)
    void test_fullParseALot(final String data, final String expectedName, final String[] expectedPath, final String[] expectedLOp, final Character[] expectedSOp) {
        final ProcessedResult results = processor.process(CommandContext.empty(), data);
        // TODO 9/20/18 test
    }

    private static class WordProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("hey ho", "hey"),
                    Arguments.of("yo\\ ye", "yo ye")
            );
        }
    }

    private static class QuoteProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("\"hello \"", "hello "),
                    Arguments.of("'test ma cou\\'ille'", "test ma cou'ille"),
                    Arguments.of("\" hellow --hey \"", " hellow --hey ")
            );
        }
    }

    private static class FullCommandProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("test and some path",
                            "test",
                            new String[]{"and", "some", "path"},
                            new String[0],
                            new Character[0])
            );
        }
    }

}