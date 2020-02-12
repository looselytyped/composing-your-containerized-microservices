package com.looselytyped.liquibase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import liquibase.exception.LiquibaseException;

public class Starter {

  private static final Logger logger = LogManager.getLogger(Starter.class);
  private static final String CONFIG_FILE_LOCATION = "/liquibase.properties";

  public static void main(String args[])
      throws LiquibaseException, ParserConfigurationException, IOException, SQLException {
    long startTime = System.currentTimeMillis();

    InputStream is = Starter.class.getResourceAsStream(CONFIG_FILE_LOCATION);

    Properties properties = new Properties();
    properties.load(is);

    // look for CLI supplied System properties and override the defaults
    for (Object key : properties.keySet()) {
      String override = System.getProperty((String) key);
      if (override != null) {
        properties.put(key, override);
      }
    }

    String jdbcConnUrl = properties.getProperty("jdbcConnUrl");
    String dbUserName = properties.getProperty("dbUserName");
    String dbPassword = properties.getProperty("dbPassword");
    String dbName = properties.getProperty("dbName");
    String task = properties.getProperty("task");
    String tag = properties.getProperty("tag");
    String count = properties.getProperty("count");

    Handler handler = new Handler();

    if ("diff".equalsIgnoreCase(task)) {
      String referencejdbcConnUrl = properties.getProperty("referencejdbcConnUrl");
      String referencedbUserName = properties.getProperty("referencedbUserName");
      String referencedbPassword = properties.getProperty("referencedbPassword");

      handler.diffLog(referencejdbcConnUrl, referencedbUserName, referencedbPassword, jdbcConnUrl, dbUserName,
          dbPassword);
    } else {
      handler.initiateLiquibase(jdbcConnUrl, dbUserName, dbPassword, dbName, task, tag, count);
    }

    logger.warn("Time taken to complete the liquibase updation : " + (System.currentTimeMillis() - startTime) / 1000
        + " seconds.");
  }
}
