package com.zyh.pro.scanner.main;

public class MatcherToResult<RETURN, CLUE> implements ToResult<RETURN, CLUE> {

	private final ReturnMatcher<RETURN, CLUE> matcher;

	public MatcherToResult(ReturnMatcher<RETURN, CLUE> matcher) {
		this.matcher = matcher;
	}

	@Override
	public RETURN get(CLUE clue) {
		if (matcher.isMatch(clue))
			return matcher.onMatched(clue);
		return null;
	}
}
