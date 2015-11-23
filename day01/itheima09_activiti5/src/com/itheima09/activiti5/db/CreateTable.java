package com.itheima09.activiti5.db;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class CreateTable {
	@Test
	public void testCreateTable() {
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();
		// ���ݿ�����
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration
				.setJdbcUrl("jdbc:mysql://localhost:3306/itheima09_activiti5?useUnicode=true&characterEncoding=utf8");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");

		// ͨ�������ã���������Activiti��23�ű�
		/**
		 * public static final String DB_SCHEMA_UPDATE_FALSE =
		 * "false";//��ʾ�����Զ�������֤�����Ǵ��ڵ�
		 * 
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP =
		 * "create-drop";��ɾ�����ٴ�����
		 * 
		 * public static final String DB_SCHEMA_UPDATE_TRUE =
		 * "true";//��ʾ��������ڣ��ͻ��Զ�������
		 */
		configuration
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// ��������������������(�Ӷ��������ݿ��23�ű�)
		ProcessEngine processEngine = configuration.buildProcessEngine();
		System.out.println(processEngine);
	}

	@Test
	public void testCreateTableFromResource() {
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(
						"activiti.cfg.xml")//
				.buildProcessEngine();
		System.out.println(processEngine);
	}
}
