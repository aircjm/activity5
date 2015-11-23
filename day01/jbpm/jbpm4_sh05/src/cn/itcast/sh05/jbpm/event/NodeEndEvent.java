package cn.itcast.sh05.jbpm.event;

import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

public class NodeEndEvent implements EventListener{

	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("node end events");
	}

}
