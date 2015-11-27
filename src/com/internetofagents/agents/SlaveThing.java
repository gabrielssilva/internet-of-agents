package com.internetofagents.agents;

import com.internetofagents.behaviors.ServiceType;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class SlaveThing extends Thing {

	private static final long serialVersionUID = -3607739220387217089L;
	
	@Override
	protected void setup() {
		super.setup();
		
		this.addBehaviour(new OfferServiceBehavior());
		this.addBehaviour(new PerformActionBehavior());
	}
	
	
	private class OfferServiceBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 2826783037896725544L;

		@Override
		public void action() {
			MessageTemplate cfpTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = this.myAgent.receive(cfpTemplate);
			
			if (msg != null) {
				// Message received
				ServiceType serviceType = ServiceType.valueOf(msg.getContent());
				ACLMessage reply = msg.createReply();

				if (hasAbility(serviceType)) {
					reply.setPerformative(ACLMessage.PROPOSE);
					
					// Maybe send the place?
					// reply.setContent(...);
				} else {
					reply.setPerformative(ACLMessage.REFUSE);
					
					// reply.setContent(...);
				}
				
				this.myAgent.send(reply);
			} else {
				// Keep waiting
				block();
			}
		}
		
	}
	
	private class PerformActionBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 2979730890098622216L;

		@Override
		public void action() {
			MessageTemplate cfpTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = this.myAgent.receive(cfpTemplate);
			
			if (msg != null) {
				// Message received
				ServiceType serviceType = ServiceType.valueOf(msg.getContent());
				ACLMessage reply = msg.createReply();

				if (performAction(serviceType)) {
					reply.setPerformative(ACLMessage.INFORM);
				} else {
					reply.setPerformative(ACLMessage.FAILURE);
				}
				
				this.myAgent.send(reply);
			} else {
				// Keep waiting
				block();
			}
		}
	}
}
