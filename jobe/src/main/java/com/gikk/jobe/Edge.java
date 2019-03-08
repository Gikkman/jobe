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

import java.util.Objects;

class Edge {
    private final Node leftNode;
    private final Node rightNode;

    Edge(Node left, Node right) {
        this.leftNode = left;
        this.rightNode = right;
    }

    boolean isLeftmost() {
        return this.leftNode.equals(Node.EMPTY);
    }

    boolean isRightmost() {
        return this.rightNode.equals(Node.EMPTY);
    }

    Node getRightNode() {
        return rightNode;
    }

    Node getLeftNode() {
        return leftNode;
    }

    @Override
    public String toString() {
        return "[" + leftNode.getNodeID() + "->" + rightNode.getNodeID() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.leftNode, other.leftNode)) {
            return false;
        }
        if (!Objects.equals(this.rightNode, other.rightNode)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.leftNode);
        hash = 97 * hash + Objects.hashCode(this.rightNode);
        return hash;
    }
}
