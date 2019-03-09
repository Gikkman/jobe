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

public class EdgeTest {
    @Test
    public void testIsLeftMost() {
        TestUtil testUtil = new TestUtil();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Edge e1 = new Edge(Node.EMPTY, n1);
        Edge e2 = new Edge(n1, n2);
        Edge e3 = new Edge(n2, Node.EMPTY);

        assertTrue("Node.EMPTY as left node didn't make edge leftmost", e1.isLeftmost());
        assertFalse("two normal nodes made edge leftmost", e2.isLeftmost());
        assertFalse("Node.EMPTY as right node made edge leftmost", e3.isLeftmost());
    }

    @Test
    public void testIsRightMost() {
        TestUtil testUtil = new TestUtil();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Edge e1 = new Edge(Node.EMPTY, n1);
        Edge e2 = new Edge(n1, n2);
        Edge e3 = new Edge(n2, Node.EMPTY);

        assertFalse("Node.EMPTY as left node made edge rightmost", e1.isRightmost());
        assertFalse("two normal nodes made edge rightmost", e2.isRightmost());
        assertTrue("Node.EMPTY as right node didn't make edge leftmost", e3.isRightmost());
    }

    @Test
    public void testGetRightNode() {
        TestUtil testUtil = new TestUtil();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Edge edge = new Edge(n1, n2);
        assertEquals("the node passed to constructor as right, was not returned by getRightNode", n2,
                     edge.getRightNode());
        assertNotEquals("the node passed to constructor as left, was returned by getRightNode", n1,
                        edge.getRightNode());
    }

    @Test
    public void testGetLeftNode() {
        TestUtil testUtil = new TestUtil();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Edge edge = new Edge(n1, n2);
        assertEquals("the node passed to constructor as left, was not returned by getLeftNode", n1, edge.getLeftNode());
        assertNotEquals("the node passed to constructor as right, was returned by getLeftNode", n2, edge.getLeftNode());
    }

    @Test
    public void testToString() {
        TestUtil testUtil = new TestUtil();
        Node n1 = new Node(testUtil.createNodePrototype("hello", "world"), 1);
        Node n2 = new Node(testUtil.createNodePrototype("world", "!"), 2);
        Edge edge = new Edge(n1, n2);
        assertEquals("wrong toString value", "[1->2]", edge.toString());
    }

    @Test
    public void testHashCode() {
        TestUtil testUtil = new TestUtil();
        Node n1 = new Node(testUtil.createNodePrototype("hello", "world"), 1);
        Node n2 = new Node(testUtil.createNodePrototype("world", "!"), 2);
        Node n3 = new Node(testUtil.createNodePrototype("world", "ipsum"), 3);

        Edge edge = new Edge(n1, n2);
        Edge sameNodes = new Edge(n1, n2);
        Edge differentNodes = new Edge(n1, n3);

        assertEquals("same edge gave different hash code", edge.hashCode(), edge.hashCode());
        assertEquals("equal edge gave different hash code", edge.hashCode(), sameNodes.hashCode());
        assertNotEquals("different edge gave same hash code", edge.hashCode(), differentNodes.hashCode());
    }

    @Test
    public void testEquals() {
        TestUtil testUtil = new TestUtil();
        Node n1 = new TestNode(testUtil.createNodePrototype("hello", "world"), 1);
        Node n2 = new TestNode(testUtil.createNodePrototype("world", "!"), 2);
        Node n3 = new TestNode(testUtil.createNodePrototype("lore", "ipsum"), 3);
        Node n4 = new Node(testUtil.createNodePrototype("hello", "world"), 2);

        Edge edge = new Edge(n1, n2);
        Edge sameNodes = new Edge(n1, n2);
        Edge differentRightNode = new Edge(n1, n3);
        Edge differentLeftNode = new Edge(n3, n2);
        Edge differentHashCode = new Edge(n4, n2);

        assertEquals("same edge did not evaluate to equal", edge, edge);
        assertEquals("equal edge did not evaluate to equal", edge, sameNodes);
        assertNotEquals("edge with different right node was equal", edge, differentRightNode);
        assertNotEquals("edge with different left node was equal", edge, differentLeftNode);
        assertNotEquals("edge with different hash code was equal", edge, differentHashCode);

        assertNotEquals("edge was equal to null", edge, null);
        assertNotEquals("edge was equal to object of other type", edge, n1);
    }

    /**
     * Class for testing the case where the hash codes of two nodes matches, but their tokens does not.
     * It is a rather unlikely case, but it might happen so we want to cover it
     */
    private class TestNode extends Node {
        TestNode(NodePrototype prototype, int id) {
            super(prototype, id);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
