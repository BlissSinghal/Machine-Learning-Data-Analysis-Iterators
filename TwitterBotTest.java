package org.cis1200;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;

/** Tests for TwitterBot class */
public class TwitterBotTest {
    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<>();
        Collections.addAll(l, words);
        return l;
    }

    /*
     * The below test case test whether your TwitterBot class itself is written
     * correctly. To generate a tweet of specific content, make a walk
     * containing the indices of the words you want to appear in the tweet and
     * use that as the {@code NumberGenerator}
     */

    private static final String[] TWEET_1 = { "a", "table", "and", "a", "chair" };
    private static final String[] TWEET_2 = { "a", "banana", "!", "and", "a", "banana", "?" };

    private static List<List<String>> getTestTrainingDataExample() {
        List<List<String>> trainingData = new LinkedList<>();
        trainingData.add(listOfArray(TWEET_1));
        trainingData.add(listOfArray(TWEET_2));
        return trainingData;
    }

    @Test
    public void generatorWorks() {
        TwitterBot tb = new TwitterBot(getTestTrainingDataExample());

        String expected = "a banana?";
        int[] walk = { 0, 0, 1, 0 };
        NumberGenerator ng = new ListNumberGenerator(walk);
        String acc = tb.generateTweet(ng);
        assertEquals(expected, acc);
    }

    /**
     * When the training data is empty, your TwitterBot should create an empty
     * MarkovChain.
     * An empty tweet should be generated by your bot.
     * No exceptions should be thrown and your program should not go into an
     * infinite loop!
     */
    @Test
    public void emptyTrainingDataCreatesEmptyTweet() {
        // Checks that your program does not go into an infinite loop
        assertTimeoutPreemptively(
                Duration.ofSeconds(10), () -> {
                    // No exceptions are thrown if training data is empty
                    TwitterBot tb = new TwitterBot(new LinkedList<>());
                    // Checks that the bot creates an empty tweet
                    assertEquals(0, tb.generateTweet().length());
                }
        );
    }

    /*
     * Add additional test cases here, modeled after the generatorWorks test shown
     * above.
     */

    @Test
    public void singleton() {
        //creating a singleton
        LinkedList<String> line = new LinkedList<>();
        line.add("word");

        //creating twitter bott
        List<List<String>> data = new LinkedList<>(new LinkedList<>());
        data.add(line);

        //making sure only generates that singleton
        TwitterBot tb = new TwitterBot(data);
        assertEquals("word", tb.generateTweet());
    }

    @Test
    public void createsTweetFromWalkPunctuatedCorrectly() {
        MarkovChain mc = new MarkovChain();

        int[] walk = {1, 0};

        String sentence1 = "Hello my name is Bliss !";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        List<List<String>> data = new LinkedList<>(new LinkedList<>());
        data.add(listOfArray(sentence1.split(" ")));
        data.add(listOfArray(sentence2));
        data.add(listOfArray(sentence3.split(" ")));

        //making sure that the walk works correctly
        assertEquals("Hello", mc.startTokens.pick(walk[0]));
        assertEquals("!", mc.bigramFrequencies.get("Hello").pick(walk[1]));
        assertEquals("name", mc.bigramFrequencies.get("!").pick(walk[0]));
        assertEquals("is", mc.bigramFrequencies.get("name").pick(walk[0]));


        TwitterBot tb = new TwitterBot(data);
        NumberGenerator ng = new ListNumberGenerator(walk);
        assertEquals("Hello! name is Johnny", tb.generateTweet(ng));

    }

    @Test
    public void createsTweetInvalidWalkIndex() {
        MarkovChain mc = new MarkovChain();


        int[] walk = {5, 0, 0};

        String sentence1 = "Hello my name is Bliss !";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        List<List<String>> data = new LinkedList<>(new LinkedList<>());
        data.add(listOfArray(sentence1.split(" ")));
        data.add(listOfArray(sentence2));
        data.add(listOfArray(sentence3.split(" ")));

        NumberGenerator ng = new ListNumberGenerator(walk);

        assertThrows(IllegalArgumentException.class, () -> mc.startTokens.pick(walk[0]));

        //should default to index 0
        assertEquals("Hello", mc.startTokens.pick(ng));
        TwitterBot tb = new TwitterBot(data);
        assertEquals("Bliss! name is Bliss Hello!", tb.generateTweet(ng));

    }

    @Test
    public void createsTweetEmptyRow() {
        int[] walk = {0, 1};

        String sentence1 = "Hello my name is Bliss !";
        String sentence3 = "Hello Johnny";

        //the second line added is empty list
        List<List<String>> data = new LinkedList<>(new LinkedList<>());
        data.add(listOfArray(sentence1.split(" ")));
        data.add(new LinkedList<>());
        data.add(listOfArray(sentence3.split(" ")));

        //should still gen. tweet
        TwitterBot tb = new TwitterBot(data);

        assertEquals("Hello my name is Bliss!",
                tb.generateTweet(new ListNumberGenerator(walk)));
    }



}
