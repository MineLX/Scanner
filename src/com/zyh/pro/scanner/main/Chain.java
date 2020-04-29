package com.zyh.pro.scanner.main;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Chain<E> implements Iterable<E> {

	private final List<E> store;

	public Chain() {
		store = new LinkedList<>();
	}

	public Chain<E> next(E element) {
		store.add(element);
		return this;
	}

	@Override
	public Iterator<E> iterator() {
		return store.iterator();
	}
}
