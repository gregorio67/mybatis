package com.dymn.mybatis.dynamic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import com.dymn.mybatis.dynamic.builder.DynamicSqlBuilder;

public interface UpdateMapper {

	@UpdateProvider(type = DynamicSqlBuilder.class, method="buildSql")
	public int updateSql(@Param("unboundSql")  String unboundSql, @Param("sqlParams") List<Object> sqlParams);
}
