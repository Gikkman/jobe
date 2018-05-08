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
package com.gikk.jobe1;

import com.gikk.RandomAccessSet;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Gikkman
 */
public class Jobe1 {
    static final String START_OF_CHAIN = " ";
    static final String END_OF_CHAIN = " ";
    private static final int NODE_LEN = 1;
    private static final int OVERLAP = 0;

    private final Map<String, RandomAccessSet<Node>> TOKEN_TO_NODES = new HashMap<>();

    private final Map<Node, Integer> NODE_TO_NODEID = new HashMap<>();
    private final Map<Integer, Node> NODEID_TO_NODE = new HashMap<>();

    private final Map<Integer, RandomAccessSet<Edge>> NODEID_TO_EDGES = new HashMap<>();

    private final Map<Edge, RandomAccessSet<Edge>> RIGHTWARDS = new HashMap<>();
    private final Map<Edge, RandomAccessSet<Edge>> LEFTWARDS = new HashMap<>();

    public static void main(String ... args) throws UnsupportedEncodingException {
        Set<String> results = new HashSet<>();

        Jobe1 jobe = new Jobe1();
        jobe.consume("this is a test");
        jobe.consume("this is another test");
        jobe.consume("this is another test yo");
        jobe.consume("another test would be good");
        jobe.consume("test test");

        for(int i = 0; i < 10000; i++) {
            try{
                results.add(jobe.produce("test"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        results.stream().forEach(System.out::println);
    }

    public synchronized void consume(String s) {
        if( NODE_LEN <= OVERLAP ) return;
        if( NODE_LEN < 1 ) return;

        // Split incomming message into tokens
        String[] tokens = s.split("\\s+");
        int tokensLenght = tokens.length;

        if(tokensLenght <= NODE_LEN) return;

        // Array for representing node segments
        String[] nextNode = new String[NODE_LEN];
        nextNode[0] = START_OF_CHAIN;

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

        if(NODE_LEN == 1) {
            nextTokenPos = 0;
            Node node = registerNode(nextNode);
            nodes.add(node);
        } else {
            nextTokenPos = 1;
        }

        while(tokensIndex < tokensLenght){
            String token = tokens[tokensIndex++];
            if(token.isEmpty()) continue;

            nextNode[nextTokenPos++] = token;
            if(nextTokenPos == NODE_LEN) {
                // Create a node and register it for a unique ID
                Node node = registerNode(nextNode);
                nodes.add(node);

                 //Shift array left LINK_LEN-OVERLAP steps
                System.arraycopy(nextNode, NODE_LEN-OVERLAP, nextNode, 0, OVERLAP);
                nextTokenPos = OVERLAP;
            }
        }

        nextNode[nextTokenPos++] = END_OF_CHAIN;
        Node node = registerNode(nextTokenPos, nextNode);
        nodes.add(node);

        // Map edge relations
        int previousNodeID = -1;
        int currentNodeID = nodes.get(0).getNodeID();
        int nextNodeID = -1;
        for(int i = 1; i <= nodes.size(); i++) {
            nextNodeID = i == nodes.size() ? -1 : nodes.get(i).getNodeID();

            // Create edges, and assign which nodes they point to
            Edge leftEdge = new Edge();
            leftEdge.setLeftNodeID(previousNodeID);
            leftEdge.setRightNodeID(currentNodeID);

            Edge rightEdge = new Edge();
            rightEdge.setLeftNodeID(currentNodeID);
            rightEdge.setRightNodeID(nextNodeID);

            // Register edge relations
            RIGHTWARDS.computeIfAbsent(
                    leftEdge,
                    e -> new RandomAccessSet<>())
                .add(rightEdge);
            LEFTWARDS.computeIfAbsent(
                    rightEdge,
                    e -> new RandomAccessSet<>())
                .add(leftEdge);

            Set<Edge> edgeMap = NODEID_TO_EDGES.computeIfAbsent(
                currentNodeID,
                n -> new RandomAccessSet<>());
            edgeMap.add(leftEdge);
            edgeMap.add(rightEdge);

            // Move the "pointer" forward
            previousNodeID = currentNodeID;
            currentNodeID = nextNodeID;
        }
    }

    private synchronized Node registerNode(String[] tokens){
        return registerNode(tokens.length, tokens);
    }

    private synchronized Node registerNode(int len, String[] tokens) {
        Node node = new Node(len, tokens);
        int id = NODE_TO_NODEID.computeIfAbsent(
            node,
            (t) -> {
                for(String token : tokens) {
                    TOKEN_TO_NODES.computeIfAbsent(
                            token,
                            (tt) -> new RandomAccessSet<>())
                        .add(node);
                }

                int generatedID = NODE_TO_NODEID.size();
                node.setID(generatedID);
                NODEID_TO_NODE.put(generatedID, node);

                return generatedID;
            }
        );
        return NODEID_TO_NODE.get(id);
    }

    public String produce(String i) {
        Random rng = new Random();
        Node n = TOKEN_TO_NODES.get(i).getRandom(rng);
        if(n == null) return "";

        Edge e = NODEID_TO_EDGES.get(n.getNodeID()).getRandom(rng);
        if(e == null) return "";

        Edge originEdge = e;
        Deque<Node> nodes = new ArrayDeque<>();

        Edge left = originEdge;
        while(!left.isLeftmost()) {
            Node node = NODEID_TO_NODE.get( left.getLeftNodeID() );
            nodes.addFirst(node);
            RandomAccessSet<Edge> ras = LEFTWARDS.get(left);
            left = ras.getRandom(rng);
        }

        Edge right = originEdge;
        while(!right.isRightmost()) {
            Node node = NODEID_TO_NODE.get( right.getRightNodeID());
            nodes.addLast(node);
            RandomAccessSet<Edge> ras = RIGHTWARDS.get(right);
            right = ras.getRandom(rng);
        }

        // Print the NODES
//        PrintStream printStream = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());
        StringBuilder builder = new StringBuilder();
        int j = 0;
        for(Node node : nodes) {
            while(j < NODE_LEN) {
                node.writeToken(builder, j++);
            }
            j = OVERLAP;
        }
//        printStream.close();
        return builder.toString();
    }
}
