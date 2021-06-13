package com.dymn.mybatis.dynamic;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.dymn.mybatis.dynamic.builder.DynamicSqlBuilder;

class SqlBuilderTest {

	@Test
	void test()  throws Exception {
		String sql = "INSERT INTO TB_USERS (NAEM, EMAIL, AGE, CODE) values(?, ?, ?, ?)";
		List<Object> sqlParams = Arrays.asList(new Object[] {"홍길동", "kimdoy@gmail.com", 10, 20});
		
		String genSql = DynamicSqlBuilder.buildSql(sql, sqlParams);
		
		System.out.println("Generated SQL :: [" + genSql + "]");
	}

}
