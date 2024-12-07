package ch.neukom.advent2024.util.splitter;

import com.google.common.base.Splitter;

public class Splitters {
    public static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    public static final Splitter WHITESPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();
    public static final Splitter COLON_SPLITTER = Splitter.on(':').trimResults().omitEmptyStrings();

    private Splitters() {
    }
}
