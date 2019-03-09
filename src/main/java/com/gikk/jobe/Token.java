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

import java.util.Objects;

class Token {
    private final String string;
    private final int hashCache;
    private int points = 0;

    Token(String string) {
        this.string = string;
        this.hashCache = Objects.hash(string);
    }

    void incrementWeight() {
        this.points++;
    }

    int getWeight() {
        return this.points;
    }

    public String getString() {
        return this.string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Token token = (Token) o;
        if (this.hashCache != token.hashCache)
            return false;
        return Objects.equals(this.string, token.string);
    }

    @Override
    public int hashCode() {
        return this.hashCache;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
