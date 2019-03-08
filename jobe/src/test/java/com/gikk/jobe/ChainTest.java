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

public class ChainTest {

    @Test
    public void testEqualsAndHashCode() {
        TestUtil testUtil = new TestUtil();
        Node helloWorld = testUtil.createNode("hello", "world");
        Node helloLore = testUtil.createNode("hello", "lore");

        Chain short1 = testUtil.createChain(1, helloWorld);
        Chain short2 = testUtil.createChain(1, helloWorld);
        Chain short3 = testUtil.createChain(1, helloLore);

        assertEquals("Same chains evaluated as equal", short1, short1);
        assertEquals("Equal length, equal nodes evaluated as equal", short1, short2);
        assertNotEquals("Equal length, different nodes evaluated as equal", short1, short3);
        assertNotEquals("Equal length, different nodes evaluated as equal", short2, short3);
        assertEquals("Equal length, equal nodes had different hash code", short1.hashCode(), short2.hashCode());
        assertNotEquals("Equal length, different nodes had same hash code", short1.hashCode(), short3.hashCode());
        assertNotEquals("Equal length, different nodes had same hash code", short2.hashCode(), short3.hashCode());

        Chain short3DiffLength = testUtil.createChain(2, helloLore);
        assertNotEquals("Different length, equal nodes evaluated as different", short3, short3DiffLength);
        assertNotEquals("Different length, equal nodes had same hash code", short3.hashCode(),
                        short3DiffLength.hashCode());

        Node worldOne = testUtil.createNode("world", "!");
        Chain long1 = testUtil.createChain(1, helloWorld, worldOne);
        Chain long2 = testUtil.createChain(1, helloWorld, worldOne);
        assertEquals("Equal length, equal nodes evaluated as different", long1, long2);
        assertEquals("Equal length, equal nodes had different hash code", long1.hashCode(), long2.hashCode());

        Node oneOneOne = testUtil.createNode("!", "!!");
        Chain longest = testUtil.createChain(1, helloWorld, worldOne, oneOneOne);
        assertNotEquals("Equal length, different nodes evaluated as equal", long1, longest);
        assertNotEquals("Equal length, different nodes had same hash code", long1.hashCode(), longest.hashCode());

        assertNotEquals("Chain equal to null", short1, null);
        assertNotEquals("Chain equal to other type", short1, new Object());
    }

    @Test
    public void testToString() {
        TestUtil testUtil = new TestUtil();
        Node n1 = testUtil.createNode("hello", "my", "world");
        Node n2 = testUtil.createNode("my", "world", "!");

        Chain c1 = testUtil.createChain(0, n1, n2);
        assertEquals("Wrong toString() result", "hello my world my world !", c1.toString());

        Chain c2 = testUtil.createChain(1, n1, n2);
        assertEquals("Wrong toString() result", "hello my world world !", c2.toString());

        Chain c3 = testUtil.createChain(2, n1, n2);
        assertEquals("Wrong toString() result", "hello my world !", c3.toString());
    }
}
