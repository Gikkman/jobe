package com.gikk.jobe2;

import java.util.Collection;
import java.util.List;

public interface Selector
{
	Token selectPivot(Collection<Token> tokens);
	Chain selectChain(Scorer scorer, Collection<Chain> alternatives);
}
