package com.gikk;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

public class RandomAccessSetTest
{
	@Test
	public void testAdd() {
		RandomAccessSet<Integer> set = new RandomAccessSet<>();

		set.add(1);
		assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

		set.add(1);
		assertEquals("RandoAccessSet.size() missmatch", 1, set.size());

		set.add(2);
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

		Random rng = new TestRandom(1,0);
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
		public int nextInt()
		{
			int next = values[lastIndex++];
			if(next > values.length) lastIndex = 0;
			return next;
		}

		@Override
		public int nextInt(int bound)
		{
			int next;
			do{
				next = nextInt();
			} while (next > bound);
			return next;
		}
	}
}
