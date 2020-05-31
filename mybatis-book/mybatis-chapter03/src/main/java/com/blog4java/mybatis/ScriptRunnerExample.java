package com.blog4java.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class ScriptRunnerExample {

    @Test
    public void testScriptRunner() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:mybatis",
                    "sa", "");
            // 脚本执行语法
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            // 这里可以设置属性

            scriptRunner.runScript(Resources.getResourceAsReader("create-table.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
