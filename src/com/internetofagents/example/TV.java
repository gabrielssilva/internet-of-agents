package com.internetofagents.example;

import com.internetofagents.agents.Thing;
import com.internetofagents.behaviors.Ability;

public class TV extends Thing {

	private static final long serialVersionUID = 6470040213650252659L;

	@Override
	protected void setup() {
		super.setup();
		
		//registerAbility(null, new TurnOff());
		
		this.setMode("ROMANTIC");
	}
	
	private class TurnOff extends Ability{

		@Override
		public boolean perform() {
			System.out.println("Turning " + getLocalName() + " Off...");
			return true;
		}
		
	}
}
