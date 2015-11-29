package com.internetofagents.agents;

import java.util.Map;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;

public class ManInTheMiddle extends Agent {

	private static final long serialVersionUID = 7179999536677508196L;
	
	@Override
	protected void setup() {
		System.out.println( getLocalName() + " setting up");
	
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName( getAID() );
		
		this.addBehaviour(new Eavesdrop());
	    
		try{
	        DFService.register( this, dfd );
	    }
	    catch (Exception e) {
	        System.out.println( "OMG! An Exception: " + e );
	        e.printStackTrace();
	    }
	}
	
	private class Eavesdrop extends AMSSubscriber{

		private static final long serialVersionUID = -3751527678649595117L;

		@Override
		protected void installHandlers(Map handlers) {
			EventHandler createAgentHandler = new BornAgentEvent();
			
			handlers.put(IntrospectionVocabulary.BORNAGENT, createAgentHandler);
		}
		
		private class BornAgentEvent implements EventHandler{

			private static final long serialVersionUID = -1709226224922510054L;

			@Override
			public void handle(Event arg0) {
				System.out.println("A new agent is alive!");
			}
		}
	}
}
