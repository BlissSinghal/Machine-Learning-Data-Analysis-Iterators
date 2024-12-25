package org.cis1200;

import java.util.*;

/**
 * This inner class represents a "walk" through the Markov Chain as an
 * {@code Iterator} that yields each token encountered during the walk.
 * <p>
 * A walk through the chain is determined by the given {@code NumberGenerator},
 * which picks from among the choices of tokens according to their
 * probability distributions.
 * <p>
 * For example, given
 * 
 * <pre>
 *  ILLUSTRATIVE EXAMPLE MARKOV CHAIN:
 *  startTokens: { "a":2 }
 *  bigramFrequencies:
 *  "!":    { "and":1 }
 *  "?":    { "&lt;END&gt;":1 }
 *  "a":    { "banana":2  "chair":1  "table":1 }
 *  "and":  { "a":2 }
 *  "banana":   { "!":1  "?":1 }
 *  "chair":    { "&lt;END&gt;":1 }
 *  "table":    { "and":1 }
 * </pre>
 * 
 * The sequence of numbers 0 2 0 determines the (valid) walk consisting of the
 * three tokens
 * "a", "chair", and {@code END_TOKEN} as follows:
 * <ul>
 * <li>The first 0 picks out "a" from among the {@code startTokens}. (Since "a"
 * occurred
 * with frequency 2, either 0 or 1 would yield "a".)
 * <li>Next, the 2, picks out "chair" from the probability distribution over
 * bigrams
 * associated with "a" (0-1 map to "banana", 2 maps to "chair", and 3 maps to
 * "table").
 * <li>Finally, the last 0 picks out {@code END_TOKEN} from the bigrams
 * associated with
 * "chair".
 * </ul>
 * See the documentation for {@code pick} in
 * {@link ProbabilityDistribution#pick(int)}
 * for more details.
 */
class MarkovChainIterator implements Iterator<String> {

    // The iterator needs to have references to the data stored in the MarkovChain
    // the distribution of the startTokens and the bigramFrequencies. These are
    private final ProbabilityDistribution<String> startTokens;
    private final Map<String, ProbabilityDistribution<String>> bigramFrequencies;

    // stores the source of numbers that determine the path of ths walk
    private final NumberGenerator ng;

    // TODO: add field(s) used in implementing the Iterator functionality

    private String currToken;
    private boolean hasNext = true;

    /**
     * Constructs an iterator that follows the path specified by the given
     * {@code NumberGenerator}. The first token of the walk is chosen from
     * {@code startTokens}
     * by picking from that distribution using ng's first number. If the number
     * generator can
     * not provide a valid start index, or if there are no start tokens, any
     * exceptions should
     * be caught and the constructed Iterator should be empty (i.e., hasNext is
     * always false).
     *
     * @param startTokens       from the MarkovChain (assumed not null)
     * @param bigramFrequencies from the MarkovChain (assumed not null)
     * @param ng                the number generator to use for this walk (assumed
     *                          not null)
     */
    MarkovChainIterator(
            ProbabilityDistribution<String> startTokens,
            Map<String, ProbabilityDistribution<String>> bigramFrequencies,
            NumberGenerator ng
    ) {
        this.startTokens = startTokens;
        this.bigramFrequencies = bigramFrequencies;
        this.ng = ng;
        if (startTokens.getTotal() == 0) {
            hasNext = false;
        } else {
            try {
                currToken = startTokens.pick(ng);
            } catch (RuntimeException e) {
                hasNext = false;
            }
        }

    }

    /**
     * This method determines whether there is a next token in the
     * Markov Chain based on the current state of the walk. Remember that the
     * end of a sentence is denoted by the token {@code END_TOKEN}.
     * <p>
     * Your solution should be very short.
     *
     * @return true if {@link #next()} will return a non-{@code END_TOKEN} String
     *         and false otherwise
     */
    @Override
    public boolean hasNext() {
        return hasNext && !currToken.equals(MarkovChain.END_TOKEN);

    }

    /**
     * @return the next token in the MarkovChain's walk
     * @throws NoSuchElementException if there are no more tokens on the walk
     *                                through the chain (i.e. it has reached
     *                                {@code END_TOKEN}),
     *                                or if the number generator provides an invalid
     *                                choice
     *                                (e.g, an illegal argument for {@code pick}).
     */
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String nextToken;
        try {
            nextToken = bigramFrequencies.get(currToken).pick(ng);

        } catch (RuntimeException e) {
            hasNext = false;
            throw new NoSuchElementException();
        }

        //update curr token to be next token
        String temp = currToken;
        currToken = nextToken;
        return temp;


    }

}
