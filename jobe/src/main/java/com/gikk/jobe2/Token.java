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

import java.util.Objects;

class Token {
    private final String string;
    private int points = 0;
    private int hashCache = Integer.MIN_VALUE;

    Token(String string) {
        this.string = string;
    }

    void incrementWeight() {
        this.points++;
    }

    int getPoints() {
        return this.points;
    }

    public String getString() {
        return this.string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Token token = (Token) o;
        return Objects.equals(this.string, token.string);
    }

    @Override
    public int hashCode() {
        if (this.hashCache != Integer.MIN_VALUE)
            return this.hashCache;
        this.hashCache = Objects.hash(this.string);
        return this.hashCache;
    }

    @Override
    public String toString() {
        return string;
    }
}
