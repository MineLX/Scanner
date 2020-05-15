package com.zyh.pro.scanner.main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Sequence<ELEMENT> {

	private final IndexedIterator<ELEMENT> indexedIterator;

	public Sequence(IndexedIterator<ELEMENT> indexedIterator) {
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

	// FIXME 2020/4/30  wait for me!!!  remove it if must
	public List<ELEMENT> map(Function<List<ELEMENT>, ELEMENT> converter, Predicate<ELEMENT> terminatorPredicate) {
		List<ELEMENT> result = new ArrayList<>();
		consumeAll(new Combiner<>(terminatorPredicate, combination -> result.add(converter.apply(combination))));
		return result;
	}

	// FIXME 2020/5/2  wait for me!!!   end Operation | another responsibility
	public List<ELEMENT> til(ELEMENT endToken) {
		return tilIf(next -> Objects.equals(next, endToken));
	}

	public List<ELEMENT> tilIf(Predicate<ELEMENT> endTokenPredicate) {
		List<ELEMENT> result = new ArrayList<>();
		while (indexedIterator.hasNext()) {
			ELEMENT next = indexedIterator.next();
			if (endTokenPredicate.test(next))
				break;
			result.add(next);
		}
		return result;
	}

	public Sequence<ELEMENT> merge(ELEMENT endWith, BinaryOperator<ELEMENT> operator) {
		return new Sequence<>(new DecoratedIndexedIterator<ELEMENT>(indexedIterator) {
			@Override
			public ELEMENT next() {
				ELEMENT result = indexedIterator.next();
				while (indexedIterator.hasNext()) {
					ELEMENT addend = indexedIterator.next();
					result = operator.apply(result, addend);
					if (Objects.equals(addend, endWith))
						break;
				}
				return result;
			}
		});
	}

	public Sequence<ELEMENT> split(ELEMENT splitBy) {
		return new Sequence<>(new DecoratedIndexedIterator<ELEMENT>(indexedIterator) {
			@Override
			public ELEMENT next() {
				ELEMENT next = indexedIterator.next();
				if (!Objects.equals(next, splitBy))
					return next;
				return next(); // find an element which is not equals to $splitBy
			}
		});
	}

	public <R> Sequence<R> map(Function<ELEMENT, R> mapper) {
		return new Sequence<>(indexedIterator.map(mapper));
	}

	public interface IndexedConsumer<ELEMENT> {
		void onConsume(ELEMENT element, int startAt);

		default void onEnd() {
		}
	}
}
