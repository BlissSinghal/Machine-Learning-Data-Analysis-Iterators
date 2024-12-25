package org.cis1200;

import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

/**
 * {@code LineIterator} provides a useful wrapper around Java's provided
 * {@code BufferedReader} and provides practice with implementing an
 * {@code Iterator}. Your
 * solution should not read the entire file into memory at once, instead reading
 * a line whenever the {@code next()} method is called.
 * <p>
 * Notes:
 * - Any {@code IOExceptions} thrown be caught and handled properly.
 *
 * - Do not use the {@code ready()} method from {@code BufferedReader}.
 */
public class LineIterator implements Iterator<String> {


    BufferedReader reader;

    private String currentLine;
    private boolean reachedEnd;


    /**
     * Constructs a {@code LineIterator} for reader. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * The only method that should be called on BufferedReader is readLine() and
     * close(). You cannot call any other methods.
     *
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */
    public LineIterator(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("reader is null");
        }
        try {
            currentLine = reader.readLine();
        } catch (IOException e) {
            currentLine = null;
        }

        this.reader = reader;
    }

    public boolean getReachedEnd() {
        return reachedEnd;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    /**
     * Creates a LineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * <p>
     * This constructor is implemented for you. DO NOT MODIFY THIS CONSTRUCTOR.
     * 
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public LineIterator(String filePath) {
        this(FileUtilities.fileToReader(filePath));
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method attempts to close the
     * BufferedReader. In case of an IOException during the closing process,
     * an error message is printed to the console indicating the issue.
     *
     * @return a boolean indicating whether the LineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        //closing the reader if we have reached end
        if (currentLine == null) {
            try {
                reader.close();
                reachedEnd = true;
            } catch (IOException e) {
                System.out.println("Error closing file");
            }
        }
        return currentLine != null;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        String nextLine;
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        try {
            nextLine = reader.readLine();
        } catch (IOException e) {

            throw new NoSuchElementException();
        }
        String temp = currentLine;
        currentLine = nextLine;
        return temp;

    }
}
