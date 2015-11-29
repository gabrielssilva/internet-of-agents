package com.internetofagents.agents;

import jade.core.BaseService;
import jade.core.Profile;
import jade.core.ServiceException;
import jade.core.messaging.MessagingService;

public class OurMessagingService extends MessagingService {
	
	/*
	// Service name
	public static final String NAME =
	"com.internetofagents.agents.OurMessagingService";
	
	// Service parameter names
	public static final String VERBOSE =
	"com_internetofagents_agents_OurMessagingService";
	
	private boolean verbose = false;
*/
	
	 public void boot(Profile p) throws ServiceException {
		 super.boot(p);
		 //verbose = p.getBooleanProperty(VERBOSE, false);
		 System.out.println("VERBOSE = ");
	 }
	
	 @Override
	 public String getName() {
		 // TODO Auto-generated method stub
		 return NAME;
	 }
}
