package com.internetofagents.agents;

import java.util.Map;

import jade.core.Agent;
import jade.core.Location;
import jade.domain.AMSEventQueueFeeder;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.DFSubscriber;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.JADEAgentManagement.JADEManagementVocabulary;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.lang.acl.ACLMessage;
import jade.util.InputQueue;

public class ManInTheMiddle extends Agent {

	private static final long serialVersionUID = 7179999536677508196L;
	
	@Override
	protected void setup() {
		System.out.println( getLocalName() + " setting up");
	
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName( getAID() );
		
		this.addBehaviour(new Eavesdrop());
		//this.addBehaviour(new Eavesdrop2(this, dfd));
	    
		try{
	        DFService.register( this, dfd );
	    }
	    catch (Exception e) {
	        System.out.println( "OMG! An Exception: " + e );
	        e.printStackTrace();
	    }
	}

	private class Eavesdrop2 extends DFSubscriber{

		private static final long serialVersionUID = -8966888153464815857L;

		public Eavesdrop2(Agent a, DFAgentDescription template) {
			super(a, template);
		}

		@Override
		public void onDeregister(DFAgentDescription arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRegister(DFAgentDescription arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void handleInform(ACLMessage arg0) {
			//System.out.println(arg0.getContent());
			super.handleInform(arg0);
		}
		
	}
	
	private class Eavesdrop extends AMSSubscriber{

		private static final long serialVersionUID = -3751527678649595117L;

		@Override
		protected void installHandlers(Map handlers) {
			handlers.put(IntrospectionVocabulary.BORNAGENT, new BornAgentEvent());	
		}
		
		private class BornAgentEvent implements EventHandler{

			private static final long serialVersionUID = -1709226224922510054L;

			@Override
			public void handle(Event arg0) {
				/*SearchConstraints c = new SearchConstraints();
	            c.setMaxResults ( new Long(-1) );
	            
	            DFAgentDescription[] agents = null;
				try {
					agents = DFService.search(myAgent, new DFAgentDescription (), c );
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            for (DFAgentDescription dfAgentDescription : agents) {
	            	if(!dfAgentDescription.getName().equals(myAgent.getAID()))
	            		myAgent.addBehaviour(new Eavesdrop2(dfAgentDescription.getName(), dfAgentDescription));
				}*/
				System.out.println("A new agent is alive!");
			}
		}
	}
}
