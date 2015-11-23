package cn.itcast.sh05.jbpm.db;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class CreateTable {
	@Test
	public void testCreateTable(){
		Configuration configuration = new Configuration();
		configuration.configure("jbpm.hibernate.cfg.xml");
		configuration.buildSessionFactory();
	}
}
