package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.Collector;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CollectorTest {
	@Test
	public void toList() {
		Collector<String, List<String>> collector = Collector.toList();
		collector.onConsume("1", 0);
		collector.onConsume("2", 1);
		assertThat(collector.toResult().toString(), is("[1, 2]"));
	}
}
