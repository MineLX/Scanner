package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.CollectTree;
import com.zyh.pro.scanner.main.CompositeToResult;
import com.zyh.pro.scanner.main.IStringScanner;
import com.zyh.pro.scanner.main.StringScanner;
import org.junit.Test;

import java.util.function.BinaryOperator;

import static com.zyh.pro.scanner.main.ReturnMatcher.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class CollectTreeTest {
	@Test
	public void children() {
		BinaryOperator<String> converter = (one, another) -> one + another;
		CollectTree.Root<String, IStringScanner> root = chain(converter);
		assertThat(root.get(new StringScanner("print(123)")), is("print(123)"));
		assertThat(root.get(new StringScanner("print")), is("print"));
		assertThat(root.get(new StringScanner("print->{lambda}")), is("print->{lambda}"));
		assertThat(root.get(new StringScanner("print#")), is("print"));
		assertNull(root.get(new StringScanner("#")));
	}

	@Test
	public void simple_test() {
		CompositeToResult<String, String> composite = new CompositeToResult<>();
		composite.add(of(string -> string.equals("hello"), string -> "world"));
		composite.add(of(string -> string.equals("!"), string -> "null"));
		assertThat(composite.get("hello"), is("world"));
		assertThat(composite.get("!"), is("null"));
		assertNull(composite.get("?"));
	}

	private CollectTree.Root<String, IStringScanner> chain(BinaryOperator<String> converter) {
		CollectTree<String, IStringScanner> chain = new CollectTree<>(
				of(scanner -> scanner.existsIf(Character::isAlphabetic), IStringScanner::nextAlpha)) ;
		chain.addChild(new CollectTree<>(
				of(scanner -> scanner.exists("("),
						scanner -> '(' + scanner.between('(', ')') + ')')));
		chain.addChild(new CollectTree<>(
				of(scanner -> scanner.exists("->"),
						scanner -> String.valueOf(scanner.nextChar()) +
								scanner.nextChar() +
								'{' +
								scanner.between('{', '}') +
								'}')));

		CollectTree.Root<String, IStringScanner> root = CollectTree.root(converter);
		root.addChild(chain);
		return root;
	}
}
