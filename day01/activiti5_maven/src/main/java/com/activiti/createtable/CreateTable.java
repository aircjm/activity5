package com.activiti.createtable;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * Created by jiaming.chen1 on 2015/11/24.
 *
 * 两种方式创建activiti数据库表
 */
public class CreateTable {
    @Test
    public void createActivityTable(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();

        //设置链接参数
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti5?useUnicode=true&characterEncoding=utf8");
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

        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);

        //创建工作流引擎
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);

    }


    @Test
    public void testCreateTableFromResource(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }
}
