package com.zyh.pro.scanner.main;

public interface IScanner<ELEMENT> {
	void pullBack(ELEMENT element);

	boolean hasNext();

	int getIndex();
}
