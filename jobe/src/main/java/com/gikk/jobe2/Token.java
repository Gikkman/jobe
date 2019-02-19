package com.gikk.jobe2;

import java.util.Objects;

class Token
{
	private final String string;
	private int points = 0;
	private int hashCache = Integer.MIN_VALUE;

	Token(String string) {
		this.string = string;
	}

	void addPoint() {
		this.points++;
	}

	int getPoints() {
		return this.points;
	}

	public String getString() {
		return this.string;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Token token = (Token) o;
		return Objects.equals(this.string, token.string);
	}

	@Override
	public int hashCode()
	{
		if(this.hashCache != Integer.MIN_VALUE) return this.hashCache;
		this.hashCache = Objects.hash(this.string);
		return this.hashCache;
	}

	@Override
	public String toString()
	{
		return string;
	}
}
