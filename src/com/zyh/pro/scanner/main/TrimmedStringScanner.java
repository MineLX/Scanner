package com.zyh.pro.scanner.main;

import java.util.function.IntPredicate;

public class TrimmedStringScanner implements IStringScanner {

	private final IStringScanner decorated;

	public TrimmedStringScanner(IStringScanner decorated) {
		this.decorated = decorated;
	}

	@Override
	public void setIndex(int index) {
		decorated.setIndex(index);
	}

	@Override
	public char nextChar() {
		decorated.trim();
		return decorated.nextChar();
	}

	@Override
	public String nextAlpha() {
		decorated.trim();
		return decorated.nextAlpha();
	}

	@Override
	public String nextPage() {
		decorated.trim();
		return decorated.nextPage();
	}

	@Override
	public String nextChars(int count) {
		decorated.trim();
		return decorated.nextChars(count);
	}

	@Override
	public boolean pass(String expected) {
		decorated.trim();
		return decorated.pass(expected);
	}

	@Override
	public boolean exists(String expected) {
		decorated.trim();
		return decorated.exists(expected);
	}

	@Override
	public boolean existsIf(IntPredicate predicate) {
		decorated.trim();
		return decorated.existsIf(predicate);
	}

	@Override
	public String between(char left, char right) {
		decorated.trim();
		return decorated.between(left, right);
	}

	@Override
	public boolean hasNext() {
		decorated.trim();
		return decorated.hasNext();
	}

	@Override
	public boolean isEmpty() {
		decorated.trim();
		return decorated.isEmpty();
	}

	@Override
	public String nextFloat() {
		decorated.trim();
		return decorated.nextFloat();
	}

	@Override
	public void pullBack(int count) {
		decorated.pullBack(count);
	}

	@Override
	public void pullBack(String item) {
		decorated.pullBack(item);
	}

	@Override
	public int hasCount() {
		return decorated.hasCount();
	}

	@Override
	public void trim() {
		decorated.trim();
	}

	@Override
	public int getIndex() {
		return decorated.getIndex();
	}

	@Override
	public String peek(int count) {
		return decorated.peek(count);
	}

	@Override
	public IStringScanner copy() {
		return new TrimmedStringScanner(decorated.copy());
	}

	@Override
	public String toString() {
		return decorated.toString();
	}

	@Override
	public String collect(IntPredicate predicate) {
		return decorated.collect(predicate);
	}
}
