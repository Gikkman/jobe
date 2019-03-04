package com.gikk.jobe2;

public interface StringSplitter
{
	String[] split(String string);

	static StringSplitter getDefault() {
		return new StringSplitterDefault();
	}
}
