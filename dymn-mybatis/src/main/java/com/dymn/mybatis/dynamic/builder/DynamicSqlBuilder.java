package com.dymn.mybatis.dynamic.builder;

import java.util.List;

public class DynamicSqlBuilder {
	
	/**
	 * 
	 * @param sql
	 * @param sqlParams
	 * @return
	 * @throws Exception
	 */
	public static String buildSql(final String unboundSql, final List<Object> sqlParams) throws Exception {
		String resultSql = unboundSql;
		
		for (Object value : sqlParams) {
			if (value instanceof String) {
				resultSql = resultSql.replaceFirst("\\?", String.format("'%s'", (String)value));				
			}
			else if (value instanceof Integer || value instanceof Long || value instanceof Float) {
				resultSql = resultSql.replaceFirst("\\?", String.valueOf(value));
			}
			else if (value instanceof Long) {
				resultSql = resultSql.replaceFirst("\\?", String.format("'%s'", (String)value));								
			}
		}
		
		return resultSql;
	}
	

}
