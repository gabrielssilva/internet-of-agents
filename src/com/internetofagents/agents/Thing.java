package com.internetofagents.agents;

import java.util.Hashtable;

import com.internetofagents.behaviors.Ability;
import com.internetofagents.behaviors.GenericCyclicBehaviour;
import com.internetofagents.behaviors.Mode;
import com.internetofagents.behaviors.ServiceType;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
	private Mode mode;
	private String place;
	
	@Override
	protected void setup() {
		this.place = Place.HOUSE;
		this.abilities = new Hashtable<ServiceType, Ability >();
		
		try{
			System.out.println( getLocalName() + " setting up");

            // create the agent description of itself
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            DFService.register( this, dfd );

            // Default thing behaviors
    		this.addBehaviour(new OfferServiceBehavior());
    		this.addBehaviour(new PerformActionBehavior());
    		this.addBehaviour(new RegisterOnKeeperBehaviour());
    		this.addBehaviour(new AnnounceMode());
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
					// set content to describe action
					// reply.setContent(...)
					// performAction(...)
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
	
	private class AnnounceMode extends CyclicBehaviour {

		private static final long serialVersionUID = 5897938057182082473L;

		@Override
		public void action() {
			//Broadcast Mode
		}
		
	}
	
	private class RegisterOnKeeperBehaviour extends Behaviour {

		private static final long serialVersionUID = -2575325491744325392L;
		private AID keepers[];
				
		@Override
		public void action() {
			System.out.println("Searching for keepers");
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("keeper@" + place);
			template.addServices(sd);
			
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template); 
				this.keepers = new AID[result.length];
				
				System.out.println("Found keeper, trying to register: " + result.toString());
				System.out.println("Found keeper, trying to register: " + result.length);
				System.out.println("Found keeper, trying to register: " + result[0]);
				
				if(result.length > 0){
					System.out.println("Found keeper, trying to register: " + result[0].getName());
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(result[0].getName());
					this.myAgent.send(msg);
				} else {
					block();
				}
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
		}
		
		@Override
		public boolean done() {
			return this.keepers.length > 0;
			//return false;
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

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
}
