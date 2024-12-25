package org.cis1200;

import java.io.*;
import java.util.List;

/**
 * This is the class where everything you've worked on comes together!
 * You can see that we've provided a path to a CSV file of tweets and the
 * column from which they can be extracted. When run as an application, this
 * program builds a Markov Chain from the training data in the CSV file,
 * generates 10 random tweets, and prints them to the terminal.
 * <p>
 * There is nothing for you to do in this file -- it already contains the
 * code necessary to put your TwitterBot to work.
 * <p>
 * When run on the data from the illustrative_example.csv, we get output that
 * looks like:
 * 
 * <pre>
 * a table and a banana! and a chair
 * a table and a banana! and a banana! and a table and a chair
 * a chair
 * a chair
 * a banana! and a table and a banana! and a banana! and a banana! and a banana! and a banana?
 * a table and a banana?
 * a banana?
 * a table and a chair
 * a chair
 * a table and a banana! and a table and a chair*
 * </pre>
 *
 * <p>
 * When run on more realistic data from factretreiver_tweets.csv, we get
 * witticisms like
 * * the following (among much junkier random outputs):
 * *
 * 
 * <pre>
 *  * Octopuses can have belly buttons.
 *  * Sometimes a seed pods look like.
 *  * Tigers cannot breathe through their mouth.
 *  * The average of expensive material is called Chicken Feed.
 *  * Dogs are more efficient fliers than about his followers!
 *  * Humans can remember ditching school
 * </pre>
 */
public class TwitterBotMain {

    /**
     * This is a path to the CSV file containing the tweets. The main method
     * below uses the tweets in this file when calling TwitterBot. If you want
     * to run the TwitterBot on the other files we provide, change this path to
     * a different file. (You may need to adjust the TWEET_COLUMN too.)
     */
    static final String PATH_TO_TWEETS = "files/dailypenn_tweets.csv";
    /** Column in the PATH_TO_TWEETS CSV file to read tweets from */
    static final int TWEET_COLUMN = 2;
    /** File to store generated tweets */
    static final String PATH_TO_OUTPUT_TWEETS = "files/generated_tweets.txt";

    /**
     * Prints ten generated tweets to the console so that you can see how your bot
     * is
     * performing!
     */
    public static void main(String[] args) {

        // Obtain a Reader for processing the CSV file
        BufferedReader csvReader = FileUtilities.fileToReader(PATH_TO_TWEETS);

        // Extract all the CSV fields at the given TWEET_COLUMN
        List<String> rawTweets = CSV.csvFieldsAtColumn(csvReader, TWEET_COLUMN);

        // Parse the raw tweets into training data
        List<List<String>> trainingData = TweetParser.rawTweetsToTrainingData(rawTweets);

        // Construct a trained TwitterBot
        TwitterBot t = new TwitterBot(trainingData);

        // Uncomment the line below to see the MarkovChain produced from the given
        // training data
        // System.out.println(t.mc.toString());

        // Generate 10 random tweets and print them to the terminal
        List<String> tweets = t.generateRandomTweets(10);
        for (String tweet : tweets) {
            System.out.println(tweet);
        }

        // Write the generated tweets to the given file.
        // Make sure that PATH_TO_OUTPUT_TWEETS is set properly.
        FileUtilities.writeStringsToFile(tweets, PATH_TO_OUTPUT_TWEETS, false);
    }

}
