package com.zyh.pro.scanner.main;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FunctionalCombiner extends Combiner {
	private final Consumer<List<String>> combinationConsumer;
	private final Predicate<String> terminatorPredicate;

	public FunctionalCombiner(Consumer<List<String>> combinationConsumer, Predicate<String> terminatorPredicate) {
		this.combinationConsumer = combinationConsumer;
		this.terminatorPredicate = terminatorPredicate;
	}

	@Override
	protected void onCombined(List<String> combination) {
		combinationConsumer.accept(combination);
	}

	@Override
	protected boolean isTerminator(String token) {
		return terminatorPredicate.test(token);
	}
}
