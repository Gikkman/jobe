package com.gikk.jobe2;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.gikk.RandomAccessSet;

public class JobeTest
{
	@Test
	public void test()
	{
		Set<String> results = new HashSet<>();

		Jobe jobe = new Jobe(3, 2);
		jobe.consume("this is gikk test");
		jobe.consume("this is a test");
		jobe.consume("this is another test");
		jobe.consume("another test yo");
		jobe.consume("another test would be good");
		jobe.consume("test test");

		for (int i = 0; i < 10000; i++)
		{
			try
			{
				results.add(jobe.produce("test"));
				results.add(jobe.produce("gikkman"));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		System.out.println("\nOutput:");
		results.forEach(System.out::println);

		System.out.println("\nNodes:");
		jobe.brain.nodeMap.values().stream()
			.sorted(Comparator.comparingInt(Node::getNodeID))
			.forEach(n -> System.out.println(n.toString()));

		System.out.println("\nTokens:");
		for (Map.Entry<Token, RandomAccessSet<Node>> e : jobe.brain.tokenNodeMap.entrySet())
		{
			String val = e.getValue().stream().map(Node::toString).collect(Collectors.joining(","));
			String key = String.format("Key: %-8s ", e.getKey());
			String value = "Val: " + val;
			System.out.println(key + value);
		}

		System.out.println("\nEdges LEFT:");
		for (Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.brain.leftwards.entrySet())
		{
			String val = e.getValue().stream()
				.sorted(Comparator.comparing(Edge::getLeftNode))
				.map(Edge::toString)
				.collect(Collectors.joining(","));
			String key = String.format("Key: %-4s ", e.getKey());
			String value = "Val: " + val;
			System.out.println(key + value);
		}

		System.out.println("\nEdges RIGHT:");
		for (Map.Entry<Node, RandomAccessSet<Edge>> e : jobe.brain.rightwards.entrySet())
		{
			String val = e.getValue().stream()
				.sorted(Comparator.comparing(Edge::getRightNode))
				.map(Edge::toString)
				.collect(Collectors.joining(","));
			String key = String.format("Key: %-4s ", e.getKey());
			String value = "Val: " + val;
			System.out.println(key + value);
		}

		Assert.assertTrue(true);
	}
}
