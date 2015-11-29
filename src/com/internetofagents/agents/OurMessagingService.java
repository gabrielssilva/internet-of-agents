package com.internetofagents.agents;

import jade.core.AID;
import jade.core.BaseService;
import jade.core.Filter;
import jade.core.Profile;
import jade.core.ServiceException;
import jade.core.VerticalCommand;
import jade.core.messaging.GenericMessage;
import jade.core.messaging.MessagingSlice;
import jade.lang.acl.ACLMessage;

public class OurMessagingService extends BaseService {
	
	// Service name
	public static final String NAME =
	"com.internetofagents.agents.OurMessagingService";
	
	
	 public void boot(Profile p) throws ServiceException {
		 super.boot(p);
		 System.out.println("Booting the Sniffer service...");
	 }

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public Filter getCommandFilter(boolean direction) {
		System.out.println("Adding filters...");
		return new LoggingFilter();
	}
	
	
	private class LoggingFilter extends Filter {
		public boolean accept(VerticalCommand command) {
			if (command.getName().equals(MessagingSlice.SEND_MESSAGE)) {
				AID sender = (AID) command.getParam(0);
				ACLMessage msg = ((GenericMessage) command.getParam(1)).getACLMessage();
				AID receiver = (AID) command.getParam(2);
				
				System.out.println("Messagem intercepted: - - - - - - - - - - - - - - - -");
				System.out.println("From: "+sender.getName());
				System.out.println("To: "+receiver.getName());
				System.out.println("Content: "+msg.getContent());
				System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - -");
			}
			
			return true;
		}
	}
}
