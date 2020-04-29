package com.zyh.pro.scanner.main;

import java.util.function.IntPredicate;

public interface IScanner {

	char nextChar();

	void pullBack(int count);

	boolean hasNext();

	String nextAlpha();

	String nextPage();

	boolean pass(String expected);

	boolean isEmpty();

	int hasCount();

	boolean exists(String expected);

	String nextChars(int count);

	void trim();

	String nextFloat();

	int getIndex();

	String peek(int count);

	default String collect(IntPredicate predicate) {
		StringBuilder result = new StringBuilder();
		char next;
		while (hasNext()) {
			if (predicate.test(next = nextChar())) {
				result.append(next);
				continue;
			}
			pullBack(1);
			return result.toString();
		}
		return result.toString();
	}

	default String til(char endChar) {
		return collect(value -> value != endChar);
	}

	boolean existsIf(IntPredicate predicate);

	String between(char left, char right); // FIXME 2020/4/27  wait for me!!!  support String
}
