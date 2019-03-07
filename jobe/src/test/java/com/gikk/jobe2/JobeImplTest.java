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

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import org.junit.Test;

public class JobeImplTest {
    @Test
    public void testConsumeThenProduce() {
        JobeImpl jobe = new JobeImpl(3, 2);
        String output = jobe.consumeThenProduce("this is a test");
        assertFalse("Jobe produced nothing, after consuming", output.isEmpty());
    }

    @Test
    public void testConsumeThenProduceManually() {
        JobeImpl jobe = new JobeImpl(3, 2);
        jobe.consume("this is a test");
        String output = jobe.produce("test");
        assertFalse("Jobe produced nothing, when getting known input", output.isEmpty());
    }

    @Test
    public void testConsumeThenProduceFromUnknown() {
        JobeImpl jobe = new JobeImpl(3, 2);
        jobe.consume("this is a test");
        String output = jobe.produce("jobe");
        assertFalse("Jobe produced nothing, when getting unknown input", output.isEmpty());
    }

    @Test
    public void testProduceWhenNothingLearned() {
        JobeImpl jobe = new JobeImpl(3, 2);
        String output = jobe.produce("jobe");
        assertTrue("Jobe produced something when nothing was learned", output.isEmpty());
    }
}
