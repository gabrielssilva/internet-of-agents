package com.internetofagents.agents;

import java.util.ArrayList;
import java.util.List;

import com.internetofagents.behaviors.GenericCyclicBehaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Keeper extends Agent {
	
	private static final long serialVersionUID = -7867936987803586884L;
	
	private String place; 
	private List<AID> things; 
	
	@Override
	protected void setup() {
		this.things = new ArrayList<AID>();
		this.place = Place.HOUSE;
		
		DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("keeper@" + this.place);
		sd.setName("Kepper");
		
		dfd.addServices(sd);
		
		this.addBehaviour(new WatchForActionBehavior());
        this.addBehaviour(new WatchForThingsBehavior());
		
        try {
    		DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.out.println("Error registering agent: "+this.getAID());
			e.printStackTrace();
		}
	}
	
	
	private class WatchForActionBehavior extends GenericCyclicBehaviour {

		private static final long serialVersionUID = 3733996350261236554L;

		@Override
		public void onMessageReceived(ACLMessage msg) {
			// content = msg.getContent()
			// do something with the content
			// change mode			
		}

		@Override
		public void onMessageMissing(ACLMessage msg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getMessageType() {
			return ACLMessage.INFORM;
		}
	}
	
	private class WatchForThingsBehavior extends GenericCyclicBehaviour {

		private static final long serialVersionUID = -685790005992042101L;

		@Override
		public void onMessageReceived(ACLMessage msg) {
			System.out.println("Agent "+msg.getSender()+" trying to register");
			things.add(msg.getSender());
			
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			this.myAgent.send(reply);
			System.out.println("Success, sending response");
		}

		@Override
		public void onMessageMissing(ACLMessage msg) {
			block();
		}
		
		@Override
		public int getMessageType() {
			return ACLMessage.REQUEST;
		}
		
	}
	
}
