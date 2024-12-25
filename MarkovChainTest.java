package org.cis1200;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for MarkovChain */
public class MarkovChainTest {

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /**
     * Helper function to make it easier to create singleton sets of Strings;
     * use this function in your tests as needed.
     *
     * @param s - the String to add to the set
     * @return - a Set containing just s
     */
    private static Set<String> singleton(String s) {
        Set<String> set = new TreeSet<>();
        set.add(s);
        return set;
    }

    /* **** ****** ***** ***** EXAMPLE TWEETS ***** ***** ****** **** */

    /*
     * Test your MarkovChain implementation!
     * Run this test case and check the printed results to see whether
     * your MarkovChain training agrees with the output below.
     *
     * ILLUSTRATIVE EXAMPLE MARKOV CHAIN:
     * startTokens: { "a":2 }
     * bigramFrequencies:
     * "!": { "and":1 }
     * "?": { "<END>":1 }
     * "a": { "banana":2 "chair":1 "table":1 }
     * "and": { "a":2 }
     * "banana": { "!":1 "?":1 }
     * "chair": { "<END>":1 }
     * "table": { "and":1 }
     *
     * We have started this test case for you. Add additional code to the test case
     * to completely characterize the state of the MarkovChain.
     */
    @Test
    public void testIllustrativeExampleMarkovChain() {
        /*
         * Note: we provide the pre-parsed sequence of tokens.
         */
        String[] tweet1 = { "a", "table", "and", "a", "chair" };
        String[] tweet2 = { "a", "banana", "!", "and", "a", "banana", "?" };

        MarkovChain mc = new MarkovChain();
        mc.addSequence(Arrays.stream(tweet1).iterator());
        mc.addSequence(Arrays.stream(tweet2).iterator());

        // Print out the Markov chain
        System.out.println("ILLUSTRATIVE EXAMPLE MARKOV CHAIN:\n" + mc);

        ProbabilityDistribution<String> pdBang = mc.get("!");
        assertEquals(singleton("and"), pdBang.keySet());
        assertEquals(1, pdBang.count("and"));

        ProbabilityDistribution<String> pdQuestion = mc.get("?");
        assertEquals(singleton(MarkovChain.END_TOKEN), pdQuestion.keySet());
        assertEquals(1, pdQuestion.count(MarkovChain.END_TOKEN));

        assertEquals(2, mc.startTokens.getTotal());
        assertEquals(2, mc.startTokens.count("a"));
        ProbabilityDistribution<String> pdA = mc.get("a");
        Set<String> keysA = new TreeSet<>();
        keysA.add("banana");
        keysA.add("chair");
        keysA.add("table");
        assertEquals(keysA, pdA.keySet());
        assertEquals(2, pdA.count("banana"));
        assertEquals(1, pdA.count("chair"));
        assertEquals(1, pdA.count("table"));

        //testing that the freqs associated with the next token are accurate
        //making sure pd for banana is correct
        ProbabilityDistribution<String> pdBanana = mc.get("banana");
        Set<String> keysBanana = new TreeSet<>();
        keysBanana.add("!");
        keysBanana.add("?");
        assertEquals(keysBanana, pdBanana.keySet());
        assertEquals(1, pdBanana.count("!"));
        assertEquals(1, pdBanana.count("?"));

        //doing the same for chair
        ProbabilityDistribution<String> pdChair = mc.get("chair");
        Set<String> keysChair = new TreeSet<>();
        keysChair.add(MarkovChain.END_TOKEN);
        assertEquals(keysChair, pdChair.keySet());
        //making sure the freq is only one
        assertEquals(1, pdChair.count(MarkovChain.END_TOKEN));

        //table
        ProbabilityDistribution<String> pdTable = mc.get("table");
        Set<String> keysTable = new TreeSet<>();
        keysTable.add("and");
        assertEquals(keysTable, pdTable.keySet());
        assertEquals(1, pdTable.count("and"));

    }

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.bigramFrequencies.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.bigramFrequencies.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    @Test
    public void testAddBigramNull() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.addBigram(null, "2"));
    }

    @Test
    public void testAddBigramMultiple() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        mc.addBigram("1", "3");
        mc.addBigram("1", "3");
        mc.addBigram("2", "3");

        assertTrue(mc.bigramFrequencies.containsKey("1"));
        assertTrue(mc.bigramFrequencies.containsKey("2"));
        assertEquals(2, mc.bigramFrequencies.keySet().size());
        ProbabilityDistribution<String> pd = mc.bigramFrequencies.get("1");
        Set<String> keys = new TreeSet<>();
        keys.add("2");
        keys.add("3");
        assertEquals(keys, pd.keySet());
        assertEquals(1, pd.count("2"));
        assertEquals(2, pd.count("3"));

        ProbabilityDistribution<String> pd2 = mc.bigramFrequencies.get("2");
        assertEquals(singleton("3"), pd2.keySet());
        assertEquals(1, pd2.count("3"));
    }



    /* ***** ****** ***** ***** ADD SEQUENCE TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddSequence() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.addSequence(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.bigramFrequencies.size());
        ProbabilityDistribution<String> pd1 = mc.bigramFrequencies.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.bigramFrequencies.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.bigramFrequencies.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testAddSequenceNull() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.addSequence(null));
        assertTrue(mc.startTokens.keySet().isEmpty());
        assertTrue(mc.bigramFrequencies.keySet().isEmpty());

    }

    @Test
    public void testAddSequenceEmptyIterator() {
        MarkovChain mc = new MarkovChain();
        String[] emptyArray = new String[0];

        mc.addSequence(Arrays.stream(emptyArray).iterator());
        assertTrue(mc.startTokens.keySet().isEmpty());
        assertTrue(mc.bigramFrequencies.keySet().isEmpty());

    }

    @Test
    public void testAddSequenceMultiple() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "Hello my name is Bliss !";
        //"Bliss Hello! name is Johnny";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        //checking to make sure start tokens is 2
        Set<String> startTokens = new TreeSet<>();
        startTokens.add("Hello");
        startTokens.add("Bliss");
        assertEquals(startTokens, mc.startTokens.keySet());

        //checking start tokens' prob distributoins
        ProbabilityDistribution<String> pdStart = mc.startTokens;
        Set<String> keysStart = new TreeSet<>();
        keysStart.add("Hello");
        keysStart.add("Bliss");
        assertEquals(keysStart, pdStart.keySet());
        assertEquals(2, pdStart.count("Hello"));
        assertEquals(1, pdStart.count("Bliss"));

        //checking bigram freqs prob distributions
        //hello
        ProbabilityDistribution<String> pdHello = mc.bigramFrequencies.get("Hello");
        Set<String> keysHello = new TreeSet<>();
        keysHello.add("my");
        keysHello.add("!");
        keysHello.add("Johnny");
        assertEquals(keysHello, pdHello.keySet());
        assertEquals(1, pdHello.count("my"));
        assertEquals(1, pdHello.count("!"));
        assertEquals(1, pdHello.count("Johnny"));

        //Bliss
        ProbabilityDistribution<String> pdBliss = mc.bigramFrequencies.get("Bliss");
        Set<String> keysBliss = new TreeSet<>();
        keysBliss.add("Hello");
        keysBliss.add("!");
        assertEquals(keysBliss, pdBliss.keySet());
        assertEquals(1, pdBliss.count("Hello"));
        assertEquals(1, pdBliss.count("!"));

        //name
        ProbabilityDistribution<String> pdName = mc.bigramFrequencies.get("name");
        Set<String> keysName = new TreeSet<>();
        keysName.add("is");
        assertEquals(keysName, pdName.keySet());
        assertEquals(2, pdName.count("is"));

        //is
        ProbabilityDistribution<String> pdIs = mc.bigramFrequencies.get("is");
        Set<String> keysIs = new TreeSet<>();
        keysIs.add("Johnny");
        keysIs.add("Bliss");
        assertEquals(keysIs, pdIs.keySet());
        assertEquals(1, pdIs.count("Johnny"));
        assertEquals(1, pdIs.count("Bliss"));

        //johnny
        ProbabilityDistribution<String> pdJohnny = mc.bigramFrequencies.get("Johnny");
        Set<String> keysJohnny = new TreeSet<>();
        keysJohnny.add(MarkovChain.END_TOKEN);
        assertEquals(keysJohnny, pdJohnny.keySet());
        assertEquals(2, pdJohnny.count(MarkovChain.END_TOKEN));





    }

    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /*
     * Here's an example test case for walking through the Markov Chain.
     * Be sure to add your own as well
     */
    @Test
    public void testWalk() {
        /*
         * Using the training data "CIS 1200 rocks" and "CIS 1200 beats CIS 1600",
         * we're going to put some bigrams into the Markov Chain.
         *
         * The given sequence of numbers acts as a path through the Markov Model
         * that should be followed by {@code walk}. Note that the sequence
         * includes a choice for {@code END_TOKEN}, so the length is one longer
         * than the {@code expectedTokens}.
         *
         */

        String[] expectedTokens = { "CIS", "1200", "beats", "CIS", "1200", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2.split(" ")).iterator());

        // it can be illustrative to print out the state of the Markov Chain at this
        // point
        System.out.println(mc);

        Integer[] seq = { 0, 0, 0, 0, 0, 1, 0 };
        List<Integer> choices = new ArrayList<>(Arrays.asList(seq));
        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(choices));
        for (String token : expectedTokens) {
            assertTrue(walk.hasNext());
            String acc = walk.next();
            assertEquals(token, acc);
        }
        assertFalse(walk.hasNext());

    }

    @Test
    public void testWalk2() {
        /* We can also use the provided method */

        String[] expectedWords = { "CIS", "1600" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2.split(" ")).iterator());

        List<Integer> choices = mc.findWalkChoices(new ArrayList<>(Arrays.asList(expectedWords)));
        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(choices));
        for (String word : expectedWords) {
            assertTrue(walk.hasNext());
            assertEquals(word, walk.next());
        }

    }

    @Test
    public void testWalk3() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "Hello my name is Bliss !";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        List<Integer> accWalkChoices =
                mc.findWalkChoices(new ArrayList<>(Arrays.asList("Hello", "Johnny")));

        assertEquals(3, accWalkChoices.size());


        assertEquals(mc.startTokens.pick(accWalkChoices.get(1)), "Hello");
        assertEquals(mc.bigramFrequencies.get("Hello").pick(accWalkChoices.get(1)), "Johnny");
        assertEquals(mc.bigramFrequencies.get("Johnny").pick(accWalkChoices.get(0)),
                MarkovChain.END_TOKEN);
        assertNull(mc.bigramFrequencies.get(MarkovChain.END_TOKEN));

        Iterator<String> walk = mc.getWalk(new ListNumberGenerator(accWalkChoices));
        for (String token: Arrays.asList("Hello", "Johnny")) {
            assertTrue(walk.hasNext());
            assertEquals(token, walk.next());
        }
        assertFalse(walk.hasNext());

    }

    @Test
    public void testWalkNullTokens() {
        MarkovChain mc = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc.findWalkChoices(null));
    }


    @Test
    public void testWalkInvalidWalkIndexConstructor() {
        MarkovChain mc = new MarkovChain();
        int[] walk = {5};

        String sentence1 = "Hello my name is Bliss !";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        ListNumberGenerator ng = new ListNumberGenerator(walk);
        Iterator<String> walkIterator = mc.getWalk(ng);

        //technically invalid index
        assertThrows(IllegalArgumentException.class, () -> mc.startTokens.pick(walk[0]));

        //but it will NOT throw bc the ng.next will return a 0
        assertEquals("Hello", mc.startTokens.pick(ng));

        //should give first element in start tokens
        assertTrue(walkIterator.hasNext());
        assertEquals("Hello", walkIterator.next());

        //should keep giving the first element
        assertTrue(walkIterator.hasNext());
        assertEquals("my", walkIterator.next());

        assertEquals("name", walkIterator.next());


    }

    @Test
    public void testWalkEmptyStartTokens() {
        MarkovChain mc = new MarkovChain();
        int[] walk = {0};
        Iterator<String> walkIterator = mc.getWalk(new ListNumberGenerator(walk));

        //making sure start tokens is empty
        assertTrue(mc.startTokens.keySet().isEmpty());

        //making sure that walkIterator is empty
        assertFalse(walkIterator.hasNext());
        assertThrows(NoSuchElementException.class, walkIterator::next);

        //subsequent calls produce same results
        assertFalse(walkIterator.hasNext());
        assertThrows(NoSuchElementException.class, walkIterator::next);

    }

    @Test
    public void testWalkTooShort() {
        MarkovChain mc = new MarkovChain();

        int[] walk = {1};

        String sentence1 = "Hello my name is Bliss !";
        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        Iterator<String> walkIterator = mc.getWalk(new ListNumberGenerator(walk));

        //making sure that the walk works correctly
        assertEquals("Hello", mc.startTokens.pick(walk[0]));
        assertEquals("Johnny", mc.bigramFrequencies.get("Hello").pick(walk[0]));

        //walk iterator should go back to beginning if it reaches end of the list
        assertTrue(walkIterator.hasNext());
        assertEquals("Hello", walkIterator.next());

        //next iteration...
        assertTrue(walkIterator.hasNext());
        assertEquals("Johnny", walkIterator.next());

        //next iteration...
        assertFalse(walkIterator.hasNext());



    }

    @Test
    public void testWalkNegativeIndex() {
        MarkovChain mc = new MarkovChain();
        int[] walk = {-1};
        String sentence1 = "Hello my name is Bliss !";

        String[] sentence2 = {"Bliss", "Hello", "!", "name", "is", "Johnny"};
        String sentence3 = "Hello Johnny";
        mc.addSequence(Arrays.stream(sentence1.split(" ")).iterator());
        //mc.addSequence(Arrays.stream(sentence2).iterator());
        mc.addSequence(Arrays.stream(sentence3.split(" ")).iterator());

        ListNumberGenerator ng = new ListNumberGenerator(walk);
        Iterator<String> walkIterator = mc.getWalk(ng);

        //technically should give error
        assertThrows(IllegalArgumentException.class, () -> mc.startTokens.pick(walk[0]));

        //but list number gen makes it so that doesn't happen and returns 0 instead
        assertEquals("Hello", mc.startTokens.pick(0));

        //making it sure it defaults to index of 0
        assertTrue(walkIterator.hasNext());
        assertEquals("Hello", walkIterator.next());

        assertTrue(walkIterator.hasNext());
        assertEquals("Johnny", walkIterator.next());

        assertFalse(walkIterator.hasNext());
        assertThrows(NoSuchElementException.class, walkIterator::next);

    }

}
