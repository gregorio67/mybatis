package com.dymn.mybatis.dynamic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import com.dymn.mybatis.dynamic.builder.DynamicSqlBuilder;

public interface InsertMapper {

	@InsertProvider(type = DynamicSqlBuilder.class, method = "buildSql")
	public int insertSql(@Param("unboundSql")  String unboundSql, @Param("sqlParams") List<Object> sqlParams);
}
