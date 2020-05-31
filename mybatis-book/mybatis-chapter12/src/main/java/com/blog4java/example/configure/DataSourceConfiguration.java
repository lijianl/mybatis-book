package com.blog4java.example.configure;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

// mybatis-spring

/**
 * javaConfig加载数据源
 */

@Configuration

/**
 * {@link MapperScan}注解扫描Mapper接口和xml文件, 并生成代理注入到spring
 *
 * 这是Mybatis与spring结合的入口
 *
 *{@link MapperScan 加载配置}
 *{@link MapperScannerRegistrar 扫描配置,生成并修改BeanDefinition,注入BeanDefinitionRegistry}
 *{@link MapperFactoryBean} 生成Mapper代理
 */
@MapperScan(basePackages = {"com.blog4java.example.mapper"},
        sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfiguration {

    /**
     * 因为使用了{@link @EnableAutoConfiguration}启动了spring-boot的自带的DataSource配置
     * 需要使用{@link @Primary}指定加载的优先级
     */
    @Bean(name = "dataSource")
    @Primary
    public DataSource setDataSource() {
        // => 创建数据源Bean，并执行数据库脚本
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("create-table-c12.sql")
                .addScript("init-data-c12.sql")
                .build();
    }

    /**
     * 1. 结合的第一个地方
     * mybatis-spring使用{@link SqlSessionFactoryBean} 创建 {@link SqlSessionFactory}
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        // 通过Mybatis Spring模块提供的SqlSessionFactoryBean
        // 创建Mybatis的SqlSessionFactory对象
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:com/blog4java/example/mapper/*.xml"));
        return bean.getObject();
    }

    /**
     * 2. 结合的第二个地方
     * 使用{@link SqlSessionTemplate } 实现session管理
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        // 创建Mybatis Spring模块中的SqlSessionTemplate对象
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 申明事务使用
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager setTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    /**
     * 编程事务使用
     * <p>
     * {@link org.apache.ibatis.transaction.Transaction}  mybatis事务
     * {@link org.apache.ibatis.transaction.jdbc.JdbcTransaction} mybatis自己通过JDBC管理事务,自己管理connection
     * {@link org.apache.ibatis.transaction.managed.ManagedTransaction} 第三方管理事务
     * {@link org.springframework.jdbc.datasource.DataSourceUtils#getConnection }Spring自己获取Connection的方法
     * {@link org.mybatis.spring.transaction.SpringManagedTransaction} Mybtais结合Spring,获取Connecton使用同一个方法
     *
     * @param transactionManager
     * @return
     */
    @Bean
    @Primary
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") DataSourceTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
