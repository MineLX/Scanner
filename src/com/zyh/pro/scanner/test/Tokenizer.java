package com.zyh.pro.scanner.test;

import com.zyh.pro.scanner.main.FunctionalCombiner;
import com.zyh.pro.scanner.main.IScanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Tokenizer implements Iterator<String> {

	protected final IScanner scanner;

	public Tokenizer(IScanner scanner) {
		this.scanner = scanner;
	}

	public void consume(CallBack consumer) {
		pushNext(consumer);
		if (scanner.isEmpty())
			consumer.onEnd();
	}

	private void pushNext(CallBack consumer) {
		int index = scanner.getIndex();
		consumer.onTokenComes(next(), index);
	}

	@Override
	public abstract String next();

	@Override
	public boolean hasNext() {
		return !scanner.isEmpty();
	}

	public List<String> toList() {
		List<String> result = new LinkedList<>();
		consumeAll((token, startAt) -> result.add(token));
		return result;
	}

	// FIXME 2020/4/26  wait for me!!!  could be iteratorsHelper or not Tokenizer
	public List<String> map(Function<List<String>, String> converter, Predicate<String> terminatorPredicate) {
		List<String> result = new ArrayList<>();
		consumeAll(new FunctionalCombiner(combination -> result.add(converter.apply(combination)), terminatorPredicate));
		return result;
	}

	public void consumeAll(CallBack consumer) {
		while (hasNext())
			pushNext(consumer);
		consumer.onEnd();
	}

	public interface CallBack {
		void onTokenComes(String token, int startAt);

		default void onEnd() {
		}
	}
}
