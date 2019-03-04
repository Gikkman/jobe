package com.gikk.jobe2;

import java.util.ArrayDeque;
import java.util.Deque;

class Chain
{
	private final Deque<Node> nodes;
	private final int overlap;

	Chain(Node origin, int overlap)
	{
		this.nodes = new ArrayDeque<>();
		this.nodes.add(origin);
		this.overlap = overlap;
	}

	void addFirst(Node node) {
		this.nodes.addFirst(node);
	}

	void addLast(Node node) {
		this.nodes.addLast(node);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		int startIndex = 0;
		for(Node node : nodes) {
			node.writeTokens(builder, startIndex);
			startIndex = overlap;
		}
		return builder.toString().trim();
	}
}
