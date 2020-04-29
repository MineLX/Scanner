package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.*;
import org.junit.Test;

import java.util.function.BinaryOperator;

import static com.zyh.pro.scanner.main.ReturnMatcher.functional;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ReturnTreeTest {
	@Test
	public void children() {
		BinaryOperator<String> converter = (one, another) -> one + another;
		ReturnTree.Root<String, IScanner> root = chain(converter);
		assertThat(root.get(new Scanner("print(123)")), is("print(123)"));
		assertThat(root.get(new Scanner("print")), is("print"));
		assertThat(root.get(new Scanner("print->{lambda}")), is("print->{lambda}"));
		assertThat(root.get(new Scanner("print#")), is("print"));
		assertNull(root.get(new Scanner("#")));
	}

	@Test
	public void simple_test() {
		CompositeToResult<String, String> composite = new CompositeToResult<>();
		composite.add(new MatcherToResult<>(functional(string -> string.equals("hello"), string -> "world")));
		composite.add(new MatcherToResult<>(functional(string -> string.equals("!"), string -> "null")));
		assertThat(composite.get("hello"), is("world"));
		assertThat(composite.get("!"), is("null"));
		assertNull(composite.get("?"));
	}

	private ReturnTree.Root<String, IScanner> chain(BinaryOperator<String> converter) {
		ReturnTree<String, IScanner> chain = new ReturnTree<>(
				functional(scanner -> scanner.existsIf(Character::isAlphabetic), IScanner::nextAlpha)) ;
		chain.addChild(new ReturnTree<>(
				functional(scanner -> scanner.exists("("),
						scanner -> '(' + scanner.between('(', ')') + ')')));
		chain.addChild(new ReturnTree<>(
				functional(scanner -> scanner.exists("->"),
						scanner -> String.valueOf(scanner.nextChar()) +
								scanner.nextChar() +
								'{' +
								scanner.between('{', '}') +
								'}')));

		ReturnTree.Root<String, IScanner> root = ReturnTree.root(converter);
		root.addChild(chain);
		return root;
	}
}
