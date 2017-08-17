package fsudb.httpImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import fsudb.FSUDB;
import fsudb.FSUResultSet;

public class FSUDBQueryImpl implements FSUDB {
	private String baseUrl;
	
	/**
	 * 使用URL构造一个对象。
	 * 例如，访问表的URL是
	 * <tt>http://localhost:9999/resources/tables/</tt>, 则URL应为此值，而非
	 * <tt>http://localhost:9999/resources/tables/SESSION</tt>或类似的值。
	 * @param url
	 */
	public FSUDBQueryImpl(String url) {
		this.baseUrl = url;
	}
	@Override
	public boolean executeStatement(String sql) {
		try {
			HttpURLConnection connection = getConnection(baseUrl);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.print("sql=" + sql);
			pw.close();
			os.close();
			connection.disconnect();
			final int RESPONSE_CODE = connection.getResponseCode();
			if(RESPONSE_CODE >= 200 && RESPONSE_CODE < 300) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean executeStatements(List<String> statements) {
		try {
			HttpURLConnection connection = getConnection(baseUrl);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			for(String sql : statements) {
				pw.print("sql=" + sql);
				pw.print("&");
			}
			pw.close();
			os.close();
			connection.disconnect();
			final int RESPONSE_CODE = connection.getResponseCode();
			if(RESPONSE_CODE >= 200 && RESPONSE_CODE < 300) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public FSUResultSet getTableContent(String tableName, String whereClause) {
		final ResultSetImpl resultSet = new ResultSetImpl();
		try {
			String url = null;
			if(whereClause != null) {
				url = baseUrl + tableName + "?clause=" + whereClause;
			} else {
				url = baseUrl + tableName;
			}
			url = url.replaceAll(" ", "%20");
			HttpURLConnection connection = getConnection(url);
			connection.setRequestMethod("GET");
			connection.addRequestProperty("Accept-Content", "text/html");
			connection.disconnect();
			final int RESPONSE_CODE = connection.getResponseCode();
			if(RESPONSE_CODE >= 200 && RESPONSE_CODE < 300) {
				Parser p = new Parser(connection);
				p.visitAllNodesWith(new NodeVisitor() {
				     public void visitTag (Tag tag)
				     {
				    	 if(tag instanceof TableTag) {
				    		 TableTag ttag = (TableTag) tag;

				    		 TableRow[] rows = ttag.getRows();
				    		 ArrayList<String> headersString = new ArrayList<String>();
				    		 ArrayList<Vector<String>> rowsString = new ArrayList<Vector<String>> ();
				    		 for(TableRow row : rows) {
				    			 final TableHeader[] headers = row.getHeaders();
			    				 final int HEADERS_COUNT = headers.length;
				    			 if(HEADERS_COUNT != 0) {
					    			 for(int i = 0; i < HEADERS_COUNT; i++) {
					    				 headersString.add(headers[i].getStringText());
					    			 }
				    			 } else {
					    			 final TableColumn[] columns = row.getColumns();
				    				 final int COLUMNS_COUNT = columns.length;
				    				 Vector<String> rowString = new Vector<String>(COLUMNS_COUNT);
					    			 for(int i = 0; i < COLUMNS_COUNT; i++) {
					    				 rowString.add(i, columns[i].getStringText());
					    			 }
					    			 rowsString.add(rowString);
				    			 }
				    		 }
				    		 resultSet.setColumnNames(headersString);
				    		 resultSet.setRows(rowsString);
				    	 }
				     }

				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	/**
	 * 创建连接。
	 * @param url
	 */
	private HttpURLConnection getConnection(String url) throws IOException {
		URL theUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) theUrl
				.openConnection();
		HttpURLConnection.setFollowRedirects(true);
		return connection;
	}
}
