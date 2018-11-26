package com.simonchu.dataaccessframework.database;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Mybatis 拦截器，负责拦截Mybatis 执行sql 情况
 * 
 * @author zhaozan.chu
 * @date 2018/10/17 19:50
 *
 */
@Component
@Intercepts(value = {
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class MybatisInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

	@SuppressWarnings("unused")
	private Properties properties;

	/**
	 * 拦截Sql执行情况
	 * 
	 * @author zhaozan.chu
	 * @date 2018/10/17 19:52
	 *
	 * @param invocation
	 *            调用者
	 * @return
	 * @throws Throwable
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue = null;
		long start = System.currentTimeMillis();
		returnValue = invocation.proceed();
		long end = System.currentTimeMillis();
		long time = (end - start);
		if (time > 1) {
			String sql = getSql(configuration, boundSql, sqlId, time);
			logger.info(sql);

		}
		return returnValue;
	}

	/**
	 * 从Mybatis 中获取执行的SQL信息
	 * 
	 * @author zhaozan.chu
	 * @date 2018/10/17 19:56
	 *
	 * @param configuration
	 * @param boundSql
	 * @param sqlId
	 * @param time
	 * @return
	 */
	public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
		String sql = showSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append(sqlId);
		str.append(":");
		str.append(sql);
		str.append(":");
		str.append(time);
		str.append("ms");
		return str.toString();
	}

	/**
	 * 获取SQL 参数信息
	 * 
	 * @author zhaozan.chu
	 * @date 2018/10/17 20:10
	 *
	 * @param obj
	 * @return
	 */
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}

		}
		return value;
	}
	
	/**
	 * 正则表达式转义
	 * @author zhaozan.chu
	 * @date   2018/10/17 20:15
	 *
	 * @param keyword
	 * @return
	 */
	private static String escapeExprSpecialWord(String keyword) {  
	    if (!StringUtils.isEmpty(keyword)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	} 

	/**
	 * 显示SQL语句
	 * 
	 * @author zhaozan.chu
	 * @date 2018/10/17 20:17
	 *
	 * @param configuration
	 * @param boundSql
	 * @return
	 */
	public static String showSql(Configuration configuration, BoundSql boundSql) {
		try {
			Object parameterObject = boundSql.getParameterObject();
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
			if (parameterMappings.size() > 0 && parameterObject != null) {
				TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
				if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
					sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

				} else {
					MetaObject metaObject = configuration.newMetaObject(parameterObject);
					for (ParameterMapping parameterMapping : parameterMappings) {
						String propertyName = parameterMapping.getProperty();
						if (metaObject.hasGetter(propertyName)) {
							Object obj = metaObject.getValue(propertyName);
							try {
								sql = sql.replaceFirst("\\?", escapeExprSpecialWord(getParameterValue(obj)));
							} catch (Exception ex) {

							}
						} else if (boundSql.hasAdditionalParameter(propertyName)) {
							Object obj = boundSql.getAdditionalParameter(propertyName);
							sql = sql.replaceFirst("\\?", getParameterValue(obj));
						}
					}
				}
			}
			return sql;
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return "";
		}
	}

	/**
	 * 拦截插件
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	/**
	 * 配置属性
	 */
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
