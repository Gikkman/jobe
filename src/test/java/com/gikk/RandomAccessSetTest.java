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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class RandomAccessSetTest {
    @Test
    public void testAdd() {
        RandomAccessSet<String> set = new RandomAccessSet<>();

        assertTrue("Adding unique returned wrong status", set.add("hello"));
        assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

        assertFalse("Adding duplicate returned wrong status", set.add("hello"));
        assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

        assertTrue("Adding unique returned wrong status", set.add("world"));
        assertEquals("RandoAccessSet.size() missmatch", 2, set.size());
    }

    @Test
    public void testRemove() {
        String s1 = "hello", s2 = "world";
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add(s1);
        set.add(s2);
        assertTrue("Removed existing element, but got false", set.remove(s1));
        assertFalse("Removed non-existing element, but got true", set.remove("ipsum"));
        assertTrue("Removed existing element, but got false", set.remove(s2));
    }

    @Test
    public void testRemoveAt() {
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add("hello");
        set.add("world");
        set.add("ipsum");

        // First element should be "hello", since it was the first inserted
        assertEquals("hello", set.get(0));
        set.removeAt(0);

        // After using removeAt(), the gap from the removed element is filled by the last element in the collection
        // (i.e. "lore")
        assertEquals("ipsum", set.get(0));

        // Test that removing an element beyond the set's size does nothing
        set.removeAt(2);
    }

    @Test(
        expected = NoSuchElementException.class)
    public void testIterator() {
        String s1 = "lore", s2 = "ipsum";
        Set<String> allString = Stream.of("lore", "ipsum").collect(Collectors.toSet());
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add(s1);
        set.add(s2);
        Iterator<String> itr = set.iterator();

        assertTrue("itr with element returned false on hasNext", itr.hasNext());
        assertTrue("element that should exist in set, didn't", allString.remove(itr.next()));

        int size = set.size();
        itr.remove();
        assertEquals("size did not shrink after itr.remove()", size - 1, set.size());

        assertTrue("itr didn't have a next, though it should", itr.hasNext());
        assertTrue("element that should exist in set, didn't", allString.remove(itr.next()));

        assertFalse("itr shouldn't have next", itr.hasNext());
        itr.next();
    }

    @Test
    public void testIteratorRemove() {
        String s1 = "hello", s2 = "small";
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add(s1);
        set.add(s2);
        Iterator<String> itr = set.iterator();

        // You gotta call itr.next before remove
        try {
            itr.remove();
            Assert.fail("remove() without calling next() should throw exception");
        }
        catch (Exception e) {
            assertTrue("Wrong exception type", e instanceof IllegalStateException);
        }

        // Calling next should allow us to remove something
        assertEquals("wrong size though nothing should have been removed", 2, set.size());
        itr.next();
        itr.remove();
        assertEquals("wrong size since something should have been removed", 1, set.size());

        // You gotta call itr.next() before each remove, so having done it once above should not matter bellow
        try {
            itr.remove();
            Assert.fail("remove() without calling next() should throw exception");
        }
        catch (Exception e) {
            assertTrue("Wrong exception type", e instanceof IllegalStateException);
        }

        itr.next();
        itr.remove();
        assertEquals("set should be empty", 0, set.size());
    }

    @Test
    public void testGet() {
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add("hello");
        set.add("world");
        assertEquals("RandomAccessSet.get(index) missmatch", "hello", set.get(0));
        assertEquals("RandomAccessSet.get(index) missmatch", "world", set.get(1));
    }

    @Test
    public void testGetRandom() {
        RandomAccessSet<String> set = new RandomAccessSet<>();
        set.add("hello");
        set.add("world");

        Random rng = new TestRandom(1, 0);
        assertEquals("RandomAccessSet.getRandom(rng) missmatch", "world", set.getRandom(rng));
        assertEquals("RandomAccessSet.getRandom(rng) missmatch", "hello", set.getRandom(rng));
    }

    @Test
    public void testGetRandomIfEmpty() {
        RandomAccessSet<String> set = new RandomAccessSet<>();
        Random rng = new TestRandom(1, 0);
        assertNull("RandomAccessSet.getRandom(rng) should give null if empty", set.getRandom(rng));
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
