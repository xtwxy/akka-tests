package fsudb;

import java.util.List;

/**
 * FSU的数据库访问接口，用于在测试代码中更新、取得FSU数据库表中的内容。
 * 
 * 由于FSU进程以C++实现，且运行于嵌入式系统，因此不方便使用JDBC等方法来访问其中的数据，故在
 * FSU中加入通过HTTP直接插入、更新、取得表内容的功能，外部程序通过HTTP来使用此功能，避免
 * <ol>
 *   <li>使用JDBC只能在台式电脑上测试的问题。</li>
 *   <li>多进程访问SQLite数据库产生的锁定库文件带来的问题。</li>
 * </ol>
 * 以接口方式声明其功能，再以HTTP实现此接口，将来有可能更换成其它方便的方法，又不影响使用此代
 * 码的测试代码。
 * 
 * @author wangxy
 */
public interface FSUDB {
	/**
	 * 执行单条SQL语句。
	 * @param sql 要执行的SQL语句
	 * @return <code>true</code> - 表示事务成功提交； 
	 * 	<code>false</code> - 表示事务回滚。
	 */
	public boolean executeStatement(String sql);
	/**
	 * 执行多条SQL语句。
	 * 
	 * 实现代码应支持事务，以免造成数据混乱的麻烦。
	 * 
	 * @param statements SQL语句清单。
	 * @return <code>true</code> - 表示事务成功提交； 
	 * 	<code>false</code> - 表示事务回滚。
	 */
	public boolean executeStatements(List<String> statements);
	/**
	 * 取得表的内容。
	 * @param tableName 表名。
	 * @param whereClause 查询条件，即SQL语句的WHERE子句。
	 * @return 结果集。
	 */
	public FSUResultSet getTableContent(String tableName, String whereClause);
}
