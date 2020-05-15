package com.zyh.pro.scanner.main;

public class DecoratedIndexedIterator<E> implements IndexedIterator<E> {
	private final IndexedIterator<E> decorated;

	public DecoratedIndexedIterator(IndexedIterator<E> decorated) {
		this.decorated = decorated;
	}
	
	@Override
	public int getIndex() {
		return decorated.getIndex();
	}

	@Override
	public boolean hasNext() {
		return decorated.hasNext();
	}

	@Override
	public E next() {
		return decorated.next();
	}
}
