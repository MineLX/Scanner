package com.zyh.pro.scanner.main;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Matcher<CLUE> extends Match<CLUE> {

	void onMatched(CLUE clue);

	static <CLUE> Matcher<CLUE> functional(Predicate<CLUE> isMatchedPredicate, Consumer<CLUE> onMatchedConsumer) {
		return new Matcher<CLUE>() {
			@Override
			public void onMatched(CLUE clue) {
				onMatchedConsumer.accept(clue);
			}

			@Override
			public boolean isMatch(CLUE clue) {
				return isMatchedPredicate.test(clue);
			}
		};
	}
}
