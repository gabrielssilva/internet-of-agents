package com.internetofagents.agents;

import java.util.Iterator;
import java.util.Map;

import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.DFSubscriber;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.BornAgent;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;

public class ManInTheMiddle extends Agent {

	private static final long serialVersionUID = 7179999536677508196L;
	
	@Override
	protected void setup() {
		System.out.println( getLocalName() + " setting up");
	
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName( getAID() );
		
		this.addBehaviour(new WatchAMSSubscription());
		this.addBehaviour(new WatchDFSubscription(this, dfd));
	    
		try{
	        DFService.register( this, dfd );
	    }
	    catch (Exception e) {
	        System.out.println( "OMG! An Exception: " + e );
	        e.printStackTrace();
	    }
	}

	private class WatchDFSubscription extends DFSubscriber{

		private static final long serialVersionUID = -8966888153464815857L;

		public WatchDFSubscription(Agent a, DFAgentDescription template) {
			super(a, template);
		}

		@Override
		public void onDeregister(DFAgentDescription arg0) {
			
		}

		@Override
		public void onRegister(DFAgentDescription arg0) {
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
					System.out.println("Adding agent " + arg0.getName() + " to the sniffer service.");
				}
				
				
				System.out.println("A new agent is alive!");
			}
		}
	}

}
