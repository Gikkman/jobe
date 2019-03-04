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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import com.gikk.RandomAccessSet;

public class Brain {
    final Map<String, Token> stringTokenMap = new HashMap<>();
    final Map<Token, RandomAccessSet<Node>> tokenNodeMap = new HashMap<>();

    final Map<NodePrototype, Node> nodeMap = new HashMap<>();

    final Map<Node, RandomAccessSet<Edge>> leftwards = new HashMap<>();
    final Map<Node, RandomAccessSet<Edge>> rightwards = new HashMap<>();

    synchronized Token[] registerTokens(String[] prototypes) {
        Token[] tokens = new Token[prototypes.length];
        for (int i = 0; i < prototypes.length; i++) {
            String prototype = prototypes[i];
            Token token = stringTokenMap.computeIfAbsent(prototype, Token::new);
            token.incrementWeight();
            tokens[i] = token;
        }
        return tokens;
    }

    synchronized Node registerNode(NodePrototype prototype) {
        Node node = nodeMap.computeIfAbsent(prototype, (proto) -> {
            Node n = new Node(proto.getTokens(), nodeMap.size());
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

    Token getToken(String string) {
        return stringTokenMap.get(string);
    }

    Token getRandomToken(Random rng) {
        int size = stringTokenMap.size();
        int index = rng.nextInt(size);
        Iterator<Token> itr = stringTokenMap.values().iterator();
        Token out = itr.next();
        for (int i = 0; i < index; i++)
            out = itr.next();
        return out;
    }

    public Node getRandomNodeFromToken(Token token, Random rng) {
        return tokenNodeMap.get(token).getRandom(rng);
    }
}
