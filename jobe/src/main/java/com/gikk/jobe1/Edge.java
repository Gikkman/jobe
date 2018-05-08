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

/**
 *
 * @author Gikkman
 */
class Edge {
    private int leftNodeID = -1;
    private int rightNodeID = -1;

    void setLeftNodeID(int id) {
        this.leftNodeID = id;
    }

    void setRightNodeID(int id) {
        this.rightNodeID = id;
    }

    boolean isLeftmost() {
        return this.leftNodeID < 0;
    }

    boolean isRightmost() {
        return this.rightNodeID < 0;
    }

    int getRightNodeID() {
        return rightNodeID;
    }

    int getLeftNodeID() {
        return leftNodeID;
    }

    @Override
    public String toString() {
        return "[" + leftNodeID + "->" + rightNodeID + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.leftNodeID;
        hash = 59 * hash + this.rightNodeID;
        return hash;
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
        if (this.leftNodeID != other.leftNodeID) {
            return false;
        }
        if (this.rightNodeID != other.rightNodeID) {
            return false;
        }
        return true;
    }
}
