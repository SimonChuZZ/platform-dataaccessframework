package com.simonchu.dataaccessframework.database;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.simonchu.dataaccessframework.constant.CommonConstant;

import javax.sql.DataSource;

/**
 * generator 配置管理
 *
 * @author zhaozan.chu
 * @date   2018/11/6 16:41
 *
 */
@Component
public class MybatisManager {
    @Autowired
    private MybatisProperties mybatisProperties;

    @Autowired
    private MybatisInterceptor mybatisInterceptor;

    @Autowired
    private DataSource dataSource;

    /**
     * 构建mybatis 配置
     * @return
     */
    public org.apache.ibatis.session.Configuration buildMybatisConfig(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLazyLoadingEnabled(mybatisProperties.isLazyLoadingEnabled());
        configuration.setAggressiveLazyLoading(mybatisProperties.isAggressiveLazyLoading());
        configuration.setCacheEnabled(mybatisProperties.isCacheEnabled());
        configuration.setMultipleResultSetsEnabled(mybatisProperties.isMultipleResultSetsEnabled());
        configuration.setUseColumnLabel(mybatisProperties.isUseColumnLabel());
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        return configuration;
    }

    /**
     * 构建mybatis SqlSessionFactory
     * @param configuration 配置
     * @return
     * @throws Exception
     */
    public SqlSessionFactory buildSqlSessionFactory(org.apache.ibatis.session.Configuration configuration) throws Exception {
        if(null == dataSource){
            return null;
        }

        if(CommonConstant.NULL.equals(mybatisProperties.getTypeAliasesPackage())
                || CommonConstant.NULL.equals(mybatisProperties.getMappers())){
            throw new IllegalArgumentException("mybatis.type-aliases-package 和 mapper.mappers 必须配置");
        }

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
        sessionFactory.setConfiguration(configuration);

        // 如果配置了mapperLocation，则可能设置了xml mapper
        if(!CommonConstant.NULL.equals(mybatisProperties.getMapperLocation())){
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources(mybatisProperties.getMapperLocation()));
        }

        sessionFactory.setPlugins(new Interceptor[] { mybatisInterceptor });

        return sessionFactory.getObject();
    }
}
