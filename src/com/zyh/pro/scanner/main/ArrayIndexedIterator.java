package com.zyh.pro.scanner.main;

public class ArrayIndexedIterator<E> implements IndexedIterator<E> {

	private final E[] array;

	private int index;

	public ArrayIndexedIterator(E[] array) {
		this.array = array;
		index = 0;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public boolean hasNext() {
		return index < array.length;
	}

	@Override
	public E next() {
		return array[index++];
	}
}
