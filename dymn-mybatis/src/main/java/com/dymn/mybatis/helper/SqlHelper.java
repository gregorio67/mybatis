package com.dymn.mybatis.helper;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

public class SqlHelper {

	
	
	private SqlHelper() {
		/** Do Nothing **/
	}
	
	/**
	 * 
	 * @param sql
	 * @param maxRows
	 * @return
	 */
	public static String addLimitSql(String sql, String maxRows) {
		StringBuilder tempSql = new StringBuilder();
		
		tempSql.append("SELECT * FROM (SELECT TEMP.*, ROWNUM ROW_ID FROM (");
		tempSql.append(sql);
		tempSql.append(") TEMP WHERE ROWNUM <= ").append(maxRows);
		tempSql.append(") WHERE ROW_ID > 0");
		
		return tempSql.toString();
	}
	
	
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static String limitPageSql(String sql, String offset, String limit) {
		StringBuilder tempSql = new StringBuilder();
		tempSql.append(sql).append("\n");
		tempSql.append("limit ").append(offset).append(", ").append(limit);
		
		return tempSql.toString();
	}
	
	/**
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static String limitOffsetSql(String sql, String offset, String limit) {
		StringBuilder tempSql = new StringBuilder();
		tempSql.append(sql).append("\n");
		tempSql.append("limit ").append(limit);
		tempSql.append(" offset ").append(offset);
		
		return tempSql.toString();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getCaller() throws Exception {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        String callerClass = ste.getClassName();
        String callerMethod = ste.getMethodName();
        
        return Arrays.asList(callerClass, callerMethod);
	}
	
	
	public enum DBType {
		ORACLE,
		TIBERO,
		MYSQL,
		POSTGRESQL,
		MARIADB;
		
		public static DBType getType(String type) {
			if (type.equalsIgnoreCase(DBType.ORACLE.name())) {
				return DBType.ORACLE;
			}
			else if (type.equalsIgnoreCase(DBType.TIBERO.name())) {
				return DBType.TIBERO;
			}
			else if (type.equalsIgnoreCase(DBType.MYSQL.name())) {
				return DBType.MYSQL;
			}
			else if (type.equalsIgnoreCase(DBType.POSTGRESQL.name())) {
				return DBType.POSTGRESQL;
			}
			else {
				return DBType.MARIADB;
			}
		}
	}
}
