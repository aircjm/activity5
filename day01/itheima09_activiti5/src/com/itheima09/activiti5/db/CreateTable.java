package com.itheima09.activiti5.db;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class CreateTable {
	@Test
	public void testCreateTable() {
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();
		// 数据库配置
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration.setJdbcUrl("jdbc:mysql://localhost:3306/itheima09_activiti5?useUnicode=true&characterEncoding=utf8");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");

		// 通过该配置，用来创建Activiti的23张表
		/**
		 * public static final String DB_SCHEMA_UPDATE_FALSE =
		 * "false";//表示不能自动创建表，证明表是存在的
		 * 
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP =
		 * "create-drop";先删除表，再创建表
		 * 
		 * public static final String DB_SCHEMA_UPDATE_TRUE =
		 * "true";//表示如果表不存在，就会自动创建表
		 */
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// 创建工作流的流程引擎(从而创建数据库的23张表)
		ProcessEngine processEngine = configuration.buildProcessEngine();
		System.out.println(processEngine);
	}

	@Test
	public void testCreateTableFromResource() {
		ProcessEngine processEngine = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml")
				.buildProcessEngine();
		System.out.println(processEngine);
	}
}
