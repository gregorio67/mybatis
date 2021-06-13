package com.dymn.mybatis.dynamic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.dymn.mybatis.dynamic.builder.DynamicSqlBuilder;

public interface SelectMapper {

	@SelectProvider(type = DynamicSqlBuilder.class, method = "buildSql")
	public Map<String, Object> seelctSql(@Param("unboundSql")  String unboundSql, @Param("sqlParams") List<Object> sqlParams);

	@SelectProvider(type = DynamicSqlBuilder.class, method = "buildSql")
	public List<Map<String, Object>> seelctListSql(@Param("unboundSql")  String unboundSql, @Param("sqlParams") List<Object> sqlParams);

}
