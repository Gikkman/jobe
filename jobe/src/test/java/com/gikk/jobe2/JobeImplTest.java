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

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.gikk.RandomAccessSet;

public class JobeImplTest {
    @Test
    @Ignore
    public void testSync() {
        Set<String> results = new HashSet<>();

        JobeImpl jobe = new JobeImpl(3, 2);
        jobe.consume("this is gikk test");
        jobe.consume("this is a test");
        jobe.consume("this is another test");
        jobe.consume("another test yo");
        jobe.consume("another test would be good");
        jobe.consume("test test");

        for (int i = 0; i < 10000; i++) {
            try {
                results.add(jobe.produce("test me"));
                results.add(jobe.produce("gikkman"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("\nOutput:");
        results.forEach(System.out::println);

        System.out.println("\nNodes:");
        jobe.brain.nodeMap.values().stream().sorted(Comparator.comparingInt(Node::getNodeID))
                .forEach(n -> System.out.println(n.toString()));

        System.out.println("\nTokens:");
        for (Map.Entry<Token, RandomAccessSet<Node>> e : jobe.brain.tokenNodeMap.entrySet()) {
            String val = e.getValue().stream().map(Node::toString).collect(Collectors.joining(","));
            String key = String.format("Key: %-8s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);
        }

        System.out.println("\nEdges LEFT:");
        for (Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.brain.leftwards.entrySet()) {
            String val = e.getValue().stream().sorted(Comparator.comparing(Edge::getLeftNode)).map(Edge::toString)
                    .collect(Collectors.joining(","));
            String key = String.format("Key: %-4s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);
        }

        System.out.println("\nEdges RIGHT:");
        for (Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.brain.rightwards.entrySet()) {
            String val = e.getValue().stream().sorted(Comparator.comparing(Edge::getRightNode)).map(Edge::toString)
                    .collect(Collectors.joining(","));
            String key = String.format("Key: %-4s ", e.getKey());
            String value = "Val: " + val;
            System.out.println(key + value);
        }

        Assert.assertTrue(true);
    }
}
