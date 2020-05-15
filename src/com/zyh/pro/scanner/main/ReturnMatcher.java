package com.zyh.pro.scanner.main;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ReturnMatcher<RETURN, CLUE> extends Match<CLUE> {

	RETURN onMatched(CLUE clue);

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> accepter(RETURN returnValue) {
		return functional(clue -> true, clue -> returnValue);
	}

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> whatIf(Predicate<CLUE> isConsumablePredicate,
	                                                         Function<CLUE, RETURN> onConsumeFunction) {
		return new ReturnMatcher<RETURN, CLUE>() {
			@Override
			public boolean isMatch(CLUE clue) {
				return isConsumablePredicate.test(clue);
			}

			@Override
			public RETURN onMatched(CLUE clue) {
				if (isMatch(clue))
					return onConsumeFunction.apply(clue);
				return null;
			}
		};
	}

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> functional(Predicate<CLUE> isConsumablePredicate,
	                                                             Function<CLUE, RETURN> onConsumeFunction) {
		return new ReturnMatcher<RETURN, CLUE>() {
			@Override
			public boolean isMatch(CLUE clue) {
				return isConsumablePredicate.test(clue);
			}

			@Override
			public RETURN onMatched(CLUE clue) {
				return onConsumeFunction.apply(clue);
			}
		};
	}
}
