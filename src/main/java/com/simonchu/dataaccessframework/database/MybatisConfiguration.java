package com.simonchu.dataaccessframework.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * generator 配置
 *
 * @author zhaozan.chu
 * @date   2018/11/7 9:57
 *
 */
@Configuration
@AutoConfigureAfter({MybatisManager.class})
public class MybatisConfiguration {
    @Autowired
    private MybatisManager mybatisManager;

    @Bean(name="sqlSessionFactory")
    public DynamicSqlSessionFactory sqlSessionFactory() throws Exception {
        org.apache.ibatis.session.Configuration configuration = mybatisManager.buildMybatisConfig();
        SqlSessionFactory sqlSessionFactory = mybatisManager.buildSqlSessionFactory(configuration);
        return new DynamicSqlSessionFactory(sqlSessionFactory);
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer(){
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                //这里表示开启驼峰命名
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
