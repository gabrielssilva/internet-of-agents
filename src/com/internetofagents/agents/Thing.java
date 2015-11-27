package com.internetofagents.agents;

import java.util.Hashtable;

import com.internetofagents.behaviors.Ability;
import com.internetofagents.behaviors.ServiceType;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Thing extends Agent {

	private static final long serialVersionUID = 6716848269178218937L;
	private Hashtable<ServiceType, Ability > abilities;
	
	@Override
	protected void setup() {
		this.abilities = new Hashtable<ServiceType, Ability >();
		
		try{
			System.out.println( getLocalName() + " setting up");

            // create the agent descrption of itself
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            DFService.register( this, dfd );

            // Default thing behaviors
    		
    		this.addBehaviour(new OfferServiceBehavior());
    		this.addBehaviour(new PerformActionBehavior());
        }
        catch (Exception e) {
            System.out.println( "OMG! An Exception: " + e );
            e.printStackTrace();
        }
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

	
	public void registerAbility(ServiceType serviceType, Ability ability) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceType.toString());
		sd.setName(serviceType.toString());
		dfd.addServices(sd);
		
		try {
			System.out.println("Registering "+serviceType.toString()+" for "+this.getAID());
			DFService.register(this, dfd);
			this.abilities.put(serviceType, ability);
		} catch (FIPAException e) {
			System.out.println("Unable to register service: " + serviceType.toString());
			e.printStackTrace();
		}
	}
	
	
	public boolean hasAbility(ServiceType serviceType) {
		return this.abilities.containsKey(serviceType);
	}
	
	public boolean performAction(ServiceType serviceType) {
		Ability ability = this.abilities.get(serviceType);
		return ability.perform();
	}
	
}
