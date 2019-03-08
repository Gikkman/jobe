/**
 * The MIT License
 *
 * Copyright 2018-2019 Gikkman.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gikk.jobe;

import java.util.Random;

class JobeImpl implements Jobe {
    private final StringSplitter tokenizer = StringSplitter.getDefault();
    final Brain brain;

    /**
     * Creates a new JobeImpl instance.
     *
     * @param nodeLength
     *            the lenght of the chain segments (i.e. how many words)
     * @param nodeOverlap
     *            the overlap between adjacent chain segments
     * @throws IllegalArgumentException
     *             if nodeLenght < 1 or nodeOverlap < -1 or nodeLenght <= nodeOverlap
     */
    JobeImpl(int nodeLength, int nodeOverlap) throws IllegalArgumentException {
        if (nodeLength < 1)
            throw new IllegalArgumentException("NodeLength has to be greater than 0");
        if (nodeOverlap < 0)
            throw new IllegalArgumentException("NodeOverlap has to be greater than -1");
        if (nodeLength <= nodeOverlap)
            throw new IllegalArgumentException("NodeLength has to be greater than NodeOverlap");

        this.brain = new Brain(nodeLength, nodeOverlap);
    }

    @Override
    public String consumeThenProduce(String input) {
        consume(input);
        return produce(input);
    }

    @Override
    public void consume(String input) {
        String[] split = tokenizer.split(input);
        Token[] tokens = brain.registerTokens(split);

        brain.learn(tokens);
    }

    @Override
    public String produce(String input) {
        Random rng = new Random();
        String[] split = tokenizer.split(input);
        Token[] tokens = brain.getTokens(split);
        Token originToken = getOriginToken(tokens, rng);
        Chain chain = brain.generate(originToken, rng);
        return chain.toString();
    }

    private Token getOriginToken(Token[] tokens, Random rng) {
        /*
         * The token[] might contain null elements
         */
        int currentLowestWeight = -1;
        Token currentWinningToken = null;
        for (Token token : tokens) {
            // Tokens with weight 1 has only been seen once, and might be a simple misspelling.
            // Hence, we want a word to have at least occured twice
            if (token != null && token.getWeight() > currentLowestWeight && token.getWeight() > 1) {
                currentWinningToken = token;
            }
        }
        if (currentWinningToken == null) {
            currentWinningToken = brain.getRandomToken(rng);
        }
        return currentWinningToken;
    }
}
