package com.simonchu.dataaccessframework.database;

import com.alibaba.druid.pool.DruidDataSource;
import com.simonchu.dataaccessframework.constant.CommonConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源管理器
 *
 * @author zhaozan.chu
 * @date   2018/11/5 14:34
 *
 */
@Component
public class DataSourceManager {
    private Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * 构建数据源
     */
    public DataSource buildDataSource(){
        if(dataSourceProperties == null) {
            return null;
        }else{
            return druidDataSource(dataSourceProperties);
        }
    }

    private DruidDataSource druidDataSource(DataSourceProperties dataSourceProperties) {
        if (null == dataSourceProperties || CommonConstant.NULL.equals(dataSourceProperties.getDbUrl())
                || CommonConstant.NULL.equals(dataSourceProperties.getUsername())
                || CommonConstant.NULL.equals(dataSourceProperties.getDriverClassName())) {
            return null;
        }

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getDbUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        try {
            datasource.setFilters(dataSourceProperties.getFilters());
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }

        return datasource;
    }
}
