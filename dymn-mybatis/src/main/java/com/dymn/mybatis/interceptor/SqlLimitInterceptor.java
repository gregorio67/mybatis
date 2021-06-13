/**
 * @Project : 병행검증 솔루션
 * @Class : CounterInteceptor.java
 * @Description :
 * @Author : kimdoy
 * @Since : Feb 19, 2020
 * @Copyright LG CNS
 *------------------------------------------------------
 *      Dodification Information
 *------------------------------------------------------
 *  Date        Modifier       Reason
 *------------------------------------------------------
 *  Feb 19, 2020     kimdoy         Initial
 *------------------------------------------------------
 */   
package com.dymn.mybatis.interceptor;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dymn.mybatis.context.ExecuteService;
import com.dymn.mybatis.context.ServiceContext;
import com.dymn.mybatis.context.ServiceInfos;
import com.dymn.mybatis.helper.SqlHelper;
import com.dymn.mybatis.helper.SqlHelper.DBType;

//@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class SqlLimitInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlLimitInterceptor.class);
	
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
	
	private String offset;
	private String limit;
	private String dbType;
	

	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		/** Interceptor Type is StatementHandler **/
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		
//		String sqlId = mappedStatement.getDatabaseId();
		
		SqlCommandType sqlType = mappedStatement.getSqlCommandType();
		
		ServiceInfos serviceInfos = ServiceContext.getContext();
		ExecuteService serviceInfo = serviceInfos.getExecuteServices() != null ? serviceInfos.getExecuteServices().get(serviceInfos.getExecuteServices().size() - 1) : null;
		
		LOGGER.info("Current Service :: [{}-{}]", serviceInfo.getClassName(), serviceInfo.getMethodName());
		if (!"SELECT".equalsIgnoreCase(sqlType.name()) || !serviceInfo.getMethodName().contains("List")) {
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		String originalSql = boundSql.getSql();
		
		List<String> callers = SqlHelper.getCaller();
		LOGGER.info("Caller class and method ::[{}-{}]", callers.get(0), callers.get(1));
		String limitSql = null;
		if (DBType.getType(dbType) == DBType.POSTGRESQL) {
			limitSql = SqlHelper.limitOffsetSql(originalSql, "0", "2");
		}
		else if (DBType.getType(dbType) == DBType.MYSQL || DBType.getType(dbType) == DBType.MARIADB) {
			limitSql = SqlHelper.limitPageSql(originalSql, "0", "2");			
		}
		else {
			limitSql = SqlHelper.addLimitSql(originalSql, "2");			
		}
		
		LOGGER.info("Limit SQL :: {}", limitSql);
		metaObject.setValue("delegate.boundSql.sql", limitSql);
		
		return invocation.proceed(); 
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties property) {
		this.offset = property.getProperty("offset") != null ? (String)property.getProperty("offset") : null;
		this.limit = property.getProperty("limit") != null ? (String)property.getProperty("limit") : null;
		this.dbType = property.getProperty("dbType") != null ? (String)property.getProperty("dbType") : null;
		
		if (dbType == null) {
			throw new RuntimeException("DB Type should not be null, please check your configutation");
		}
		if (offset == null || limit == null) {
			throw new RuntimeException("Offset or limit should not be null, please check your configutation");				
		}
	}
	

	
	
	/**
	RowBounds rb = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
	LOGGER.debug("RowBounds = {}", rb);

	if (rb == null || rb == RowBounds.DEFAULT) {
		return invocation.proceed();
	}
	
	Check rowbound
	StringBuffer sb = new StringBuffer();
	sb.append(originalSql);
	sb.append(" limit ");
	sb.append(rb.getOffset());
	sb.append(", ");
	sb.append(rb.getLimit());

	logger.debug("sql = {}", sb.toString());

	RowBounds 정보 제거
	metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
	metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);		
	**/	

}
