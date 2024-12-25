package org.cis1200;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for LineIterator */
public class LineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to
        // test out our LineIterator if we do not want to. We can just
        // create a StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    @Test
    public void testEmpty() {
        String words = "";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        //making sure all future calls to hasNext return false
        assertNull(li.getCurrentLine());
        assertFalse(li.hasNext());
        assertFalse(li.hasNext());
        //making sure all future calls to next give the exception
        assertThrows(NoSuchElementException.class, li::next);
        assertThrows(NoSuchElementException.class, li::next);

    }

    @Test
    public void testNullArgConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new LineIterator((String) null));
    }

    @Test
    public void testSingleton() {
        String words = "only one line";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("only one line", li.next());
        assertFalse(li.hasNext());
        assertThrows(NoSuchElementException.class, li::next);
    }

    @Test
    public void testEmptyFirstLine() {
        String words = "\n new line";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        LineIterator li = new LineIterator(br);

        assertTrue(li.hasNext());
        assertEquals("", li.next());

        assertTrue(li.hasNext());
        assertEquals(" new line", li.next());
        assertFalse(li.hasNext());

        //making sure we closed the file
        assertTrue(li.getReachedEnd());
    }

    @Test
    public void testBufferReaderClosed() {
        String words = "only one line";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Error closing BufferedReader");
        }
        LineIterator li = new LineIterator(br);
        assertFalse(li.hasNext());
        assertThrows(NoSuchElementException.class, li::next);
    }

    @Test
    public void testInvalidFile() {
        assertThrows(IllegalArgumentException.class, () -> new LineIterator("random"));
    }

    @Test
    public void validFile() {
        LineIterator li = new LineIterator("files/homies.csv");
        assertTrue(li.hasNext());
        assertEquals("sup my homies", li.next());
    }








}
