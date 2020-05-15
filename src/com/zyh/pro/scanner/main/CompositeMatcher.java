package com.zyh.pro.scanner.main;

import java.util.ArrayList;
import java.util.List;

import static com.zyh.pro.scanner.main.ReturnMatcher.accepter;

public class CompositeMatcher<RETURN, CLUE> implements ReturnMatcher<RETURN, CLUE> {

	private final List<ReturnMatcher<RETURN, CLUE>> children;

	public CompositeMatcher() {
		children = new ArrayList<>();
	}

	@Override
	public boolean isMatch(CLUE clue) {
		return children.stream().anyMatch(child -> child.isMatch(clue));
	}

	@Override
	public RETURN onMatched(CLUE clue) {
		return children.stream()
				.filter(child -> child.isMatch(clue)).findFirst()
				.orElse(accepter(null)).onMatched(clue);
	}

	public CompositeMatcher<RETURN, CLUE> add(ReturnMatcher<RETURN, CLUE> child) {
		children.add(child);
		return this;
	}
}
