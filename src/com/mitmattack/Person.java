package com.mitmattack;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Person extends Agent {

	private static final long serialVersionUID = -8495365903036386745L;

	@Override
	protected void setup() {
		System.out.println( getLocalName() + " setting up");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );
        
        ServiceDescription sd = new ServiceDescription();
		sd.setType("person-communication");
		sd.setName("Person-Communication");
		dfd.addServices(sd);
		
		this.addBehaviour(new Speak());
		this.addBehaviour(new Listen());
        
		try{
            DFService.register( this, dfd );
        }
        catch (Exception e) {
            System.out.println( "OMG! An Exception: " + e );
            e.printStackTrace();
        }
	}

	private class Speak extends OneShotBehaviour {

		private static final long serialVersionUID = -5788792990071056764L;

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setContent("My secret is, that I Love cats!");
			
			DFAgentDescription [] agents = null;
	        
	        try {
	        	DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("person-communication");
				template.addServices(sd);
				
				agents = DFService.search(myAgent, template); 
				
	            for (DFAgentDescription dfAgentDescription : agents) {
	       
	            	if(!dfAgentDescription.getName().equals(this.myAgent.getAID()))
	            		System.out.println(getLocalName()+" Sending to: "+dfAgentDescription.getName());
	            		msg.addReceiver(dfAgentDescription.getName());
				}
	            
				
				this.myAgent.send(msg);

				//System.out.println(getLocalName() + " says: " + msg.getContent());
	        }
	        catch (Exception e) {
	        	System.out.println("No agents found =(");
	        }

		}
	}
	
	private class Listen extends CyclicBehaviour {

		private static final long serialVersionUID = 5641892905754916496L;

		@Override
		public void action() {
			MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = this.myAgent.receive(template);
			
			if (msg != null) {
				System.out.println(getLocalName() + " heard: " + msg.getContent());
			} else {
				// Keep waiting
				block();
			}	
		}
		
	}
}
