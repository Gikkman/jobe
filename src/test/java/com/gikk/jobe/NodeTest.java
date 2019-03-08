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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NodeTest {
    @Test
    public void testGetId() {
        NodePrototype prototype = new TestUtil().createNodePrototype("hello", "world");
        Node n1 = new Node(prototype, 1);
        assertEquals("got wrong id", 1, n1.getNodeID());

        Node n2 = new Node(prototype, 555);
        assertEquals("got wrong id", 555, n2.getNodeID());
    }

    @Test
    public void testIsLeftMost() {
        TestUtil testUtil = new TestUtil();
        NodePrototype prototype1 = testUtil.createNodePrototype("hello", "world");
        Node n1 = new Node(prototype1, 1);
        assertFalse(n1.isLeftmost());

        NodePrototype prototype2 = new NodePrototype(TokenStop.START_OF_CHAIN, testUtil.createToken("hello"));
        Node n2 = new Node(prototype2, 2);
        assertTrue("START_OF_CHAIN should be left most", n2.isLeftmost());
    }

    @Test
    public void testIsRightMost() {
        TestUtil testUtil = new TestUtil();
        NodePrototype prototype1 = testUtil.createNodePrototype("hello", "world");
        Node n1 = new Node(prototype1, 1);
        assertFalse(n1.isRightmost());

        NodePrototype prototype2 = new NodePrototype(testUtil.createToken("hello"), TokenStop.END_OF_CHAIN);
        Node n2 = new Node(prototype2, 2);
        assertTrue("END_OF_CHAIN should be right momst", n2.isRightmost());
    }

    @Test
    public void testGetTokens() {
        Node n1 = new TestUtil().createNode("hello", "world");
        List<Token> tokens = n1.getTokens();
        assertEquals("first token should be 'hello'", "hello", tokens.get(0).getString());
        assertEquals("second token should be 'world'", "world", tokens.get(1).getString());
    }

    @Test
    public void testWeight() {
        Node n1 = new TestUtil().createNode("hello", "world");
        int weight = n1.getWeight();
        n1.incrementWeight();
        assertEquals("weight did not increase", weight + 1, n1.getWeight());
    }

    @Test
    public void testToString() {
        NodePrototype prototype = new TestUtil().createNodePrototype("hello", "world", "!");
        Node n1 = new Node(prototype, 55);
        assertEquals("55:[hello-world-!]", n1.toString());
    }

    @Test
    public void testEquals() {
        TestUtil testUtil = new TestUtil();
        NodePrototype prototype1 = testUtil.createNodePrototype("hello", "world");
        NodePrototype prototype2 = testUtil.createNodePrototype("world", "!");
        Node n1 = new Node(prototype1, 1);
        Node n2 = new Node(prototype1, 1);
        Node n3 = new Node(prototype1, 2);
        Node n4 = new Node(prototype2, 1);
        Node n5 = new Node(prototype2, 3);

        assertEquals("same nodes didn't evaluate to equal", n1, n1);
        assertEquals("equal nodes didn't evaluate to equal", n1, n2);
        assertNotEquals("same tokens, different id, evaluated as equal", n1, n3);
        assertNotEquals("different tokens, same id, evaluated as equal", n1, n4);
        assertNotEquals("different tokens, different id, evaluated as equal", n1, n5);

        assertNotEquals("token was equal to null", n1, null);
        assertNotEquals("token was equal to other type", n1, new Object());
    }
}
