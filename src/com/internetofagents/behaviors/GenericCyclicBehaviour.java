package com.internetofagents.behaviors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class GenericCyclicBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -1303982911221710829L;
	
	@Override
	public void action() {
		MessageTemplate cfpTemplate = MessageTemplate.MatchPerformative(getMessageType());
		ACLMessage msg = this.myAgent.receive(cfpTemplate);
		
		if (msg != null) {
			onMessageReceived(msg);
		} else {
			onMessageMissing(msg);
		}
	}

	public abstract int getMessageType();
	public abstract void onMessageReceived(ACLMessage msg);
	public abstract void onMessageMissing(ACLMessage msg);
}
