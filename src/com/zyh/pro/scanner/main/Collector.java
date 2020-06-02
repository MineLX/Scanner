package com.zyh.pro.scanner.main;

import java.util.LinkedList;
import java.util.List;

public interface Collector<E, R> extends Sequence.IndexedConsumer<E> {

	R toResult();

	static <E> Collector<E, List<E>> toList() {
		return new Collector<E, List<E>>() {
			private final List<E> result = new LinkedList<>();

			@Override
			public List<E> toResult() {
				return result;
			}

			@Override
			public void onConsume(E e, int startAt) {
				result.add(e);
			}
		};
	}
}
