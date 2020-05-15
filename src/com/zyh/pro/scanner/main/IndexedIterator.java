package com.zyh.pro.scanner.main;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public interface IndexedIterator<E> extends Iterator<E> {
	int getIndex();

	default <R> IndexedIterator<R> map(Function<E, R> towardFunction) {
		return new IndexedIterator<R>() {
			@Override
			public boolean hasNext() {
				return IndexedIterator.this.hasNext();
			}

			@Override
			public R next() {
				E result = IndexedIterator.this.next();
				if (result == null)
					return null;
				return towardFunction.apply(result);
			}

			@Override
			public int getIndex() {
				return IndexedIterator.this.getIndex();
			}
		};
	}
}
