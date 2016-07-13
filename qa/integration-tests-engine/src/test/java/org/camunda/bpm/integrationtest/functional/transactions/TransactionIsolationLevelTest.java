package org.camunda.bpm.integrationtest.functional.transactions;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.integrationtest.util.AbstractFoxPlatformIntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class TransactionIsolationLevelTest extends AbstractFoxPlatformIntegrationTest {

  @Deployment
  public static WebArchive processArchive() {
    return initWebArchiveDeployment();
  }

  @Inject
  private ProcessEngine processEngine;

  @Test
  public void testTransactionIsolationLevelOnConnection() {
    ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
    SqlSession sqlSession = processEngineConfiguration.getDbSqlSessionFactory()
        .getSqlSessionFactory()
        .openSession();
    try {
      int transactionIsolation = sqlSession.getConnection().getTransactionIsolation();
      assertEquals("TransactionIsolationLevel for connection is " + transactionIsolation + " instead of " + TransactionIsolationLevel.READ_COMMITTED,
          TransactionIsolationLevel.READ_COMMITTED.getLevel(), transactionIsolation);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
