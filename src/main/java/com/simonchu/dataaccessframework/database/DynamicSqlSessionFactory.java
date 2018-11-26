package com.simonchu.dataaccessframework.database;

import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 动态mybatis session 连接工厂
 *
 * @author zhaozan.chu
 * @date   2018/11/7 13:47
 *
 */
public class DynamicSqlSessionFactory implements SqlSessionFactory {
    private final AtomicReference<SqlSessionFactory> sqlSessionFactoryAtomicReference;

    public DynamicSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        sqlSessionFactoryAtomicReference = new AtomicReference<>(sqlSessionFactory);
    }

    public SqlSessionFactory setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        return sqlSessionFactoryAtomicReference.getAndSet(sqlSessionFactory);
    }

    @Override
    public SqlSession openSession() {
        return sqlSessionFactoryAtomicReference.get().openSession();
    }

    @Override
    public SqlSession openSession(boolean b) {
        return sqlSessionFactoryAtomicReference.get().openSession(b);
    }

    @Override
    public SqlSession openSession(Connection connection) {
        return sqlSessionFactoryAtomicReference.get().openSession(connection);
    }

    @Override
    public SqlSession openSession(TransactionIsolationLevel transactionIsolationLevel) {
        return sqlSessionFactoryAtomicReference.get().openSession(transactionIsolationLevel);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType) {
        return sqlSessionFactoryAtomicReference.get().openSession(executorType);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, boolean b) {
        return sqlSessionFactoryAtomicReference.get().openSession(executorType,b);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel transactionIsolationLevel) {
        return sqlSessionFactoryAtomicReference.get().openSession(executorType,transactionIsolationLevel);
    }

    @Override
    public SqlSession openSession(ExecutorType executorType, Connection connection) {
        return sqlSessionFactoryAtomicReference.get().openSession(executorType,connection);
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSessionFactoryAtomicReference.get().getConfiguration();
    }
}
