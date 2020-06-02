package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.*;
import org.junit.Test;

import java.util.List;

import static com.zyh.pro.scanner.main.ReturnMatcher.of;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SequenceTest {
	@Test
	public void collect() {
		assertThat(Sequence.of(1, 2).collect(Collector.toList()).toString(),
				is("[1, 2]"));
	}

	@Test
	public void sequence_of() {
		Sequence<String> sequence = Sequence.of("1", "2");
		assertThat(sequence.toList().toString(), is("[1, 2]"));
	}

	@Test
	public void sequenceMatcher() {
		StringScanner scanner = new StringScanner("1,2,3unrecognized");
		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(of(scanner1 -> scanner1.existsIf(Character::isDigit),
						IStringScanner::nextFloat))
				.add(of(scanner1 -> scanner1.exists(","),
						scanner1 -> valueOf(scanner1.nextChar()))));
		assertThat(sequence.toList().toString(), is("[1, ,, 2, ,, 3]"));
	}

	@Test
	public void map() {
		Sequence<String> sequence = Sequence.of("1", "2");
		Sequence<Integer> split = sequence.map(Integer::parseInt);
		assertThat(split.toList().toString(), is("[1, 2]"));
	}

	@Test
	public void split() {
		StringScanner scanner = new StringScanner("123||456|||789");

		Sequence<String> sequence = scanner.sequence(new CompositeToResult<String, IStringScanner>()
				.add(of(
						scanner1 -> scanner1.existsIf(Character::isDigit),
						IStringScanner::nextFloat))
				.add(of(
						scanner1 -> scanner1.exists("|"),
						scanner1 -> String.valueOf(scanner1.nextChar())))
		);
		Sequence<String> split = sequence.split("|");
		assertThat(split.toList().toString(), is("[123, 456, 789]"));
	}

	@Test
	public void unrecognized_char() {
		TrimmedStringScanner scanner = new TrimmedStringScanner(new StringScanner("alpha ? -> (follow an unrecognized char)"));
		Sequence<String> sequence = scanner.sequence(of(
				scanner1 -> scanner1.existsIf(Character::isAlphabetic),
				IStringScanner::nextAlpha
		));
		assertThat(sequence.toList().toString(), is("[alpha]"));
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
