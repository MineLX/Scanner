package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.IScanner;
import com.zyh.pro.scanner.main.Scanner;
import com.zyh.pro.scanner.main.TrimmedScanner;
import com.zyh.pro.taskscheduler.main.CallbackTask;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TokenizerTest {
	@Test
	public void edge_toList() {
		CharsTokenizer tokenizer = new CharsTokenizer(new TrimmedScanner(new Scanner("1 ")));
		assertThat(tokenizer.toList().toString(), is("[1]"));
	}

	@Test
	public void step_tokenizer() throws InterruptedException {
		CallbackTask task = new CallbackTask();
		IScanner source = new Scanner("hello");
		Tokenizer tokenizer = new CharsTokenizer(source);
		assertThat(tokenizer.next(), is("h"));
		assertThat(tokenizer.next(), is("e"));
		assertThat(tokenizer.next(), is("l"));
		assertThat(tokenizer.next(), is("l"));
		tokenizer.consume(new Tokenizer.CallBack() {
			@Override
			public void onTokenComes(String token, int startAt) {
			}

			@Override
			public void onEnd() {
				task.done();
			}
		});
		assertThat(tokenizer.hasNext(), is(false));
		task.waitForCompletion();
	}

	@Test
	public void onEnd() {
		CharsTokenizer tokenizer = new CharsTokenizer(new Scanner("hello"));
		Tokenizer.CallBack callback = new Tokenizer.CallBack() {
			boolean isEnd;

			@Override
			public void onTokenComes(String token, int startAt) {
				System.out.println("hello");
			}

			@Override
			public void onEnd() {
				assertFalse(isEnd);
				isEnd = true;
			}
		};
		tokenizer.consume(callback);
		tokenizer.consume(callback);
	}

	@Test
	public void collector() {
		Tokenizer tokenizer = new CharsTokenizer(new Scanner("print(456);print(123)"));
		List<String> result = tokenizer.map(tokens -> String.join("", tokens), token -> token.equals(";"));
		assertThat(result.toString(), is("[print(456);, print(123)]"));
	}

	@Test
	public void toList() {
		CharsTokenizer tokenizer = new CharsTokenizer(new Scanner("hello"));
		List<String> result = tokenizer.toList();
		assertThat(result.toString(), is("[h, e, l, l, o]"));
	}

	@Test
	public void simple_test() {
		Tokenizer tokenizer = new CharsTokenizer(new Scanner("hello"));
		tokenizer.consume(new TestTokenConsumer(asList(
				"h" + "0",
				"e" + "1",
				"l" + "2",
				"l" + "3",
				"o" + "4"
		)));
	}

	private static class TestTokenConsumer implements Tokenizer.CallBack {

		private final List<String> result;

		private int index;

		private TestTokenConsumer(List<String> result) {
			this.result = result;
		}

		@Override
		public void onTokenComes(String token, int startAt) {
			assertThat(token + startAt, is(result.get(index++)));
		}
	}

	private static class CharsTokenizer extends Tokenizer {

		CharsTokenizer(IScanner source) {
			super(source);
		}

		@Override
		public String next() {
			return valueOf(scanner.nextChar());
		}
	}
}
