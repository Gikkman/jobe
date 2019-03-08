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
package com.gikk;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class RandomAccessSetTest {
    @Test
    public void testAdd() {
        RandomAccessSet<Integer> set = new RandomAccessSet<>();

        assertTrue("Adding unique returned wrong status", set.add(1));
        assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

        assertFalse("Adding duplicate returned wrong status", set.add(1));
        assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

        assertTrue("Adding unique returned wrong status", set.add(2));
        assertEquals("RandoAccessSet.size() missmatch", 2, set.size());
    }

    @Test
    public void testGet() {
        RandomAccessSet<Integer> set = new RandomAccessSet<>();
        set.add(1);
        set.add(2);
        assertEquals("RandomAccessSet.get(index) missmatch", new Integer(1), set.get(0));
        assertEquals("RandomAccessSet.get(index) missmatch", new Integer(2), set.get(1));
    }

    @Test
    public void testGetRandom() {
        RandomAccessSet<Integer> set = new RandomAccessSet<>();
        set.add(1);
        set.add(2);

        Random rng = new TestRandom(1, 0);
        assertEquals("RandomAccessSet.getRandom(rng) missmatch", new Integer(2), set.getRandom(rng));
        assertEquals("RandomAccessSet.getRandom(rng) missmatch", new Integer(1), set.getRandom(rng));

    }

    private class TestRandom extends Random {
        final int[] values;
        int lastIndex = 0;

        TestRandom(int... values) {
            this.values = values;
        }

        @Override
        public int nextInt() {
            int next = values[lastIndex++];
            if (next > values.length)
                lastIndex = 0;
            return next;
        }

        @Override
        public int nextInt(int bound) {
            int next;
            do {
                next = nextInt();
            }
            while (next > bound);
            return next;
        }
    }
}
