package com.simonchu.dataaccessframework.database;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * generator 配置刷新
 *
 * @author zhaozan.chu
 * @date   2018/11/6 17:30
 *
 */
@Component
public class ApolloMybatisRefresher {
    private final Logger logger = LoggerFactory.getLogger(ApolloMybatisRefresher.class);

    @Autowired
    private MybatisProperties mybatisProperties;

    @Autowired
    private MybatisManager mybatisManager;

    @Autowired
    private DynamicSqlSessionFactory dynamicSqlSessionFactory;

    private final String typeAliasesPackage ="generator.type-aliases-package";
    private final String lazyLoadingEnabled ="generator.configuration.lazyLoadingEnabled";
    private final String aggressiveLazyLoading ="generator.configuration.aggressiveLazyLoading";
    private final String cacheEnabled="generator.configuration.cacheEnabled";
    private final String multipleResultSetsEnabled ="generator.configuration.multipleResultSetsEnabled";
    private final String useColumnLabel ="generator.configuration.useColumnLabel";

    @ApolloConfigChangeListener
    private void changeHanlder(ConfigChangeEvent changeEvent) {
        boolean isMybatisChanged = false;
        for (String key : changeEvent.changedKeys()) {
            if (!isMybatisChanged) {
                if (key.startsWith("generator.")){
                    isMybatisChanged = true;
                }
            }

            if(key.equals(typeAliasesPackage)){
                mybatisProperties.setTypeAliasesPackage(changeEvent.getChange(typeAliasesPackage).getNewValue());
            }else if(key.equals(lazyLoadingEnabled)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(lazyLoadingEnabled).getNewValue());
                mybatisProperties.setLazyLoadingEnabled(b);
            }else if(key.equals(aggressiveLazyLoading)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(aggressiveLazyLoading).getNewValue());
                mybatisProperties.setAggressiveLazyLoading(b);
            }else if(key.equals(cacheEnabled)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(cacheEnabled).getNewValue());
                mybatisProperties.setCacheEnabled(b);
            }else if(key.equals(multipleResultSetsEnabled)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(multipleResultSetsEnabled).getNewValue());
                mybatisProperties.setMultipleResultSetsEnabled(b);
            }else if(key.equals(useColumnLabel)){
                boolean b = Boolean.parseBoolean(changeEvent.getChange(useColumnLabel).getNewValue());
                mybatisProperties.setUseColumnLabel(b);
            }

            if(isMybatisChanged){
                try {
                    // 切换mybatis 配置
                    org.apache.ibatis.session.Configuration configuration = mybatisManager.buildMybatisConfig();
                    SqlSessionFactory sqlSessionFactory = mybatisManager.buildSqlSessionFactory(configuration);
                    dynamicSqlSessionFactory.setSqlSessionFactory(sqlSessionFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
