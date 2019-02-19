/*
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

import com.gikk.RandomAccessSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Jobe {
    static final Token START_OF_CHAIN = new Token("\\s");
    static final Token END_OF_CHAIN = new Token("\\e");
    private static final int NODE_LEN = 3;
    private static final int OVERLAP = 2;

    private final Map<String, Token> TOKENS = new HashMap<>();
    private final Map<Token, RandomAccessSet<Node>> TOKEN_TO_NODES = new HashMap<>();

    private final Map<NodePrototype, Node> NODE_MAP = new HashMap<>();

    private final Map<Node, RandomAccessSet<Edge>> LEFTWARDS = new HashMap<>();
    private final Map<Node, RandomAccessSet<Edge>> RIGHTWARDS = new HashMap<>();

    public static void main(String ... args) {
        Set<String> results = new HashSet<>();

        Jobe jobe = new Jobe();
        jobe.consume("this is gikk test");
        jobe.consume("this is a test");
        jobe.consume("this is another test");
        jobe.consume("this is another test yo");
        jobe.consume("another test would be good");
        jobe.consume("test test");

        for(int i = 0; i < 10000; i++) {
            try{
                results.add(jobe.produce("test"));
                results.add(jobe.produce("gikkman"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("\nOutput:");
        results.stream().forEach(System.out::println);

        System.out.println("\nNodes:");
        jobe.NODE_MAP.values().stream()
            .sorted(Comparator.comparingInt(Node::getNodeID))
            .forEach(n -> System.out.println(n.toString()));

        System.out.println("\nTokens:");
        for(Map.Entry<Token, RandomAccessSet<Node>> e : jobe.TOKEN_TO_NODES.entrySet()) {
            String val = e.getValue().stream().map(Node::toString).collect(Collectors.joining(","));
            String key = String.format("Key: %-8s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);        
        }

        System.out.println("\nEdges LEFT:");
        for(Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.LEFTWARDS.entrySet()) {
           String val = e.getValue().stream()
                .sorted( (Edge e1, Edge e2) ->
                    e1.getLeftNode().getNodeID() - e2.getLeftNode().getNodeID()
                ).map(Edge::toString)
                .collect(Collectors.joining(","));
            String key = String.format("Key: %-4s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);
        }

        System.out.println("\nEdges RIGHT:");
        for(Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.RIGHTWARDS.entrySet()) {
            String val = e.getValue().stream()
                .sorted( (Edge e1, Edge e2) ->
                    e1.getRightNode().getNodeID() - e2.getRightNode().getNodeID()
                ).map(Edge::toString)
                .collect(Collectors.joining(","));
            String key = String.format("Key: %-4s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);
        }
    }

    public Jobe() {}

    public void consume(String s) {
        if( NODE_LEN <= OVERLAP ) return;
        if( NODE_LEN < 1 ) return;

        // Split incoming message into tokens
        String[] split = s.split("\\s+");
        int tokensLenght = split.length;
        Token[] tokens = new Token[tokensLenght];
        for(int i = 0; i < tokensLenght; i++) tokens[i] = registerToken(split[i]);

        // Array for representing node segments
        Token[] nextTokens = new Token[NODE_LEN];
        nextTokens[0] = START_OF_CHAIN;

        /** The idea is to construct several token series, where each series
         * is equal to the previous one shifted a number of steps to the left.
         *
         * The number of tokens in each segment is managed by LINK_LEN and the
         * number of steps we shift left is LINK_LEN - OVERLAP. Say we have a
         * LINK_LEN of 3, and an OVERLAP of 1, then shifting "I am hungry" two
         * steps left gives us just "hungry _ _", which conveniently is one
         * token of overlap.
         *
         * As an example, the sentence "I am hungry", with a LINK_LEN of 3 and
         * overlap of 1, should produce the following series:
         * "\\s I am"
         * "am very hungry"
         * "hungry \\e \\e"
         * As seen, we fill out the final series with END_OF_CHAIN tokens.
         *
         */
        int tokensIndex = 0;
        int nextTokenPos;
        List<Node> nodes = new ArrayList<>();

        // Special case for chains of length 1
        if(NODE_LEN == 1) {
            nextTokenPos = 0;
            NodePrototype proto = new NodePrototype(nextTokens, 1);
            Node node = registerNode(proto);
            nodes.add(node);
        } else {
            nextTokenPos = 1;
        }

        while(tokensIndex < tokensLenght){
            nextTokens[nextTokenPos++] = tokens[tokensIndex++];
            if(nextTokenPos == NODE_LEN) {
                // Create a node and register it for a unique ID
                NodePrototype proto = new NodePrototype(nextTokens, nextTokenPos);
                Node node = registerNode(proto);
                nodes.add(node);

                 //Shift array left LINK_LEN-OVERLAP steps
                System.arraycopy(nextTokens, NODE_LEN-OVERLAP, nextTokens, 0, OVERLAP);
                nextTokenPos = OVERLAP;
            }
        }

        nextTokens[nextTokenPos++] = END_OF_CHAIN;
        NodePrototype proto = new NodePrototype(nextTokens, nextTokenPos);
        Node node = registerNode(proto);
        nodes.add(node);

        // Map edge relations
        Node previousNode = Node.EMPTY;
        Node currentNode = nodes.get(0);
        Node nextNode;
        for(int i = 1; i <= nodes.size(); i++) {
            nextNode = i == nodes.size() ? Node.EMPTY : nodes.get(i);

            // Register edge relations
            RIGHTWARDS.computeIfAbsent(
                    currentNode,
                    e -> new RandomAccessSet<>())
                .add(new Edge(currentNode, nextNode));
            LEFTWARDS.computeIfAbsent(
                    currentNode,
                    e -> new RandomAccessSet<>())
                .add(new Edge(previousNode, currentNode));

            // Move the "pointer" forward
            previousNode = currentNode;
            currentNode = nextNode;
        }
    }

    private synchronized Token registerToken(String prototype) {
        Token token = TOKENS.computeIfAbsent(
            prototype,
            (proto) -> {
                Token t = new Token(proto);
                return t;
            }
        );
        token.addPoint();
        return token;
    }

    private synchronized Node registerNode(NodePrototype prototype) {
        Node node = NODE_MAP.computeIfAbsent(
            prototype,
            (proto) -> {
                Node n = new Node(proto.getTokens(), NODE_MAP.size());
                for(Token token : proto.getTokens()) {
                    TOKEN_TO_NODES.computeIfAbsent(
                            token,
                            (tt) -> new RandomAccessSet<>()
                        )
                        .add(n);
                }
                return n;
            }
        );
        node.incrementWeight();
        return node;
    }

    private String produce(String i) {
        Random rng = new Random();
        Token token = TOKENS.get(i);
        if(token == null) token = getRandomToken(rng);
        Node originNode = TOKEN_TO_NODES.get(token).getRandom(rng);
        if(originNode == null) return null;

        Deque<Node> nodes = new ArrayDeque<>();
        nodes.add(originNode);

        Node left = originNode;
        while(!left.isLeftmost()) {
            RandomAccessSet<Edge> ras = LEFTWARDS.get(left);
            left = ras.getRandom(rng).getLeftNode();
            nodes.addFirst(left);
        }

        Node right = originNode;
        while(!right.isRightmost()) {
            RandomAccessSet<Edge> ras = RIGHTWARDS.get(right);
            right = ras.getRandom(rng).getRightNode();
            nodes.addLast(right);
        }

        // Print the NODES
        StringBuilder builder = new StringBuilder();
        int j = 0;
        for(Node node : nodes) {
            while(j < NODE_LEN) {
                node.writeToken(builder, j++);
            }
            j = OVERLAP;
        }
        return builder.toString().trim();
    }

    private Token getRandomToken(Random rng)
    {
        int size = TOKENS.size();
        int index = rng.nextInt(size);
        Iterator<Token> itr = TOKENS.values().iterator();
        Token out = itr.next();
        for(int i = 0; i < index; i++)
            out = itr.next();
        return out;
    }
}
