package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.ChainConsumer;
import org.junit.Test;

import static com.zyh.pro.scanner.main.Matcher.functional;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChainConsumerTest {
	@Test
	public void simple_test() {
		ChainConsumer<String> chainConsumer = new ChainConsumer.Builder<String>()
				.next(functional(element -> element.equals("hello"),
						element -> System.out.println("element = " + element)))
				.next(functional(element -> element.equals("world"),
						element -> System.out.println("element = " + element)))
				.build();
		assertThat(chainConsumer.consume("world"), is(true));
		assertThat(chainConsumer.consume("hello"), is(true));
		assertThat(chainConsumer.consume("non-exists"), is(false));
	}
}
