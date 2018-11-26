package com.simonchu.dataaccessframework.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 配置
 *
 * @author zhaozan.chu
 * @date   2018/11/6 16:36
 *
 */
@Configuration
public class MybatisProperties {
    @Value("${mybatis.mapper-location:null}")
    private String mapperLocation;

    @Value("${mybatis.type-aliases-package:null}")
    private String typeAliasesPackage;

    @Value("${mybatis.configuration.lazyLoadingEnabled:false}")
    private boolean lazyLoadingEnabled;

    @Value("${mybatis.configuration.aggressiveLazyLoading:false}")
    private boolean aggressiveLazyLoading;

    @Value("${mybatis.configuration.cacheEnabled:false}")
    private boolean cacheEnabled;

    @Value("${mybatis.configuration.multipleResultSetsEnabled:false}")
    private boolean multipleResultSetsEnabled;

    @Value("${mybatis.configuration.useColumnLabel:false}")
    private boolean useColumnLabel;

    @Value("${mybatis.configuration.map-underscore-to-camel-case:true}")
    private boolean mapUnderscoreToCamelCase;

    @Value("${mapper.mappers:null}")
    private String mappers;

    @Value("${mapper.not-empty:null}")
    private boolean notEmpty;

    @Value("${mapper.identity:MYSQL}")
    private String identity;

    @Value("${mapper.basePackage:null}")
    private String basePackage;

    public String getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public boolean isLazyLoadingEnabled() {
        return lazyLoadingEnabled;
    }

    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    public boolean isAggressiveLazyLoading() {
        return aggressiveLazyLoading;
    }

    public void setAggressiveLazyLoading(boolean aggressiveLazyLoading) {
        this.aggressiveLazyLoading = aggressiveLazyLoading;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public boolean isMultipleResultSetsEnabled() {
        return multipleResultSetsEnabled;
    }

    public void setMultipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
        this.multipleResultSetsEnabled = multipleResultSetsEnabled;
    }

    public boolean isUseColumnLabel() {
        return useColumnLabel;
    }

    public void setUseColumnLabel(boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public String getMappers() {
        return mappers;
    }

    public void setMappers(String mappers) {
        this.mappers = mappers;
    }

    public boolean isNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public String toString() {
        return "MybatisProperties{" +
                "mapperLocation='" + mapperLocation + '\'' +
                "typeAliasesPackage='" + typeAliasesPackage + '\'' +
                ", lazyLoadingEnabled=" + lazyLoadingEnabled +
                ", aggressiveLazyLoading=" + aggressiveLazyLoading +
                ", cacheEnabled=" + cacheEnabled +
                ", multipleResultSetsEnabled=" + multipleResultSetsEnabled +
                ", useColumnLabel=" + useColumnLabel +
                ", mapUnderscoreToCamelCase=" + mapUnderscoreToCamelCase +
                ", mappers='" + mappers + '\'' +
                ", notEmpty=" + notEmpty +
                ", identity='" + identity + '\'' +
                ", basePackage='" + basePackage + '\'' +
                '}';
    }
}
