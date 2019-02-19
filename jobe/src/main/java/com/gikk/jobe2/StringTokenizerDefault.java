package com.gikk.jobe2;

public class StringTokenizerDefault implements StringTokenizer
{
	@Override
	public String[] tokenize(String string)
	{
		return string.split("\\s+");
	}
}
