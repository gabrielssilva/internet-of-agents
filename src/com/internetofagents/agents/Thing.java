package com.internetofagents.agents;

import java.util.Hashtable;

import com.internetofagents.behaviors.Ability;
import com.internetofagents.behaviors.ServiceType;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class Thing extends Agent {

	private static final long serialVersionUID = 6716848269178218937L;
	private Hashtable<ServiceType, Ability> abilities; 
	
	@Override
	protected void setup() {
		this.abilities = new Hashtable<ServiceType, Ability>();
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
