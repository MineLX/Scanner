package com.zyh.pro.scanner.main;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositeToResult<RETURN, CLUE> implements ToResult<RETURN, CLUE> {

	private final List<ToResult<RETURN, CLUE>> children;

	public CompositeToResult() {
		children = new ArrayList<>();
	}

	@Override
	public RETURN get(CLUE clue) {
		for (ToResult<RETURN, CLUE> child : children) {
			RETURN result = child.get(clue);
			if (result != null)
				return result;
		}
		return null;
	}

	public CompositeToResult<RETURN, CLUE> add(ToResult<RETURN, CLUE> child) {
		children.add(Objects.requireNonNull(child));
		return this;
	}

	public CompositeToResult<RETURN, CLUE> add(ReturnMatcher<RETURN, CLUE> child) {
		return add(new MatcherToResult<>(child));
	}
}
