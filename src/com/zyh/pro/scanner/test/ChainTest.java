package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.Chain;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChainTest {
	@Test
	public void simple_test() {
		Chain<String> chain = new Chain<>();
		chain.next("hello").next("world");

		String hello = "hello";
		for (String s : chain) {
			assertThat(s, is(hello));
			hello = "world";
		}
	}
}
