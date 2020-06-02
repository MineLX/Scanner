package com.zyh.pro.scanner.main;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ReturnMatcher<RETURN, CLUE> extends Match<CLUE>, ToResult<RETURN, CLUE> {

	RETURN onMatched(CLUE clue);

	@Override
	default RETURN get(CLUE clue) {
		if (isMatch(clue))
			return onMatched(clue);
		return null;
	}

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> returns(RETURN returnValue) {
		return of(clue -> true, clue -> returnValue);
	}

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> returns(Function<CLUE, RETURN> returns) {
		return of(clue -> true, returns);
	}

	static <RETURN, CLUE> ReturnMatcher<RETURN, CLUE> of(Predicate<CLUE> isConsumablePredicate,
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
