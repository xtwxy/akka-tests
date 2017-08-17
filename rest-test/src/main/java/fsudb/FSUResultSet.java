package fsudb;

/**
 * FSU数据库查询的结果集接口。
 * 
 * 此接口类似<code>java.sql.ResultSet</code>接口。
 * 可以根据具体的数据库访问方法实现。例如HTTP实现，或JDBC实现。
 * 
 * <code>FSUDB#getTableContent()</code>可以设计为返回
 * <code>java.sql.ResultSet</code>接口。但由于实现<code>java.sql.ResultSet</code>
 * 接口对于我们的代码来说过于复杂(空的代码框架已经超过1千行)，因此设计此简化的接口。
 * 
 * 查询出错时，采取返回码取代<code>javax.sql.SQLException</code>，方便测试代码使用断言。
 * 
 * 返回的数据以字符串形式表示，但并非所有的数据均可使用这种方式表示。因此，存在局限。
 * 
 * @author wangxy
 * @see <code>java.sql.ResultSet</code>
 */
public interface FSUResultSet {
	/**
	 * 取得列名。
	 * @return 列名，以数组形式
	 */
	public String[] getColumnNames();
	/**
	 * 根据列名取得该列的数据，以字符串表示。
	 * @param columnName 列名。
	 * @return 该列的数据。
	 */
	public String getColumnAsString(String columnName);
	/**
	 * 根据列下标取得该列的数据，以字符串表示。
	 * @param columnNumber 列下标。
	 * @return 该列的数据。
	 */
	public String getColumnAsString(int columnNumber);
	/**
	 * 取得结果集的行数。
	 * @return 行数。
	 */
	public int getRowCount();
	/**
	 * 是否还有更多的行。
	 * @return <code>true</code>表示有，<code>false</code>表示无。
	 */
	public boolean hasNext();
	/**
	 * 移动到下一行。
	 * @return <code>true</code>表示有下一行，<code>false</code>表示无下一行。
	 */
	public boolean next();
	/**
	 * 移动到第一行。
	 * @return <code>true</code>表示有第一行，<code>false</code>表示无第一行。
	 */
	public boolean first();
}
