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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class JobeImpl implements Jobe {
    static final Token START_OF_CHAIN = new Token("\\s");
    static final Token END_OF_CHAIN = new Token("\\e");

    private final StringSplitter tokenizer = StringSplitter.getDefault();
    private final int nodeLength;
    private final int nodeOverlap;

    final Brain brain = new Brain();

    public JobeImpl(int nodeLength, int nodeOverlap) {
        this.nodeLength = nodeLength;
        this.nodeOverlap = nodeOverlap;
    }

    @Override
    public void consume(String s) {
        if (nodeLength <= nodeOverlap)
            return;
        if (nodeLength < 1)
            return;
        String[] split = tokenizer.split(s);
        Token[] tokens = brain.registerTokens(split);

        // Array for representing node segments
        Token[] nextTokens = new Token[nodeLength];
        nextTokens[0] = START_OF_CHAIN;

        /*
         * The idea is to construct several token series, where each series is equal to the previous one shifted a
         * number of steps to the left.
         *
         * The number of tokens in each segment is managed by LINK_LEN and the number of steps we shift left is LINK_LEN
         * - nodeOverlap. Say we have a LINK_LEN of 3, and an nodeOverlap of 1, then shifting "I am hungry" two steps
         * left gives us just "hungry _ _", which conveniently is one token of overlap.
         *
         * As an example, the sentence "I am hungry", with a LINK_LEN of 3 and overlap of 1, should produce the
         * following series: "\\s I am" "am very hungry" "hungry \\e \\e" As seen, we fill out the final series with
         * END_OF_CHAIN tokens.
         *
         */
        int tokensIndex = 0;
        int nextTokenPos;
        List<Node> nodes = new ArrayList<>();

        // Special case for chains of length 1
        if (nodeLength == 1) {
            nextTokenPos = 0;
            NodePrototype proto = new NodePrototype(nextTokens, 1);
            Node node = brain.registerNode(proto);
            nodes.add(node);
        } else {
            nextTokenPos = 1;
        }

        while (tokensIndex < tokens.length) {
            nextTokens[nextTokenPos++] = tokens[tokensIndex++];
            if (nextTokenPos == nodeLength) {
                // Create a node and register it for a unique ID
                NodePrototype proto = new NodePrototype(nextTokens, nextTokenPos);
                Node node = brain.registerNode(proto);
                nodes.add(node);

                // Shift array left LINK_LEN-nodeOverlap steps
                System.arraycopy(nextTokens, nodeLength - nodeOverlap, nextTokens, 0, nodeOverlap);
                nextTokenPos = nodeOverlap;
            }
        }

        nextTokens[nextTokenPos++] = END_OF_CHAIN;
        NodePrototype proto = new NodePrototype(nextTokens, nextTokenPos);
        Node node = brain.registerNode(proto);
        nodes.add(node);

        // Map edge relations
        Node previousNode = Node.EMPTY;
        Node currentNode = nodes.get(0);
        Node nextNode;
        for (int i = 1; i <= nodes.size(); i++) {
            nextNode = i == nodes.size() ? Node.EMPTY : nodes.get(i);

            // Register edge relations
            brain.registerRightwardRelation(currentNode, nextNode);
            brain.registerLeftwardRelation(currentNode, previousNode);

            // Move the "pointer" forward
            previousNode = currentNode;
            currentNode = nextNode;
        }
    }

    @Override
    public String produce(String i) {
        Random rng = new Random();
        Token token = brain.getToken(i);
        if (token == null)
            token = brain.getRandomToken(rng);

        Node originNode = brain.getRandomNodeFromToken(token, rng);
        if (originNode == null)
            return null;

        Chain chain = new Chain(originNode, nodeOverlap);

        Node left = originNode;
        while (!left.isLeftmost()) {
            left = brain.getNodeLeftOf(left, rng);
            chain.addFirst(left);
        }

        Node right = originNode;
        while (!right.isRightmost()) {
            right = brain.getNodeRightOf(right, rng);
            chain.addLast(right);
        }

        return chain.toString();
    }

    @Override
    public String consumeThenProduce(String input) {
        consume(input);
        return produce(input);
    }
}
