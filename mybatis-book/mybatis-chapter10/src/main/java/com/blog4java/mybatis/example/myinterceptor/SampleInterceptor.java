package com.blog4java.mybatis.example.myinterceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

/**
 * @author: a002
 * @since: JDK1.8
 * <p>
 * probject: mybatis-book
 * date: 2020/5/31-17:55
 * description:
 */

@Intercepts({})
public class SampleInterceptor implements Interceptor {

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // todo 定义拦截逻辑
        // 执行原本的方法
        return invocation.proceed();
    }

    // 创建代理
    @Override
    public Object plugin(Object target) {
        // 代理
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
