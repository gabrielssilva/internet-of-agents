package com.mitmattack;

import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.BornAgent;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ManInTheMiddle extends Agent {

	private static final long serialVersionUID = 7179999536677508196L;
	
	@Override
	protected void setup() {
		System.out.println( getLocalName() + " setting up...");
	
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName( getAID() );
		
		this.addBehaviour(new WatchAMSSubscription());
		//this.addBehaviour(new Listen());
		SnifferService.manInTheMiddle = this.getAID();
	    
		try{
	        DFService.register( this, dfd );
	    }
	    catch (Exception e) {
	        System.out.println( "OMG! An Exception: " + e );
	        e.printStackTrace();
	    }
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("person-communication");
		template.addServices(sd);
		
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(this, template);
		} catch (FIPAException e) {
			System.out.println("ManInTheMiddle::setup - No agents found =( :" + e.getMessage());
		} 
		
        for (DFAgentDescription dfAgentDescription : agents) {
        	SnifferService.agentsToWatch.add(dfAgentDescription.getName());
        	System.out.println("Adding agent " + dfAgentDescription.getName().getLocalName() + " to the sniffer service.");
		}
	}

	
	private class WatchAMSSubscription extends AMSSubscriber{

		private static final long serialVersionUID = -3751527678649595117L;

		@Override
		protected void installHandlers(Map handlers) {
			handlers.put(IntrospectionVocabulary.BORNAGENT, new BornAgentEvent());	
		}
		
		private class BornAgentEvent implements EventHandler{

			private static final long serialVersionUID = -1709226224922510054L;

			@Override
			public void handle(Event arg0) {
				BornAgent agent = (BornAgent) arg0;

			
				if(agent.getClassName().contains("Person")){
					SnifferService.agentsToWatch.add(agent.getAgent());
					System.out.println("Adding agent " + agent.getAgent().getLocalName() + " to the sniffer service.");
				}
				
				
				System.out.println("Agent " + agent.getAgent().getLocalName() + " is alive!");
			}
		}
	}
	
/*	
	private class Listen extends CyclicBehaviour {

		private static final long serialVersionUID = 5641892905754916496L;

		@Override
		public void action() {
			MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = this.myAgent.receive(template);
			if (msg != null && SnifferService.agentsToWatch.contains(msg.getSender())) {
				System.out.println("HEY IM HERE");
				AID receiver = (AID) msg.getAllReplyTo().next();
				msg.clearAllReplyTo();
				
				msg.clearAllReceiver();
				msg.addReceiver(receiver);
				
				msg.setContent("YUCK, cats... Dogs are superior!");
				myAgent.send(msg);
			} else {
				// Keep waiting
				block(500);
			}	
		}
	}
*/
}
