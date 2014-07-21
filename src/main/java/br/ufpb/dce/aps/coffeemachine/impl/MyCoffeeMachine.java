package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;

public class MyCoffeeMachine implements CoffeeMachine{

	private ComponentsFactory factory;
	private int centavos = 0;
	private int inteiro = 0;

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void insertCoin(Coin dime) {
		
		if(dime == null){
			 throw new CoffeeMachineException("null coin");
		}
		
		centavos += dime.getValue()%100;
		inteiro +=dime.getValue()/100;
		System.out.println(centavos);
		factory.getDisplay().info("Total: US$ "+inteiro +"."+centavos);
		
		
	}
	
	

}
