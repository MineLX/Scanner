package com.zyh.pro.scanner.main;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

// FIXME 2020/4/29  wait for me!!!  turn to ITree
public class CollectTree<RETURN, CLUE> {

	private final ReturnMatcher<RETURN, CLUE> matcher;

	private final List<CollectTree<RETURN, CLUE>> children;

	public CollectTree(ReturnMatcher<RETURN, CLUE> matcher) {
		this.matcher = matcher;
		children = new ArrayList<>();
	}

	public void addChild(CollectTree<RETURN, CLUE> child) {
		children.add(child);
	}

	public void addChild(ReturnMatcher<RETURN, CLUE> matcher) {
		addChild(new CollectTree<>(matcher));
	}

	private RETURN recursiveGet(CLUE clue, BinaryOperator<RETURN> converter) {
		if (!matcher.isMatch(clue))
			return null;

		RETURN selfReturn = matcher.onMatched(clue);

		for (CollectTree<RETURN, CLUE> child : children) {
			RETURN childReturn = child.recursiveGet(clue, converter);
			if (childReturn != null) {
				return converter.apply(selfReturn, childReturn);
			}
		}
		return selfReturn;
	}

	public static <RETURN, CLUE> Root<RETURN, CLUE> root(BinaryOperator<RETURN> converter) {
		return new Root<>(converter);
	}

	public static class Root<RETURN, CLUE> implements ToResult<RETURN, CLUE> {

		private final List<CollectTree<RETURN, CLUE>> children;

		private final BinaryOperator<RETURN> converter;

		Root(BinaryOperator<RETURN> converter) {
			this.converter = converter;
			children = new ArrayList<>();
		}

		public CollectTree<RETURN, CLUE> addChild(CollectTree<RETURN, CLUE> child) {
			children.add(child);
			return child;
		}

		public CollectTree<RETURN, CLUE> addChild(ReturnMatcher<RETURN, CLUE> matcher) {
			return addChild(new CollectTree<>(matcher));
		}

		@Override
		public RETURN get(CLUE clue) {
			for (CollectTree<RETURN, CLUE> child : children) {
				RETURN childReturn = child.recursiveGet(clue, converter);
				if (childReturn != null) {
					return childReturn;
				}
			}
			return null;
		}
	}
}
