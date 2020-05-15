package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.*;
import org.junit.Test;

import java.util.List;

import static com.zyh.pro.scanner.main.ReturnMatcher.functional;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SequenceTest {
	@Test
	public void edge_multi_sequence() {

	}

	@Test
	public void sequenceMatcher() {
		StringScanner scanner = new StringScanner("1,2,3unrecognized");
		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(functional(scanner1 -> scanner1.existsIf(Character::isDigit),
						IStringScanner::nextFloat))
				.add(functional(scanner1 -> scanner1.exists(","),
						scanner1 -> valueOf(scanner1.nextChar()))));
		assertThat(sequence.toList().toString(), is("[1, ,, 2, ,, 3]"));
	}

	@Test
	public void map() { // FIXME 2020/5/2  wait for me!!!  elements built sequence
		StringScanner scanner = new StringScanner("123||456|||789");

		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(functional(
						scanner1 -> scanner1.existsIf(Character::isDigit),
						IStringScanner::nextFloat))
				.add(functional(
						scanner1 -> scanner1.exists("|"),
						scanner1 -> String.valueOf(scanner1.nextChar())))
		);
		Sequence<Integer> split = sequence.split("|").map(Integer::parseInt);
		assertThat(split.toList().toString(), is("[123, 456, 789]"));
	}

	@Test
	public void split() {
		StringScanner scanner = new StringScanner("123||456|||789");

		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(functional(
						scanner1 -> scanner1.existsIf(Character::isDigit),
						IStringScanner::nextFloat))
				.add(functional(
						scanner1 -> scanner1.exists("|"),
						scanner1 -> String.valueOf(scanner1.nextChar())))
		);
		Sequence<String> split = sequence.split("|");
		assertThat(split.toList().toString(), is("[123, 456, 789]"));
	}

	@Test
	public void merge() {
		StringScanner scanner = new StringScanner("word1,word2,word3,word4,word5");

		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(functional(
						scanner1 -> scanner1.existsIf(Character::isAlphabetic),
						IStringScanner::nextPage))
				.add(functional(
						scanner1 -> scanner1.exists(","),
						scanner1 -> String.valueOf(scanner1.nextChar())))
		);
		Sequence<String> merged = sequence.merge("word3", (summary, addend) -> summary + addend);
		assertThat(merged.toList().toString(), is("[word1,word2,word3, ,word4,word5]"));
	}

	@Test
	public void unrecognized_char() {
		TrimmedStringScanner scanner = new TrimmedStringScanner(new StringScanner("alpha ? -> (follow an unrecognized char)"));
		Sequence<String> sequence = scanner.sequence(new MatcherToResult<String, IStringScanner>(functional(
				scanner1 -> scanner1.existsIf(Character::isAlphabetic),
				IStringScanner::nextAlpha
		)));
		assertThat(sequence.toList().toString(), is("[alpha]"));
	}

	@Test
	public void til() {
		IStringScanner source = new StringScanner("print()");
		Sequence<String> tokenizer = getCharsSequence(source);
		assertThat(tokenizer.til("(").toString(), is("[p, r, i, n, t]"));
		assertThat(source.toString(), is(")"));
	}

	@Test
	public void edge_toList() {
		TrimmedStringScanner source = new TrimmedStringScanner(new StringScanner("1 "));
		Sequence<String> tokenizer = getCharsSequence(source);
		assertThat(tokenizer.toList().toString(), is("[1]"));
	}

	@Test
	public void step_tokenizer() {
		Sequence<String> sequence = getCharsSequence(new StringScanner("hello"));
		assertThat(sequence.toList().toString(), is("[h, e, l, l, o]"));
	}

	@Test
	public void collector() {
		IStringScanner source = new StringScanner("print(456);print(123)");
		Sequence<String> tokenizer = getCharsSequence(source);
		List<String> result = tokenizer.map(tokens -> String.join("", tokens), token -> token.equals(";"));
		assertThat(result.toString(), is("[print(456);, print(123)]"));
	}

	@Test
	public void toList() {
		IStringScanner source = new StringScanner("hello");
		Sequence<String> tokenizer = getCharsSequence(source);
		assertThat(tokenizer.toList().toString(), is("[h, e, l, l, o]"));
	}

	@Test
	public void simple_test() {
		IStringScanner source = new StringScanner("hello");
		Sequence<String> tokenizer = getCharsSequence(source);
		tokenizer.consumeAll(new TestTokenConsumer(asList(
				"h" + "0",
				"e" + "1",
				"l" + "2",
				"l" + "3",
				"o" + "4"
		)));
	}

	private static class TestTokenConsumer implements Sequence.IndexedConsumer<String> {

		private final List<String> result;

		private int index;

		private TestTokenConsumer(List<String> result) {
			this.result = result;
		}

		@Override
		public void onConsume(String element, int startAt) {
			assertThat(element + startAt, is(result.get(index++)));
		}
	}

	private Sequence<String> getCharsSequence(IStringScanner scanner) {
		return scanner.sequence(scanner1 -> {
			System.out.println("scanner1 = " + scanner1);
			return valueOf(scanner1.nextChar());
		});
	}
}
