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
package com.gikk.jobe2;

import com.gikk.RandomAccessSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Brain {
    static final Token START_OF_CHAIN = new TokenStop("\\s");
    static final Token END_OF_CHAIN = new TokenStop("\\e");

    final Map<String, Token> stringTokenMap = new HashMap<>();
    final Map<Token, RandomAccessSet<Node>> tokenNodeMap = new HashMap<>();

    final Map<NodePrototype, Node> nodeMap = new HashMap<>();

    final Map<Node, RandomAccessSet<Edge>> leftwards = new HashMap<>();
    final Map<Node, RandomAccessSet<Edge>> rightwards = new HashMap<>();

    private final int nodeLength, nodeOverlap;

    Brain() {
        this(3, 2);
    }

    Brain(int nodeLength, int nodeOverlap) {
        this.nodeLength = nodeLength;
        this.nodeOverlap = nodeOverlap;
    }

    synchronized Token[] registerTokens(String... prototypes) {
        Token[] tokens = new Token[prototypes.length];
        for (int i = 0; i < prototypes.length; i++) {
            String prototype = prototypes[i];
            Token token = stringTokenMap.computeIfAbsent(prototype, Token::new);
            token.incrementWeight();
            tokens[i] = token;
        }
        return tokens;
    }

    Token[] getTokens(String... split) {
        Token[] tokens = new Token[split.length];
        int i = 0;
        for (String s : split) {
            Token t = stringTokenMap.get(s);
            if (t != null)
                tokens[i++] = t;
        }
        return tokens;
    }

    Token getRandomToken(Random rng) {
        int size = stringTokenMap.size();
        if (size == 0)
            return null;

        int index = rng.nextInt(size);
        Iterator<Token> itr = stringTokenMap.values().iterator();
        Token out = itr.next();
        for (int i = 0; i < index; i++)
            out = itr.next();
        return out;
    }

    synchronized Node registerNode(NodePrototype prototype) {
        Node node = nodeMap.computeIfAbsent(prototype, (proto) -> {
            Node n = new Node(proto, nodeMap.size());
            for (Token token : proto.getTokens()) {
                tokenNodeMap.computeIfAbsent(token, (tt) -> new RandomAccessSet<>()).add(n);
            }
            return n;
        });
        node.incrementWeight();
        return node;
    }

    synchronized void registerRightwardRelation(Node origin, Node toTheRight) {
        rightwards.computeIfAbsent(origin, e -> new RandomAccessSet<>()).add(new Edge(origin, toTheRight));
    }

    synchronized void registerLeftwardRelation(Node origin, Node toTheLeft) {
        leftwards.computeIfAbsent(origin, e -> new RandomAccessSet<>()).add(new Edge(toTheLeft, origin));
    }

    Node getNodeLeftOf(Node node, Random rng) {
        return leftwards.get(node).getRandom(rng).getLeftNode();
    }

    Node getNodeRightOf(Node node, Random rng) {
        return rightwards.get(node).getRandom(rng).getRightNode();
    }

    Node getRandomNodeFromToken(Token token, Random rng) {
        return tokenNodeMap.get(token).getRandom(rng);
    }

    Chain generate(Token originToken, Random rng) {
        if (originToken == null)
            return new Chain(new ArrayDeque<>(), nodeOverlap);

        Node originNode = getRandomNodeFromToken(originToken, rng);
        Deque<Node> deque = new ArrayDeque<>();
        deque.add(originNode);

        Node left = originNode;
        while (!left.isLeftmost()) {
            left = getNodeLeftOf(left, rng);
            deque.addFirst(left);
        }

        Node right = originNode;
        while (!right.isRightmost()) {
            right = getNodeRightOf(right, rng);
            deque.addLast(right);
        }
        return new Chain(deque, nodeOverlap);
    }

    void learn(Token[] tokens) {
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
         * following series: "\\s I am" "am very hungry" "hungry \\e" As seen, the last node might be shorter than the
         * rest.
         *
         */
        int tokensIndex = 0;
        int nextTokenPos;
        List<Node> nodes = new ArrayList<>();

        // Special case for chains of length 1
        if (nodeLength == 1) {
            nextTokenPos = 0;
            NodePrototype proto = new NodePrototype(nextTokens);
            Node node = registerNode(proto);
            nodes.add(node);
        } else {
            nextTokenPos = 1;
        }

        while (tokensIndex < tokens.length) {
            nextTokens[nextTokenPos++] = tokens[tokensIndex++];
            if (nextTokenPos == nodeLength) {
                // Create a node and register it for a unique ID
                NodePrototype proto = new NodePrototype(nextTokens);
                Node node = registerNode(proto);
                nodes.add(node);

                // Shift array left LINK_LEN-nodeOverlap steps
                System.arraycopy(nextTokens, nodeLength - nodeOverlap, nextTokens, 0, nodeOverlap);
                nextTokenPos = nodeOverlap;
            }
        }

        nextTokens[nextTokenPos] = END_OF_CHAIN;
        NodePrototype proto = new NodePrototype(nextTokens);
        Node node = registerNode(proto);
        nodes.add(node);

        // Map edge relations
        Node previousNode = Node.EMPTY;
        Node currentNode = nodes.get(0);
        Node nextNode;
        for (int i = 1; i <= nodes.size(); i++) {
            nextNode = i == nodes.size() ? Node.EMPTY : nodes.get(i);

            // Register edge relations
            registerRightwardRelation(currentNode, nextNode);
            registerLeftwardRelation(currentNode, previousNode);

            // Move the "pointer" forward
            previousNode = currentNode;
            currentNode = nextNode;
        }
    }
}
