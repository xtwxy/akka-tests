package fsudb.httpImpl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import fsudb.FSUResultSet;

public class ResultSetImpl implements FSUResultSet {
	private int currentRow = -1;
	private String[] columnNames;
	private ArrayList<Vector<String>> rows;
	
	/*
	 * 辅助对象，用于<code>ResultSetImpl#getColumnAsString()</code>快速查找。
	 */
	private Map<String, Integer> mapColumnNameToIndex;
	
	synchronized void setColumnNames(ArrayList<String> headersString) {
		final int SIZE = headersString.size();
		this.columnNames = new String[SIZE];
		mapColumnNameToIndex = new Hashtable<String, Integer>();
		for(int i = 0; i < SIZE; i++) {
			this.columnNames[i] = headersString.get(i);
			mapColumnNameToIndex.put(headersString.get(i), Integer.valueOf(i));
		}
	}
	
	void setRows(ArrayList<Vector<String>> rows) {
		this.rows = rows;
	}
	@Override
	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public String getColumnAsString(String columnName) {
		return rows.get(currentRow).get(mapColumnNameToIndex.get(columnName));
	}

	@Override
	public String getColumnAsString(int columnNumber) {
		return rows.get(currentRow).get(columnNumber);
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public boolean hasNext() {
		return (currentRow < rows.size() - 1);
	}

	@Override
	public boolean next() {
		if (currentRow < rows.size() - 1) {
			currentRow += 1;
			return true;
		}
		return false;
	}

	@Override
	public boolean first() {
		if (!rows.isEmpty()) {
			currentRow = 0;
			return true;
		}
		return false;
	}


}
