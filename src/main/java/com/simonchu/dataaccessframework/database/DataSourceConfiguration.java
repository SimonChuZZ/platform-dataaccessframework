package com.simonchu.dataaccessframework.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author zhaozan.chu
 * @date   2018/10/17 20:45
 *
 */
@Configuration
public class DataSourceConfiguration {
    @Autowired
    private DataSourceManager dataSourceManager;


    @Bean("dataSource")
    public DynamicDataSource dataSource(){
        DataSource dataSource = dataSourceManager.buildDataSource();
        return new DynamicDataSource(dataSource);
    }


    /**
     * Mybatis 事务管理器
     *
     * @author zhaozan.chu
     * @date 2018/10/17 20:15
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        if(null == dataSource){
            return null;
        }

        return new DataSourceTransactionManager(dataSource);
    }
}
