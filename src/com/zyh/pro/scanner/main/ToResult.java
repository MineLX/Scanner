package com.zyh.pro.scanner.main;

public interface ToResult<RETURN, CLUE> {
	RETURN get(CLUE clue);
}
