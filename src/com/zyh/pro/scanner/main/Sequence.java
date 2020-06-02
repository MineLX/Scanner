package com.zyh.pro.scanner.main;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Sequence<ELEMENT> {

	private final IndexedIterator<ELEMENT> indexedIterator;

	Sequence(IndexedIterator<ELEMENT> indexedIterator) {
		this.indexedIterator = indexedIterator;
	}

	public void consumeAll(IndexedConsumer<ELEMENT> consumer) {
		while (indexedIterator.hasNext()) {
			int index = indexedIterator.getIndex();
			consumer.onConsume(indexedIterator.next(), index);
		}
		consumer.onEnd();
	}

	public List<ELEMENT> toList() {
		List<ELEMENT> result = new LinkedList<>();
		consumeAll((element, startAt) -> result.add(element));
		return result;
	}

	public Sequence<ELEMENT> split(ELEMENT splitBy) {
		return new Sequence<>(new DecoratedIndexedIterator<ELEMENT>(indexedIterator) {
			@Override
			public ELEMENT next() {
				ELEMENT next = indexedIterator.next();
				if (!Objects.equals(next, splitBy))
					return next;
				return next(); // find an element which is not equals to splitBy
			}
		});
	}

	public <R> Sequence<R> map(Function<ELEMENT, R> mapper) {
		return new Sequence<>(indexedIterator.map(mapper));
	}

	public <R> R collect(Collector<ELEMENT, R> collector) {
		consumeAll(collector);
		return collector.toResult();
	}

	@SafeVarargs
	public static <T> Sequence<T> of(T... values) {
		return new Sequence<>(new ArrayIndexedIterator<>(values));
	}

	public interface IndexedConsumer<ELEMENT> {
		void onConsume(ELEMENT element, int startAt);

		default void onEnd() {
		}
	}
}
