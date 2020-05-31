package com.blog4java.hsqldb;

// 加载脚本

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

// 执行sql脚本
// 执行sql语句
// test
//

public class Example01 {

    private Connection conn = null;

    @Before
    public void initData() {
        try {
            // 加载HSQLDB驱动
            Class.forName("org.hsqldb.jdbcDriver");
            // 获取Connection对象;使用key就可以
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis",
                    "sa", "");
            // 使用Mybatis的ScriptRunner工具类执行数据库脚本
            ScriptRunner scriptRunner = new ScriptRunner(conn);
            scriptRunner.setLogWriter(null);
            // sql脚本执行
            scriptRunner.runScript(Resources.getResourceAsReader("create-table.sql"));
            scriptRunner.runScript(Resources.getResourceAsReader("init-data.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHsqldbQuery() {
        // SqlRunner是Mybatis封装的操作数据库的工具类
        SqlRunner sqlRunner = new SqlRunner(conn);
        try {
            // sql执行工具
            // 调用SqlRunner类的selectAll()方法查询数据
            List<Map<String, Object>> results = sqlRunner.selectAll("select * from user");
            results.forEach(System.out::println);
            // 关闭conn
            sqlRunner.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSQLScript() {
        try {
            // 上层依赖导入
            // 定位resouces文件
            File fsql = Resources.getResourceAsFile("create-table.sql");
            System.out.println(fsql.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
