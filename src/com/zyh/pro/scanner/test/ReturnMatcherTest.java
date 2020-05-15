package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReturnMatcherTest {
//	@Test
//	public void text_schema_matcher() {
//		StringScanner scanner = new StringScanner("alpha");
//		MatcherParser parser = new MatcherParser();
//		ReturnMatcher<String, StringScanner> matcher = parser.parse("[a-z]*");
//		assertThat(matcher.isMatch(scanner), is(true));
//	}

	@Test
	public void parse_with_tokens() {
		StringScanner scanner = new StringScanner("alpha");
		MatcherParser parser = new MatcherParser();
		Match<StringScanner> matcher = parser.parseWithTokens(singletonList("[a-z]*"));
		assertThat(matcher.isMatch(scanner), is(true));
	}

	@Test
	public void composite() {
		CompositeMatcher<String, String> matcher = new CompositeMatcher<>();
		matcher.add(new ReturnMatcher<String, String>() {
			@Override
			public boolean isMatch(String o) {
				return o.equals("hello");
			}

			@Override
			public String onMatched(String o) {
				return "world";
			}
		});
		matcher.add(new ReturnMatcher<String, String>() {
			@Override
			public boolean isMatch(String o) {
				return o.equals("world");
			}

			@Override
			public String onMatched(String o) {
				return "hello";
			}
		});
		assertThat(matcher.isMatch("hello"), is(true));
		assertThat(matcher.isMatch("world"), is(true));
		assertThat(matcher.onMatched("hello"), is("world"));
		assertThat(matcher.onMatched("world"), is("hello"));
	}
}
