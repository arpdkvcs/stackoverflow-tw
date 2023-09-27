package com.codecool.stackoverflowtw.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;

@Repository
public abstract class BaseDaoJdbc {
  protected final DataSource dataSource;

  @Autowired
  public BaseDaoJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  protected void releaseConnectionIfNoTransaction(Connection currentConnection) {
    if (currentConnection != null
      && !TransactionSynchronizationManager.isActualTransactionActive()) {
      DataSourceUtils.releaseConnection(currentConnection, dataSource);
    }
  }
}
