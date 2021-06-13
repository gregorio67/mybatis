package com.dymn.mybatis.dynamic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import com.dymn.mybatis.dynamic.builder.DynamicSqlBuilder;

public interface DeleteMapper {

	@DeleteProvider(type = DynamicSqlBuilder.class, method = "buildSql")
	public int deleteSql(@Param("unboundSql")  String unboundSql, @Param("sqlParams") List<Object> sqlParams);
}
