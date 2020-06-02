package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.CompositeToResult;
import com.zyh.pro.scanner.main.ToResult;
import org.junit.Test;

import static com.zyh.pro.scanner.main.ReturnMatcher.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ToResultTest {

	@Test
	public void composite() {
		CompositeToResult<String, String> composite = new CompositeToResult<String, String>()
				.add(of(string -> string.equals("hello"), string -> "world"))
				.add(of(string -> string.equals("helloWorld"), string -> "worldHello"));
		assertThat(composite.get("hello"), is("world"));
		assertThat(composite.get("helloWorld"), is("worldHello"));
	}

	@Test
	public void matchedToResult() {
		ToResult<String, String> matched = of(string -> string.equals("hello"), string -> "world");
		assertThat(matched.get("hello"), is("world"));
		assertThat(matched.get("non-exist"), nullValue());
	}
}
