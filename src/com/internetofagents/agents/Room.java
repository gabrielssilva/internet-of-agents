package com.internetofagents.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Room extends Agent {

	private static final long serialVersionUID = -7867936987803586884L;
	
	@Override
	protected void setup() {
        try {
        	// Describe itself
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(this.getAID());
			DFService.register(this, dfd);
			
			this.addBehaviour(new WatchForActionBehavior());
		} catch (FIPAException e) {
			System.out.println("Error registering agent: "+this.getAID());
			e.printStackTrace();
		}
	}
	
	
	private class WatchForActionBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 3733996350261236554L;

		@Override
		public void action() {
			MessageTemplate cfpTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = this.myAgent.receive(cfpTemplate);
			
			if (msg != null) {
				// content = msg.getContent()
				// do something with the content
				// change mode
			}
		}
	}
	
}
