package com.zyh.pro.scanner.main;

import java.util.function.IntPredicate;

public interface IStringScanner extends IScanner<String> {

	void setIndex(int index);

	char nextChar();

	void pullBack(int count);

	String nextAlpha();

	String nextPage();

	boolean pass(String expected);

	boolean isEmpty();

	int hasCount();

	boolean exists(String expected);

	String nextChars(int count);

	void trim();

	String nextFloat();

	String peek(int count);

	IStringScanner copy();

	default String collect(IntPredicate predicate) {
		StringBuilder result = new StringBuilder();
		String next;
		while (hasNext()) {
			if (predicate.test((next = nextChars(1)).charAt(0))) {
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

	// FIXME 2020/6/2  wait for me!!! weird ... remove it if possible
	String between(char left, char right);

	default <R> Sequence<R> sequence(ToResult<R, ? super IStringScanner> matcher) {
		return new Sequence<>(new IndexedIterator<R>() {
			@Override
			public int getIndex() {
				return IStringScanner.this.getIndex();
			}

			@Override
			public R next() {
				return matcher.get(IStringScanner.this);
			}

			@Override
			public boolean hasNext() {
				if (!IStringScanner.this.hasNext())
					return false;
				int mark = IStringScanner.this.getIndex();
				R result = next();
				IStringScanner.this.setIndex(mark);
				return result != null;
			}
		});
	}
}
