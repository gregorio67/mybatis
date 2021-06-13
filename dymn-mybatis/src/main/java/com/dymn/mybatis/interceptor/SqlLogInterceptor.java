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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dymn.mybatis.context.ExecuteService;
import com.dymn.mybatis.context.ServiceContext;

//@Component
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlLogInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlLogInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object[] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement)args[0]; 
		/** SQL parameter **/
		Object param = (Object)args[1]; 
		BoundSql boundSql = mappedStatement.getBoundSql(param);
				
		String originalSql = boundSql.getSql();
		String bindSql = originalSql;
		String sqlId = mappedStatement.getId();
		

		if (param == null) {
			bindSql = bindSql.replaceFirst("\\?", "''");
		} else {
			if (param instanceof Integer || param instanceof Long || param instanceof Float	|| param instanceof Double) {
				bindSql = bindSql.replaceFirst("\\?", param.toString());
			} else if (param instanceof String) {
				bindSql = bindSql.replaceFirst("\\?", "'" + param + "'");
			} else if (param instanceof Map) {

				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();

				for (ParameterMapping mapping : paramMapping) {
					String propValue = mapping.getProperty();
					Object value = ((Map<?,?>) param).get(propValue);
					if (value instanceof String) {
						bindSql = bindSql.replaceFirst("\\?", "'" + value + "'");
					} else {
						bindSql = bindSql.replaceFirst("\\?", value.toString());
					}

				}
			} else {

				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();

				Class<? extends Object> paramClass = param.getClass();
				for (ParameterMapping mapping : paramMapping) {
					String propValue = mapping.getProperty();
					Field field = paramClass.getDeclaredField(propValue);
					field.setAccessible(true);
					Class<?> javaType = mapping.getJavaType();

					if (String.class == javaType) {
						bindSql = bindSql.replaceFirst("\\?", "'" + field.get(param) + "'");
					} else {
						bindSql = bindSql.replaceFirst("\\?", field.get(param).toString());
					}

				}
			}

		}

		List<ExecuteService> services = ServiceContext.getContext().getExecuteServices();
		int size = services != null ? services.size() : -1;
		
		ExecuteService executeService = size > 0 ? services.get(size - 1) : null;
		if (executeService != null) {
			List<String> originalSqls = executeService.getOriginalSqls() != null ? executeService.getOriginalSqls() : new ArrayList<>();
			List<String> bindSqls = executeService.getBindSqls() != null ? executeService.getBindSqls() : new ArrayList<>();
			List<String> sqlIds = executeService.getSqlIds() != null ? executeService.getSqlIds() : new ArrayList<>();
			
			originalSqls.add(originalSql);
			bindSqls.add(bindSql);
			sqlIds.add(sqlId);
			executeService.setOriginalSqls(originalSqls);
			executeService.setBindSqls(bindSqls);
			executeService.setSqlIds(sqlIds);
			
			services.set(size -1, executeService);
			
		}
		
		
		LOGGER.debug("=====================================================================");
		LOGGER.debug("original sql : {}", originalSql);
		LOGGER.debug("bind sql : {}", bindSql);
		LOGGER.debug("=====================================================================");

		return invocation.proceed(); 
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties property) {

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
