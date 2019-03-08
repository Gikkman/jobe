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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class BrainTest {
    @Test
    public void testBrainStartsEmpty() {
        Brain brain = new Brain();
        assertEquals("Brain doesn't start with no tokens", 0, brain.stringTokenMap.size());
        assertEquals("Brain doesn't start with no nodes", 0, brain.tokenNodeMap.size());
        assertEquals("Brain doesn't start with empty nodeMap", 0, brain.nodeMap.size());
        assertEquals("Brain doesn't start with no left relations", 0, brain.leftwards.size());
        assertEquals("Brain doesn't start with no right relations", 0, brain.rightwards.size());
    }

    @Test
    public void testRegisterTokens() {
        Brain brain = new Brain();
        Token[] t1 = brain.registerTokens("hello");
        assertEquals("Brain didn't register a token for 'hello'", 1, brain.stringTokenMap.size());
        assertEquals("Brain didn't set initial weight to 1 for 'hello'", 1, t1[0].getWeight());

        Token[] t2 = brain.registerTokens("world");
        assertEquals("Brain didn't register a token for 'world", 2, brain.stringTokenMap.size());
        assertEquals("Brain didn't set initial weight to 1 for 'world'", 1, t2[0].getWeight());

        brain.registerTokens("world");
        assertEquals("Brain double registered a token for 'world'", 2, brain.stringTokenMap.size());
        assertEquals("Brain didn't increment weight to 2 for 'world'", 2, t2[0].getWeight());

        Token[] t3 = brain.registerTokens("hello", "world", "!");
        assertEquals("Brain didn't register a token for '!", 3, brain.stringTokenMap.size());
        assertEquals("Brain didn't increment weight to 2 for 'hello'", 2, t3[0].getWeight());
        assertEquals("Brain didn't increment weight to 3 for 'world'", 3, t3[1].getWeight());
        assertEquals("Brain didn't set initial weight to 1 for '!'", 1, t3[2].getWeight());
    }

    @Test
    public void testGetTokens() {
        /*
         * Worth knowing: the array from getTokens always has the same number of elements as the input array, but if an
         * element in the input array doesn't have a corresponding token, that element is skipped in the token
         * retrieval, and hence the tail of the output array becomes empty.
         */
        Brain brain = new Brain();
        brain.registerTokens("hello", "!", "world");
        Token[] t = brain.getTokens("hello", "world", "!!");
        assertNotNull("Brain didn't return a token for 'hello'", t[0]);
        assertEquals("Brain returned wrong token for 'hello'", "hello", t[0].getString());
        assertNotNull("Brain didn't return a token for 'world'", t[1]);
        assertEquals("Brain returned wrong token for 'world'", "world", t[1].getString());
        assertNull("Brain did return a token for '!!'", t[2]);
    }

    @Test
    public void testGetRandomToken() {
        Random rng = new Random();
        Brain brain = new Brain();
        assertNull("Brain.getRandomToken should return null if no token's been registered", brain.getRandomToken(rng));

        Set<Token> tokens = new HashSet<>();
        brain.registerTokens("hello", "world", "!");
        for (int i = 0; i < 10000; i++) {
            Token token = brain.getRandomToken(rng);
            assertNotNull("Brain.getRandomToken should not be null, if possible", token);
            tokens.add(token);
        }
        assertEquals("Didn't get all three tokens from getRandomToken, in 10K atemtps", 3, tokens.size());
    }

    @Test
    public void testRegisterNode() {
        NodePrototype n1 = new TestUtil().createNodePrototype("hello", "world");
        Brain brain = new Brain();
        Node node1 = brain.registerNode(n1);
        assertEquals("Brain didn't register a node for {'hello','world'}", 1, brain.nodeMap.size());
        assertEquals("Brain didn't map {'hello'} and {'world'} to {'hello','world'}", 2, brain.tokenNodeMap.size());
        assertEquals("Brain didn't set initial weight to 1 for {'hello','world'}", 1, node1.getWeight());
    }

    @Test
    public void testRegisterRightward() {
        TestUtil testUtil = new TestUtil();
        Random rng = new Random();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Brain brain = new Brain();
        brain.registerRightwardRelation(n1, n2);

        Node received = brain.getNodeRightOf(n1, rng);
        assertEquals("Wrong rightwards node", n2, received);
    }

    @Test
    public void testRegisterLeftward() {
        TestUtil testUtil = new TestUtil();
        Random rng = new Random();
        Node n1 = testUtil.createNode("hello", "world");
        Node n2 = testUtil.createNode("world", "!");

        Brain brain = new Brain();
        brain.registerLeftwardRelation(n1, n2);

        Node received = brain.getNodeLeftOf(n1, rng);
        assertEquals("Wrong leftward node", n2, received);
    }

}
