package com.gikk.jobe2;

public class StringSplitterDefault implements StringSplitter
{
	@Override
	public String[] split(String string)
	{
		return string.split("\\s");
	}
}
