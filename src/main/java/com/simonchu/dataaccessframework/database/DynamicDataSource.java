package com.simonchu.dataaccessframework.database;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * 动态数据源
 *
 * @author zhaozan.chu
 * @date   2018/11/5 12:09
 *
 */
public class DynamicDataSource implements DataSource {
    private final AtomicReference<DataSource> dataSourceAtomicReference;

    public DynamicDataSource(DataSource dataSource){
        dataSourceAtomicReference = new AtomicReference<>(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSourceAtomicReference.get().getConnection();
    }

    /**
     * 设置数据源
     * @param dataSource
     * @return
     */
    public DataSource setDataSource (DataSource dataSource){
        return dataSourceAtomicReference.getAndSet(dataSource);
    }

    /**
     * 关闭数据源
     * @throws SQLException
     */
    public void close() throws SQLException {
        dataSourceAtomicReference.get().getConnection().close();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return dataSourceAtomicReference.get().getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSourceAtomicReference.get().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSourceAtomicReference.get().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSourceAtomicReference.get().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSourceAtomicReference.get().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSourceAtomicReference.get().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSourceAtomicReference.get().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSourceAtomicReference.get().getParentLogger();
    }
}
