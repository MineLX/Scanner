package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.CompositeToResult;
import com.zyh.pro.scanner.main.MatcherToResult;
import org.junit.Test;

import static com.zyh.pro.scanner.main.ReturnMatcher.whatIf;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ToResultTest {

	@Test
	public void composite() {
		CompositeToResult<String, String> composite = new CompositeToResult<String, String>()
				.add(new MatcherToResult<>(whatIf(string -> string.equals("hello"), string -> "world")))
				.add(new MatcherToResult<>(whatIf(string -> string.equals("helloWorld"), string -> "worldHello")));
		assertThat(composite.get("hello"), is("world"));
		assertThat(composite.get("helloWorld"), is("worldHello"));
	}

	@Test
	public void matchedToResult() {
		MatcherToResult<String, String> matched =
				new MatcherToResult<>(whatIf(string -> string.equals("hello"), string -> "world"));
		assertThat(matched.get("hello"), is("world"));
		assertThat(matched.get("non-exist"), nullValue());
	}
}
