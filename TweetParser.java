package org.cis1200;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides the method for turning a raw {@code String} into a
 * sequence of tokens to be used for training the Markov Chain.
 * <p>
 * There is no code you need to write for this class, but it can be helpful to
 * understand how the tokens are created. It is used in
 * {@link TwitterBotMain#main(String[])}.
 */
public class TweetParser {

    /**
     * Regular Expressions
     * <p>
     * For the purposes of this project, we consider "word characters" to be
     * alphanumeric characters [a-zA-Z0-9] and apostrophes ['], hashes [#],
     * and [@]. (We use those symbols so that "don't" "#hashtag" and "@user"
     * are parsed as single tokens.)
     * <p>
     * A <b>token</b> is either a {@code WORD_TOKEN}, which is a sequence of
     * word characters, or a {@code PUNCTUATION_TOKEN}, like "!" or "." .
     * Strings matching these constraints are described using <i>regular
     * expressions</i>
     * that the {@link Pattern} class uses to find matching substrings. See that
     * documentation for more details.
     * <p>
     * The {@code URL_REGEX} matches any substring that starts a word with
     * "http" or "https" and continues until some whitespace occurs. It is
     * used in the {@link #removeURLs(String)} static method.
     *
     */
    static final String WORD_TOKEN = "[\\p{Alnum}'@#]+";
    static final String PUNCTUATION_TOKEN = "\\p{Punct}";
    static final String TOKEN = WORD_TOKEN + "|" + PUNCTUATION_TOKEN;
    static final String URL_REGEX = "\\bhttp[s]?://\\S*";

    /**
     * Given a String, remove all substrings that look like a URL. Any word that
     * begins with the character sequence 'http' is simply replaced with the
     * empty string.
     *
     * @param s - a String from which URL-like words should be removed
     * @return s where each "URL-like" string has been deleted
     */
    static String removeURLs(String s) {
        return s.replaceAll(URL_REGEX, "");
    }

    /**
     * Converts a string into a list of training tokens by first removing
     * any URLs and then breaking up the string at any whitespace.
     *
     * @param tweet a single String to be used as a source of training data tokens
     * @return a list of tokens
     */
    static List<String> parseAndCleanTweet(String tweet) {
        List<String> cleanedTweet = new LinkedList<>();

        Pattern p = Pattern.compile(TOKEN);
        Matcher m = p.matcher(removeURLs(tweet));
        while (m.find()) {
            String word = m.group().trim();
            if (!word.isEmpty()) {
                cleanedTweet.add(word);
            }
        }
        return cleanedTweet;
    }

    /**
     * Applies the {@code parseAndCleanTweet} to a list of raw input tweets,
     * returning each cleaned tweet as a list of tokens to be used as training data.
     * <p>
     * If, after cleaning, a raw tweet has no tokens (i.e., is empty), it
     * is ignored and does not contribute to the training data.
     *
     * @param rawTweets a list of {@code Strings} to be parsed and cleaned as tweets
     * @return a list of training data examples
     */
    public static List<List<String>> rawTweetsToTrainingData(
            List<String> rawTweets
    ) {
        List<List<String>> data = new LinkedList<>();

        // clean every tweet and add all the resulting tweets to the
        // training data (if the result is non-empty)
        for (String tweet : rawTweets) {
            List<String> cleanedTweet = parseAndCleanTweet(tweet);
            if (!cleanedTweet.isEmpty()) {
                data.add(cleanedTweet);
            }
        }
        return data;
    }

}
