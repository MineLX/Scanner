package com.zyh.pro.scanner.main;

public abstract class CachedIterator<R> implements IndexedIterator<R> {

	private R cache;

	private int cacheIndex;

	@Override
	public int getIndex() {
		if (cacheIndex != -1)
			return cacheIndex;
		return realIndex();
	}

	@Override
	public boolean hasNext() {
		if (!onHasNext())
			return false;

		if (cache != null)
			return true;

		cacheIndex = realIndex();
		cache = next();
		return cache != null;
	}

	@Override
	public R next() {
		if (cache != null) {
			R result = cache;
			cache = null;
			cacheIndex = -1;
			return result;
		}
		return realNext();
	}

	protected abstract boolean onHasNext();

	protected abstract R realNext();

	protected abstract int realIndex();
}
