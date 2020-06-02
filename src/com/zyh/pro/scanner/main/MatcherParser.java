package com.zyh.pro.scanner.main;

import java.util.ArrayList;
import java.util.List;

import static com.zyh.pro.scanner.main.ReturnMatcher.of;

public class MatcherParser {

	private final CollectTree.Root<String, IStringScanner> formatTokenMatcher;

	private final CompositeToResult<ReturnMatcher<String, IStringScanner>, String> matchersMatcher;

	public MatcherParser() {
		formatTokenMatcher = new CollectTree.Root<>((one, another) -> one + another);
		formatTokenMatcher.addChild(of(scanner -> scanner.exists("[a-z]*"), scanner -> scanner.nextChars("[a-z]*".length())));

		matchersMatcher = new CompositeToResult<>();
		matchersMatcher.add(new ReturnMatcher<ReturnMatcher<String, IStringScanner>, String>() {
			@Override
			public boolean isMatch(String s) {
				return s.equals("[a-z]*");
			}

			@Override
			public ReturnMatcher<String, IStringScanner> onMatched(String s) {
				return of(scanner -> scanner.existsIf(Character::isAlphabetic), IStringScanner::nextAlpha);
			}
		});
	}

	public ReturnMatcher<String, StringScanner> parse(String format) {
		StringScanner scanner = new StringScanner(format);
		List<String> strings = scanner.sequence(formatTokenMatcher).toList();
		System.out.println("strings = " + strings);
		return null;
	}

	public Match<StringScanner> parseWithTokens(List<String> formatTokens) {
		List<ReturnMatcher<String, IStringScanner>> matchers = new ArrayList<>();
		for (String formatToken : formatTokens) {
			matchers.add(matchersMatcher.get(formatToken));
		}
		return scanner -> {
			for (ReturnMatcher<String, IStringScanner> matcher : matchers) {
				if (!matcher.isMatch(scanner))
					return false;
				matcher.onMatched(scanner);
			}
			return true;
		};
	}
}
