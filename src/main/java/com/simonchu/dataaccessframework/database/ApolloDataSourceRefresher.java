package com.simonchu.dataaccessframework.database;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Apollo 数据源配置
 *
 * @author zhaozan.chu
 * @date   2018/11/2 15:13
 *
 */
@Component
public class ApolloDataSourceRefresher {
    private final Logger logger = LoggerFactory.getLogger(ApolloDataSourceRefresher.class);

    @Autowired
    private DataSourceProperties dataSourceConfiguration;

    /**
     * 动态数据源
     */
    @Autowired
    private DynamicDataSource dataSource;

    @Autowired
    private DataSourceManager dataSourceManager;

    private final String dbUrl ="spring.datasource.url";
    private final String userName ="spring.datasource.username";
    private final String password ="spring.datasource.password";
    private final String driverClassName ="spring.datasource.driver-class-name";
    private final String initialSize ="spring.datasource.initialSize";
    private final String minIdle ="spring.datasource.minIdle";
    private final String maxActive ="spring.datasource.maxActive";
    private final String maxWait ="spring.datasource.maxWait";
    private final String minEvictableIdleTimeMillis ="spring.datasource.minEvictableIdleTimeMillis";
    private final String timeBetweenEvictionRunsMillis ="spring.datasource.timeBetweenEvictionRunsMillis";
    private final String validationQuery ="spring.datasource.validationQuery";
    private final String testWhileIdle = "spring.datasource.testWhileIdle";
    private final String testOnBorrow ="spring.datasource.testOnBorrow";
    private final String testOnReturn ="spring.datasource.testOnReturn";
    private final String filters ="spring.datasource.filters";
    private final String logSlowSql ="spring.datasource.logSlowSql";

    @ApolloConfigChangeListener
    private void changeHanlder(ConfigChangeEvent changeEvent) {
        boolean isDatabaseChanged = false;
        for (String key : changeEvent.changedKeys()) {
            if(!isDatabaseChanged) {
                if (key.startsWith("spring.datasource.")) {
                    isDatabaseChanged = true;
                }
            }
            if (key.equals(dbUrl)) {
                dataSourceConfiguration.setDbUrl(changeEvent.getChange(dbUrl).getNewValue());
            }else if (key.equals(userName)) {
                dataSourceConfiguration.setUsername(changeEvent.getChange(userName).getNewValue());
            }else if (key.equals(password)){
                dataSourceConfiguration.setPassword(changeEvent.getChange(password).getNewValue());
            }else if(key.equals(driverClassName)){
                dataSourceConfiguration.setDriverClassName(changeEvent.getChange(driverClassName).getNewValue());
            }else if (key.equals(initialSize)){
                int i= Integer.parseInt(changeEvent.getChange(initialSize).getNewValue());
                dataSourceConfiguration.setInitialSize(i);
            }else if (key.equals(minIdle)){
                int i= Integer.parseInt(changeEvent.getChange(minIdle).getNewValue());
                dataSourceConfiguration.setMinIdle(i);
            }else if (key.equals(maxActive)){
                int i= Integer.parseInt(changeEvent.getChange(maxActive).getNewValue());
                dataSourceConfiguration.setMaxActive(i);
            }else if (key.equals(maxWait)){
                int i= Integer.parseInt(changeEvent.getChange(maxWait).getNewValue());
                dataSourceConfiguration.setMaxWait(i);
            }else if (key.equals(minEvictableIdleTimeMillis)){
                int i= Integer.parseInt(changeEvent.getChange(minEvictableIdleTimeMillis).getNewValue());
                dataSourceConfiguration.setMinEvictableIdleTimeMillis(i);
            }else if (key.equals(timeBetweenEvictionRunsMillis)){
                int i= Integer.parseInt(changeEvent.getChange(timeBetweenEvictionRunsMillis).getNewValue());
                dataSourceConfiguration.setTimeBetweenEvictionRunsMillis(i);
            }else if (key.equals(validationQuery)){
                dataSourceConfiguration.setValidationQuery(changeEvent.getChange(validationQuery).getNewValue());
            }else if (key.equals(testWhileIdle)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(testWhileIdle).getNewValue());
                dataSourceConfiguration.setTestWhileIdle(b);
            }else if (key.equals(testOnBorrow)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(testOnBorrow).getNewValue());
                dataSourceConfiguration.setTestOnBorrow(b);
            }else if (key.equals(testOnReturn)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(testOnReturn).getNewValue());
                dataSourceConfiguration.setTestOnReturn(b);
            }else if (key.equals(filters)){
                dataSourceConfiguration.setFilters(changeEvent.getChange(filters).getNewValue());
            }else if (key.equals(logSlowSql)){
                dataSourceConfiguration.setFilters(changeEvent.getChange(logSlowSql).getNewValue());
            }
        }

        if(isDatabaseChanged){
            // 刷新数据源
            logger.info("开始刷新数据源");
            DataSource temp = dataSourceManager.buildDataSource();
            // 如果当前数据源正常，则切换数据源
            if(dataSource == null){
                this.dataSource = new DynamicDataSource(temp);
            }else{
                // 停止原来的连接池
                try {
                    this.dataSource.close();
                    //切换
                    this.dataSource.setDataSource(temp);
                    logger.info("成功切换数据源:"+ dataSourceConfiguration.getDbUrl());
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("关闭老数据源出错："+ e.getMessage());
                    try {
                        temp.getConnection().close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        logger.error("关闭临时数据源出错："+ e1.getMessage());
                    }
                }

            }
        }
    }


}
