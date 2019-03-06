/**
 * The MIT License
 *
 * Copyright 2018 Gikkman.
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
package com.gikk.jobe2;

public interface Jobe {
    void consume(String input);

    /**
     * Produces one candidate string from the brain, and returns the value
     *
     * @param input
     *            the string to sample for tokens
     * @return the produced string
     */
    String produce(String input);

    /**
     * Consumes the input string, then produces a single candidate string from the brain, and returns the value
     * 
     * @param input
     *            the string to consume into the brain, and to use to sample for tokens
     * @return the produced string
     */
    String consumeThenProduce(String input);

    /**
     * Returns a Jobe instance
     * 
     * @return an instance
     */
    public static Jobe getDefault() {
        return new JobeImpl(3, 2);
    }
}
