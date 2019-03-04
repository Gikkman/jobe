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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Gikkman
 */
public class Node implements Comparable<Node> {
    static final Node EMPTY = new Node(new ArrayList<>(), -1);

    private final List<Token> tokens;
    private final int id;
    private final int hashCache;

    private int weight = 0;

    Node(List<Token> tokens, int id) {
        this.tokens = tokens;
        this.id = id;
        this.hashCache = this.tokens.hashCode();
    }

    boolean isLeftmost() {
        return tokens.get(0).equals(JobeImpl.START_OF_CHAIN);
    }

    boolean isRightmost() {
        return tokens.get(tokens.size() - 1).equals(JobeImpl.END_OF_CHAIN);
    }

    List<Token> getTokens() {
        return this.tokens;
    }

    int getNodeID() {
        return id;
    }

    void incrementWeight() {
        this.weight++;
    }

    int getWeight() {
        return this.weight;
    }

    void writeTokens(StringBuilder b, int startIndex) {
        for (int i = startIndex; i < tokens.size(); i++)
            b.append(" ").append(tokens.get(i));
    }

    @Override
    public String toString() {
        return id + ":" + tokens.stream().map(Token::getString).collect(Collectors.joining("-", "[", "]"));
    }

    @Override
    public int hashCode() {
        return hashCache;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        return this.tokens.equals(other.tokens);
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.id, o.id);
    }
}
