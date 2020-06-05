package com.blog4java.mybatis.example;

import com.alibaba.fastjson.JSON;
import com.blog4java.common.DbUtils;
import com.blog4java.mybatis.example.entity.UserEntity;
import com.blog4java.mybatis.example.mapper.UserMapper;
import com.blog4java.mybatis.example.query.UserQuery;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PageInterceptorExample {

    private UserMapper userMapper;

    private SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        DbUtils.initData();
        // 获取配置文件输入流
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 通过SqlSessionFactoryBuilder的build()方法创建SqlSessionFactory实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);


        // 初始化依赖
        // 调用openSession()方法创建SqlSession实例
        sqlSession = sqlSessionFactory.openSession();
        // 获取UserMapper代理对象
        userMapper = sqlSession.getMapper(UserMapper.class);
    }


    /**
     * {@link org.apache.ibatis.builder.xml.XMLConfigBuilder#pluginElement(XNode)} 创建过程,解析琏表配置
     * {@link org.apache.ibatis.session.Configuration#addInterceptor(Interceptor)} 调用连设计模式,管理琏表
     *
     *
     * <p>
     * 指令所有插件的plugin方法,生成代理类
     * {@link org.apache.ibatis.session.Configuration#newExecutor(Transaction)}
     * {@link org.apache.ibatis.session.Configuration#newParameterHandler(MappedStatement, Object, BoundSql)}}
     * {@link org.apache.ibatis.session.Configuration#newResultSetHandler(Executor, MappedStatement, RowBounds, ParameterHandler, ResultHandler, BoundSql)}}
     * {@link org.apache.ibatis.session.Configuration#newStatementHandler(Executor, MappedStatement, Object, RowBounds, ResultHandler, BoundSql) }
     * <p>
     * <p>
     * {@link org.apache.ibatis.plugin.Plugin} 公共的代理逻辑的
     */
    @Test
    public void testPageInterceptor() {
        // 设置查询参数
        UserQuery query = new UserQuery();
        query.setPageSize(5);
        query.setFull(true);
        // 返回查询结果
        List<UserEntity> users = userMapper.getUserPageable(query);
        System.out.println("总数据量：" + query.getTotalCount() + ",总页数："
                + query.getTotalPage() + "，当前查询数据：" + JSON.toJSONString(users));
    }


}
