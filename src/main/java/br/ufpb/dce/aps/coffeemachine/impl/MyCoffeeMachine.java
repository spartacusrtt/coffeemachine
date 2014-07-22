package br.ufpb.dce.aps.coffeemachine.impl;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class MyCoffeeMachine implements CoffeeMachine{

	private ComponentsFactory factory;
	private int centavos = 0;
	private int inteiro = 0;
	
	private ArrayList<Coin> spartacus= new ArrayList();

	public MyCoffeeMachine(ComponentsFactory factory) {
		this.factory = factory;
		factory.getDisplay().info("Insert coins and select a drink!");
	}

	public void insertCoin(Coin dime) {
		
		this.spartacus.add(dime);
		if(dime == null){
			 throw new CoffeeMachineException("null coin");
		}
		
		centavos += dime.getValue()%100;
		inteiro +=dime.getValue()/100;
		System.out.println(centavos);
		factory.getDisplay().info("Total: US$ "+inteiro +"."+centavos);
		
		
	}

	public void cancel() {
		if(centavos == 0 && inteiro ==0){
			 throw new CoffeeMachineException("null coin");
		}
		factory.getDisplay().warn(Messages.CANCEL_MESSAGE);
		
		for(Coin re: Coin.reverse()){
			for(Coin li: spartacus){
				if(li == re)				
				factory.getCashBox().release(li);			
			}
			
			
		}
		
		
		factory.getDisplay().info(Messages.INSERT_COINS_MESSAGE);
		
	}
	
	

}
