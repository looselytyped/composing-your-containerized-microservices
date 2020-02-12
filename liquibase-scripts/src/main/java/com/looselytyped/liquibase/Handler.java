package com.looselytyped.liquibase;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.mariadb.jdbc.MariaDbDataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

public class Handler {

  private String connectionProperty;
  private String dbChangeLog = "users/db-changelog-users.json";

  public String getDbChangeLog() {
    return dbChangeLog;
  }

  public String getConnectionProperty() {
    return connectionProperty;
  }

  public void setConnectionProperty(String connectionProperty) {
    this.connectionProperty = connectionProperty;
  }

  public void initiateLiquibase(String jdbcConnUrl, String dbUserName, String dbPassword, String dbName, String task,
      String tag, String count) throws LiquibaseException, SQLException {
    syncChangeLog(dataSource(jdbcConnUrl, dbUserName, dbPassword), task, tag, count);
  }

  private DataSource dataSource(String jdbcConnUrl, String dbUserName, String dbPassword) throws SQLException {
    MariaDbDataSource mysqlDS = null;
    mysqlDS = new MariaDbDataSource();
    mysqlDS.setUrl(jdbcConnUrl);
    mysqlDS.setUser(dbUserName);
    mysqlDS.setPassword(dbPassword);
    return mysqlDS;
  }

  private void syncChangeLog(DataSource ds, String task, String tag, String count)
      throws LiquibaseException, SQLException {
    Liquibase liquibase = null;
    try {
      ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());
      Database db = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(ds.getConnection()));
      liquibase = new Liquibase("liquibase/" + getDbChangeLog(), resourceAccessor, db);

      if ("tagDatabase".equalsIgnoreCase(task) && (!liquibase.tagExists(tag))) {
        liquibase.tag(tag);
      }
      if ("rollback".equalsIgnoreCase(task)) {
        if (tag == null && count != null) {
          liquibase.rollback(Integer.parseInt(count), "");
        } else if (liquibase.tagExists(tag)) {
          liquibase.rollback(tag, "");
        }
      }
      if ("update".equalsIgnoreCase(task)) {
        if (liquibase.tagExists(tag)) {
          liquibase.rollback(tag, "");
        }
        liquibase.update(tag, "");
      }
    } finally {
      if (liquibase != null) {
        liquibase.forceReleaseLocks();
      }
    }
  }

  public void diffLog(String referencejdbcConnUrl, String referencedbUserName, String referencedbPassword,
      String jdbcConnUrl, String dbUserName, String dbPassword)
      throws LiquibaseException, ParserConfigurationException, IOException, SQLException {
    Liquibase liquibase = null;
    try {
      DataSource targetDS = dataSource(jdbcConnUrl, dbUserName, dbPassword);
      DataSource referenceDS = dataSource(referencejdbcConnUrl, referencedbUserName, referencedbPassword);

      Database referenceDatabase = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(referenceDS.getConnection()));
      Database targetDatabase = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(targetDS.getConnection()));

      liquibase = new Liquibase("", new FileSystemResourceAccessor(), referenceDatabase);
      DiffResult diffResult = liquibase.diff(referenceDatabase, targetDatabase, new CompareControl());
      new DiffToChangeLog(diffResult, new DiffOutputControl()).print("diff.xml");
    } finally {
      if (liquibase != null) {
        liquibase.forceReleaseLocks();
      }
    }
  }
}
