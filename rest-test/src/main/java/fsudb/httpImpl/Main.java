package fsudb.httpImpl;

import java.util.ArrayList;
import java.util.List;

import fsudb.FSUDB;
import fsudb.FSUResultSet;

public class Main {

	private static final String BASE_URL = "http://192.168.0.162:9999/resources/tables/";
	private static FSUDB query;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		query = new FSUDBQueryImpl(BASE_URL);
		
		testExecuteStatement();
		testExecuteStatements();
		testGetTableContent();
	}
	
	public static void testExecuteStatement() {
		System.out.println(query.executeStatement("UPDATE SESSION SET USER_GRADE=0"));
	}
	public static void testExecuteStatements() {
		List<String> statements = new ArrayList<String>();
		
		statements.add("UPDATE SESSION SET USER_GRADE=0");
		statements.add("UPDATE SESSION SET LOGIN_TIME=0");
		
		System.out.println(query.executeStatements(statements));
	}
	
	public static void testGetTableContent() {
		FSUResultSet rs = query.getTableContent("DEVICE_TYPE", "where id=1");
		final String[] columnNames = rs.getColumnNames();
		for(int i = 0; i < columnNames.length; i++) {
			System.out.print(columnNames[i] + ", ");
		}
		
		System.out.println();

		while(rs.next()) {
			for(int i = 0; i < columnNames.length; i++) {
				System.out.print(rs.getColumnAsString(i) + ", ");
			}
			System.out.println();
		}
	}

}
