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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class TestUtil {
    private final AtomicInteger NODE_ID = new AtomicInteger(1);
    private final Map<String, Token> TOKEN_MAP = new HashMap<>();
    private final Map<NodePrototype, Node> NODE_MAP = new HashMap<>();

    Token createToken(String word) {
        if (!TOKEN_MAP.containsKey(word)) {
            Token token = new Token(word);
            TOKEN_MAP.put(word, token);
        }
        return TOKEN_MAP.get(word);
    }

    NodePrototype createNodePrototype(String... words) {
        return new NodePrototype(Arrays.stream(words).map(this::createToken).toArray(Token[]::new));
    }

    Node createNode(String... words) {
        NodePrototype prototype = createNodePrototype(words);
        if (NODE_MAP.containsKey(prototype))
            return NODE_MAP.get(prototype);
        else {
            Node node = new Node(prototype, NODE_ID.getAndIncrement());
            NODE_MAP.put(prototype, node);
            return node;
        }
    }

    Chain createChain(int overlap, Node... nodes) {
        Deque<Node> deque = new ArrayDeque<>(Arrays.asList(nodes));
        return new Chain(deque, overlap);
    }
}
