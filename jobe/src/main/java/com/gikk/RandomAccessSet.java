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

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Thanks to fandrew: https://stackoverflow.com/a/5669034/1943559
 */
public class RandomAccessSet<E> extends AbstractSet<E> {
    private final List<E> data = new ArrayList<>();
    private final Map<E, Integer> idx = new HashMap<>();

    @Override
    public boolean add(E item) {
        if (idx.containsKey(item)) {
            return false;
        }
        idx.put(item, data.size());
        data.add(item);
        return true;
    }

    @Override
    public boolean remove(Object item) {
        Integer id = idx.get(item);
        if (id == null) {
            return false;
        }
        removeAt(id);
        return true;
    }

    E get(int i) {
        return data.get(i);
    }

    public E getRandom(Random rnd) {
        if (data.isEmpty()) {
            return null;
        }
        int id = rnd.nextInt(data.size());
        return get(id);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Removes the item at <code>index</code> and fills the gap with the last element in the collection (to avoid gaps)
     * 
     * @param index
     *            index of the element to remove
     */
    private void removeAt(int index) {
        if (index >= data.size()) {
            return;
        }
        E res = data.get(index);
        idx.remove(res);

        // Skip filling the hole if last is removed
        E last = data.remove(data.size() - 1);
        if (index < data.size()) {
            idx.put(last, index);
            data.set(index, last);
        }
    }

    private class Itr implements Iterator<E> {
        int cursor = 0; // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        @Override
        public boolean hasNext() {
            return cursor != RandomAccessSet.this.data.size();
        }

        @Override
        public E next() {
            int i = cursor;
            if (i >= RandomAccessSet.this.data.size()) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return RandomAccessSet.this.data.get(lastRet = i);
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException("next() has to be called before remove()");
            }
            try {
                E elem = RandomAccessSet.this.data.remove(lastRet);
                RandomAccessSet.this.idx.remove(elem);
                cursor = lastRet;
                lastRet = -1;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
