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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class NodePrototype {
    private final List<Token> tokens = new ArrayList<>();

    NodePrototype(Token... tokens) {
        this.tokens.addAll(Arrays.asList(tokens));
    }

    List<Token> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodePrototype other = (NodePrototype) obj;
        return this.tokens.equals(other.tokens);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.tokens.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return tokens.stream().map(Token::toString).collect(Collectors.joining("-", "[", "]"));
    }
}
