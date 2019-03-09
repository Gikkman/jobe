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

import org.junit.Test;

public class NodePrototypeTest {
    @Test
    public void testGetTokens() {
        TestUtil testUtil = new TestUtil();
        Token t1 = testUtil.createToken("hello");
        Token t2 = testUtil.createToken("world");
        NodePrototype nodePrototype = new NodePrototype(t1, t2);

        assertEquals("tokens list wrong size", 2, nodePrototype.getTokens().size());
        assertEquals("first token wrong", t1, nodePrototype.getTokens().get(0));
        assertEquals("second token wrong", t2, nodePrototype.getTokens().get(1));
    }

    @Test
    public void testEqualsAndHashCode() {
        TestUtil tu = new TestUtil();
        Token t1 = tu.createToken("hello"), t2 = tu.createToken("world"), t3 = tu.createToken("lore");
        NodePrototype np1 = new NodePrototype(t1, t2);
        NodePrototype np2 = new NodePrototype(t1, t2);
        NodePrototype np3 = new NodePrototype(t1, t3);

        assertEquals("same NodePrototype didn't evaluate as equal", np1, np1);
        assertEquals("equal NodePrototype didn't evaluate as equal", np1, np2);
        assertNotEquals("different NodePrototype didn evaluate as equal", np1, np3);

        assertEquals("same NodePrototype didn't match hash code", np1.hashCode(), np1.hashCode());
        assertEquals("equal NodePrototype didn't match hash code", np1.hashCode(), np2.hashCode());
        assertNotEquals("different NodePrototype did match hash code", np1.hashCode(), np3.hashCode());

        assertNotEquals("NodePrototype was equal to null", np1, null);
        assertNotEquals("NodePrototype was equal to new Object()", np1, new Object());
        assertNotEquals("NodePrototype was equal to new Node with same tokens", np1, tu.createNode("hello", "world"));
    }

    @Test
    public void testToString() {
        NodePrototype prototype = new TestUtil().createNodePrototype("hello", "world", "lore", "ipsum");
        assertEquals("[hello-world-lore-ipsum]", prototype.toString());
    }
}
