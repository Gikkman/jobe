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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testWeight() {
        Token t1 = new Token("hello");
        assertEquals(0, t1.getWeight());
        t1.incrementWeight();
        assertEquals(1, t1.getWeight());
        t1.incrementWeight();
        assertEquals(2, t1.getWeight());
    }

    @Test
    public void testToString() {
        Token t1 = new Token("hello");
        assertEquals("hello", t1.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Token t1 = new Token("hello");
        Token t2 = new Token("hello");
        Token t3 = new Token("world");
        assertEquals("same token didn't evaluated to equal", t1, t1);
        assertEquals("equal token didn't evaluated to equal", t1, t2);
        assertNotEquals("different token evaluated to equal", t1, t3);

        assertEquals("same token didn't have equal hash code", t1.hashCode(), t1.hashCode());
        assertEquals("equal token didn't have equal hash code", t1.hashCode(), t2.hashCode());
        assertNotEquals("different token had equal hash code", t1.hashCode(), t3.hashCode());

        assertNotEquals("token equaled null", t1, null);
        assertNotEquals("token equaled other object", TokenStop.START_OF_CHAIN);
        assertNotEquals("token equaled other object", TokenStop.END_OF_CHAIN);
    }
}
