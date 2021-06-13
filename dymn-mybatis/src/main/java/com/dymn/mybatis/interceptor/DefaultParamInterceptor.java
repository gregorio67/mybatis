package com.dymn.mybatis.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DefaultParamInterceptor  implements Interceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParamInterceptor.class);
	
	private List<String> columns;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		
		Object[] parameters = invocation.getArgs();
		Object sqlParam = parameters[1];
		String sqlId = mappedStatement.getId();
		
		if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT) {
			String userId = "UNKNOWN";
			/** Retrieve userId from session **/
			
			String ipAddr = null;
			
			Date curDate = new Date();
			
			Map<String, Object> addColumns = addColumns(userId, ipAddr, curDate);
			if (sqlParam instanceof Map<?, ?>) {
				
			}
			else if (sqlParam instanceof List){
				
			}
			else {
				for (String field : addColumns.keySet()) {
					try {
						FieldUtils.writeField(sqlParam, field, addColumns.get(field), true);						
					}
					catch(Exception ex) {
						LOGGER.error("{} is not defined in the class", field, sqlParam.getClass().getName() );
					}
				}
				
				parameters[1] = sqlParam;
				
				LOGGER.info("Sql parameter in {} is changed :: {}", sqlId, sqlParam) ;
			}
			
		}
		
		
		return invocation.proceed();
	}
	
	/**
	 * 
	 * @param userId
	 * @param ipAddr
	 * @param curDate
	 * @return
	 */
	private Map<String, Object> addColumns(String userId, String ipAddr, Date curDate) {
		Map<String, Object> result = new HashMap<>();
		for (String column : columns) {
			if (column.endsWith("usrId")) {
				result.put(column, userId);
			}
			else if (column.endsWith("ipAddr")) {
				result.put(column, ipAddr);
			}
			else if (column.endsWith("date")) {
				result.put(column, curDate);
			}
		}
		
		return result;
	}

}
