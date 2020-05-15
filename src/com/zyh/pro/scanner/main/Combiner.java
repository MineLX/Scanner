package com.zyh.pro.scanner.main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Combiner<E> implements Sequence.IndexedConsumer<E> {

	private final List<E> combination;

	private final Predicate<E> terminatorPredicate;

	private final Consumer<List<E>> combinationConsumer;

	public Combiner(Predicate<E> terminatorPredicate, Consumer<List<E>> combinationConsumer) {
		this.terminatorPredicate = terminatorPredicate;
		this.combinationConsumer = combinationConsumer;
		combination = new LinkedList<>();
	}

	@Override
	public void onConsume(E element, int startAt) {
		combination.add(element);
		if (terminatorPredicate.test(element))
			combine();
	}

	@Override
	public void onEnd() {
		if (combination.size() != 0)
			combine();
	}

	private void combine() {
		combinationConsumer.accept(Collections.unmodifiableList(combination));
		combination.clear();
	}
}
