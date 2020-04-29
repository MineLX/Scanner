package com.zyh.pro.scanner.main;

import com.zyh.pro.scanner.test.Tokenizer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Combiner implements Tokenizer.CallBack {

	private final List<String> combination;

	protected Combiner() {
		combination = new LinkedList<>();
	}

	@Override
	public void onTokenComes(String token, int startAt) {
		combination.add(token);
		if (isTerminator(token))
			combine();
	}

	@Override
	public void onEnd() {
		if (combination.size() != 0)
			combine();
	}

	private void combine() {
		onCombined(Collections.unmodifiableList(combination));
		combination.clear();
	}

	protected abstract void onCombined(List<String> combination);

	protected abstract boolean isTerminator(String token);
}
