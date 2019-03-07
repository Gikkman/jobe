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
package com.gikk.jobe1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Gikkman
 */
public class Node {
    private final List<String> tokens;
    private final int hashCache;

    private int id = -1;

    Node(int len, String... tokens) {
        this.tokens = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            this.tokens.add(i, tokens[i]);
        }

        this.hashCache = this.tokens.hashCode();
    }

    List<String> getTokens() {
        return this.tokens;
    }

    void setID(int id) {
        this.id = id;
    }

    int getNodeID() {
        return id;
    }

    public void writeToken(StringBuilder b, int index) {
        if (index >= tokens.size())
            return;
        b.append(" ").append(tokens.get(index));
    }

    @Override
    public String toString() {
        return id + ":" + tokens.stream().collect(Collectors.joining("-", "[", "]"));
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
        return Objects.equals(this.tokens, other.tokens);
    }
}
