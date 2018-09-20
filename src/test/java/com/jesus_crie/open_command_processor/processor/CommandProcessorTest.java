package com.jesus_crie.open_command_processor.processor;

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
    @ArgumentsSource(value = QuoteProvider.class)
    void test_quotedString(final String value, final String expected) {
        final CommandProcessor.Cursor cursor = new CommandProcessor.Cursor(value);
        final String quote = processor.readQuotedString(cursor, cursor.read());
        assertThat(quote, equalTo(expected));
    }

    private static class QuoteProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of("\"hello \"", "hello "),
                    Arguments.of("'test ma cou\\'ille'", "test ma cou'ille"),
                    Arguments.of("\" hellow --hey \"", " hellow --hey ")
            );
        }
    }
}