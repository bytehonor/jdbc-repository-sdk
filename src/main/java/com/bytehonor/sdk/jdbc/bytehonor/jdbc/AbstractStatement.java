package com.bytehonor.sdk.jdbc.bytehonor.jdbc;

import java.util.List;

public interface AbstractStatement {

	public List<Object> listArgs();
	
	public Object[] args();
	
	public List<Integer> listTypes();

	public int[] types();
}
