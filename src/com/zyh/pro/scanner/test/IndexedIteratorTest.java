package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.ArrayIndexedIterator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class IndexedIteratorTest {
	@Test
	public void array_IndexedIterator() {
		ArrayIndexedIterator<String> iterator = new ArrayIndexedIterator<>(new String[]{"1", "2"});
		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.getIndex(), is(0));
		assertThat(iterator.next(), is("1"));

		assertThat(iterator.hasNext(), is(true));
		assertThat(iterator.getIndex(), is(1));
		assertThat(iterator.next(), is("2"));

		assertThat(iterator.hasNext(), is(false));
		assertThat(iterator.getIndex(), is(2));
		try {
			iterator.next(); // empty exception...
			fail();
		} catch (ArrayIndexOutOfBoundsException ignored) {}
		assertThat(iterator.hasNext(), is(false));
	}
}
