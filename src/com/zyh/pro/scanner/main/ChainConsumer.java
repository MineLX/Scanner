package com.zyh.pro.scanner.main;

public class ChainConsumer<Consumed> {

	private final Matcher<Consumed> matcher;

	private ChainConsumer<Consumed> next;

	public ChainConsumer(Matcher<Consumed> matcher) {
		this.matcher = matcher;
	}

	public boolean consume(Consumed clue) {
		if (matcher.isMatch(clue)) {   // consume it if item is match
			matcher.onMatched(clue);
			return true;
		}
		if (next != null)         // send it to next if possible
			return next.consume(clue);
		return false;             // there is no way to consume this clue
	}

	public static class Builder<Consumed> {

		private ChainConsumer<Consumed> result;

		private ChainConsumer<Consumed> current;

		public Builder<Consumed> next(Matcher<Consumed> matcher) {
			if (result == null) {
				result = current = new ChainConsumer<>(matcher);
				return this;
			}
			current.next = new ChainConsumer<>(matcher);
			current = new ChainConsumer<>(matcher);
			return this;
		}

		public ChainConsumer<Consumed> build() {
			return result;
		}
	}
}
