package com.zyh.pro.scanner.main;

import java.util.function.IntPredicate;

import static java.util.Arrays.copyOfRange;

public class StringScanner implements IStringScanner {

	private final char[] source;

	private int index;

	public StringScanner(String source) {
		this.source = source.toCharArray();
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public char nextChar() {
		throwIfEnd();
		return source[index++];
	}

	@Override
	public void pullBack(int count) {
		index -= count;
	}

	@Override
	public void pullBack(String item) {
		pullBack(item.length());
	}

	@Override
	public boolean hasNext() {
		return index != source.length;
	}

	@Override
	public String nextAlpha() {
		return collect(Character::isAlphabetic);
	}

	@Override
	public String nextPage() {
		return collect(value -> Character.isAlphabetic(value) || Character.isDigit(value));
	}

	@Override
	public boolean pass(String expected) {
		boolean result = exists(expected);
		if (result) nextChars(expected.length());
		return result;
	}

	@Override
	public boolean isEmpty() {
		return index == source.length;
	}

	@Override
	public String toString() {
		return new String(source).substring(index);
	}

	@Override
	public int hasCount() {
		return source.length - index;
	}

	@Override
	public boolean exists(String expected) {
		if (isEmpty() || expected.length() > hasCount())
			return false;

		char[] chars = copyOfRange(source, index,
				index + expected.length());
		return new String(chars).equals(expected);
	}

	@Override
	public String nextChars(int count) {
		return new String(copyOfRange(source, index, index += count));
	}

	@Override
	public void trim() {
		collect(item -> item == ' ');
	}

	@Override
	public String nextFloat() {
		return collect(item -> Character.isDigit(item) || item == '.' || item == '-');
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public String peek(int count) {
		return getRemainder().substring(0, count);
	}

	@Override
	public StringScanner copy() {
		StringScanner result = new StringScanner(new String(source));
		result.index = index;
		return result;
	}

	@Override
	public boolean existsIf(IntPredicate predicate) {
		if (isEmpty())
			return false;
		return predicate.test(source[index]);
	}

	@Override
	public String between(char left, char right) {
		if (nextChar() != left)
			return "";

		StringBuilder builder = new StringBuilder();

		int sumLeft = 0;
		while (hasNext()) {
			char next = nextChar();
			if (next == left)
				sumLeft++;

			if (next == right) {
				sumLeft--;
				if (sumLeft == -1) break;
			}
			builder.append(next);
		}
		return builder.toString();
	}

	private String getRemainder() {
		return new String(source).substring(index, source.length);
	}

	private void throwIfEnd() {
		if (!hasNext())
			throw new IllegalStateException("scanner source is end.");
	}
}
