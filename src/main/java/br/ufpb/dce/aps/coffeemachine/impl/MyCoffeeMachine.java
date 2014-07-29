package br.ufpb.dce.aps.coffeemachine.impl;

import static org.mockito.Matchers.anyDouble;

import java.util.ArrayList;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.CoffeeMachineException;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Drink;
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
		factory.getDisplay().warn(Messages.CANCEL);

		for(Coin re: Coin.reverse()){
			for(Coin li: spartacus){
				if(li == re)				
					factory.getCashBox().release(li);			
			}


		}	

		factory.getDisplay().info(Messages.INSERT_COINS);

	}

	public void select(Drink drink) {
				
		
		
		if(!factory.getCupDispenser().contains(1)){
			factory.getDisplay().warn("Out of Cup");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);

			factory.getDisplay().info("Insert coins and select a drink!");
			return ;
		}
		if(!factory.getWaterDispenser().contains(0.1)){
			factory.getDisplay().warn("Out of Water");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);

			factory.getDisplay().info("Insert coins and select a drink!");
			return ;
		}
		
		if(!factory.getCoffeePowderDispenser().contains(0.1)){	

			factory.getDisplay().warn("Out of Coffee Powder");
			factory.getCashBox().release(Coin.quarter);
			factory.getCashBox().release(Coin.dime);

			factory.getDisplay().info("Insert coins and select a drink!");
			return ;

		}

		else{	

			if(drink == Drink.BLACK_SUGAR){
				if(!factory.getSugarDispenser().contains(0.1)){
					factory.getDisplay().warn("Out of Sugar");
					factory.getCashBox().release(Coin.halfDollar);

					factory.getDisplay().info("Insert coins and select a drink!");
					return ;

				}
			}


			factory.getDisplay().info(Messages.MIXING);
			factory.getCoffeePowderDispenser().release(anyDouble());
			factory.getWaterDispenser().release(anyDouble());

			if(drink == Drink.BLACK_SUGAR){
				factory.getSugarDispenser().release(0.1);
			}


			factory.getDisplay().info(Messages.RELEASING);
			factory.getCupDispenser().release(1);
			factory.getDrinkDispenser().release(anyDouble());
			factory.getDisplay().info(Messages.TAKE_DRINK);		

			factory.getDisplay().info("Insert coins and select a drink!");
			spartacus.clear();




		}
	}



}
