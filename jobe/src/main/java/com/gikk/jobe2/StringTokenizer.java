package com.gikk.jobe2;

interface StringTokenizer
{
	String[] tokenize(String string);

	default StringTokenizer getDefault() {
		return new StringTokenizerDefault();
	}
}
