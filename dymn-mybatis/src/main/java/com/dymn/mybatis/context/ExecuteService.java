package com.dymn.mybatis.context;

import java.util.List;

public class ExecuteService {

	private String className;
	private String methodName;
	private Object[] params;
	private long startTime;
	private long endTime;
	
	private List<String> originalSqls;
	private List<String> bindSqls;
	private List<String> sqlIds;
	

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}


	public List<String> getOriginalSqls() {
		return originalSqls;
	}

	public void setOriginalSqls(List<String> originalSqls) {
		this.originalSqls = originalSqls;
	}

	public List<String> getBindSqls() {
		return bindSqls;
	}

	public void setBindSqls(List<String> bindSqls) {
		this.bindSqls = bindSqls;
	}

	public List<String> getSqlIds() {
		return sqlIds;
	}

	public void setSqlIds(List<String> sqlIds) {
		this.sqlIds = sqlIds;
	}

	

}
