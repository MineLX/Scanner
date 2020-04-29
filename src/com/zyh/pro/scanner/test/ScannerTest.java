package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.IScanner;
import com.zyh.pro.scanner.main.Scanner;
import com.zyh.pro.scanner.main.TrimmedScanner;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScannerTest {
	@Test
	public void containsBetween() {
		assertThat(getScanner("(hello)").between('(', ')'), is("hello"));
		assertThat(getScanner("non-exists").between('(', ')'), is(""));
		assertThat(getScanner("(hello))").between('(', ')'), is("hello"));
		assertThat(getScanner("((hello))").between('(', ')'), is("(hello)"));
		assertThat(getScanner("(((((hello)))world))").between('(', ')'), is("((((hello)))world)"));
		assertThat(getScanner("(hell").between('(', ')'), is("hell"));
	}

	@Test
	public void edge_existsIf() {
		IScanner scanner = getScanner("");
		assertThat(scanner.existsIf(item -> true), is(false));
	}

	@Test
	public void existsIf() {
		IScanner scanner = getScanner("print(...)");
		assertThat(scanner.existsIf(Character::isAlphabetic), is(true));
	}

	@Test
	public void collect_trimmedScanner() {
		TrimmedScanner scanner = new TrimmedScanner(new Scanner(",,  2"));
		assertThat(scanner.collect(item -> item == ','), is(",,"));
		assertThat(scanner.toString(), is("  2"));
	}

	@Test
	public void pass_nonsenses() {
		IScanner scanner = getScanner(" \n3");
		scanner.collect(item -> item == ' ');
		assertThat(scanner.nextChar(), is('\n'));
	}

	@Test
	public void nextFloat_test() {
		IScanner scanner = getScanner("1.1, ");
		assertThat(scanner.nextFloat(), is("1.1"));
	}

	@Test
	public void til_test() {
		IScanner scanner = getScanner("hello|world");
		assertThat(scanner.til('|'), is("hello"));
	}

	@Test
	public void is_empty_test() {
		IScanner source = getScanner("source");
		source.nextPage();
		assertThat(source.isEmpty(), is(true));
	}

	@Test
	public void pass_all_spaces_tabs_n_breaks() {
		IScanner scanner = getScanner("  hello");
		scanner.trim();
		assertThat(scanner.nextChar(), is('h'));
	}

	@Test
	public void nextChars_test() {
		IScanner source = getScanner("source");
		String chars = source.nextChars(4);

		assertThat(chars, is("sour"));
		assertThat(source.nextChar(), is('c'));
	}

	@Test
	public void exists_no_effects() {
		IScanner source = getScanner("source");
		assertThat(source.exists("sour"), is(true));
	}

	@Test
	public void exists_side_test() {
		IScanner scanner = getScanner("hello");
		scanner.nextPage();
		assertThat(scanner.pass("myTub"), is(false));

		scanner.pullBack(2);
		assertThat(scanner.pass("hello"), is(false));
	}

	@Test
	public void has_test() {
		IScanner scanner = getScanner("hell");
		scanner.nextPage();
		assertThat(scanner.hasCount(), is(0));
	}

	@Test
	public void consume_an_char_test() {
		IScanner scanner = getScanner("</hello");
		IScanner scanner1 = getScanner("<hello");
		IScanner scanner2 = getScanner("hello");
		assertThat(scanner.pass("</"), is(true));
		assertThat(scanner1.pass("</"), is(false));
		assertThat(scanner2.pass("</"), is(false));
		assertThat(scanner1.nextChar(), is('<'));
		assertThat(scanner2.nextChar(), is('h'));
	}

	@Test
	public void scan_side_effect_test() {
		IScanner scanner = getScanner("source>hello");
		scanner.nextAlpha();
		assertThat(scanner.nextChar(), is('>'));
	}

	@Test
	public void scan_page_test() {
		IScanner scanner = getScanner("s1our2c>e");
		assertThat(scanner.nextPage(), is("s1our2c"));
	}

	@Test
	public void scan_alpha_test() {
		IScanner scanner = getScanner("source");
		assertThat(scanner.nextAlpha(),
				is("source"));
	}

	@Test
	public void hasNext_test() {
		IScanner scanner = getScanner("ice");
		scanner.nextChar();
		assertThat(scanner.hasNext(), is(true));
		scanner.nextChar();
		scanner.nextChar();
		assertThat(scanner.hasNext(), is(false));
	}

	@Test
	public void pull_back_test() {
		IScanner scanner = getScanner("source");
		char next = scanner.nextChar();
		scanner.pullBack(1);
		assertThat(scanner.nextChar(), is(next));
	}

	@Test
	public void get_char_test() {
		IScanner scanner = getScanner("source");
		assertThat(scanner.nextChar(), is('s'));
		assertThat(scanner.nextChar(), is('o'));
		assertThat(scanner.nextChar(), is('u'));
	}

	private IScanner getScanner(String source) {
		return new Scanner(source);
	}
}
