package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.CoffeeMachine;
import br.ufpb.dce.aps.coffeemachine.Coin;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;

public class MyCoffeeMachine implements CoffeeMachine {

	private ComponentsFactory factory;
	private GerenciadorDeCaixa gerenteDeCaixa =  new GerenciadorDeCaixa(); 
	private GerenciadorDeMaquina gerenteDeMaquina = new GerenciadorDeMaquina();
	
	public void insertCoin(Coin coin) {
		gerenteDeCaixa.inserirMoedas(factory, coin, gerenteDeMaquina.getModo());
	}

	public void cancel(){
		gerenteDeCaixa.cancelar(factory);	
	}
	
	public void select(Button button) {		
		gerenteDeMaquina.iniciarPedido(factory, gerenteDeCaixa, button);
	}

	public void setFactory(ComponentsFactory factory) {
		this.factory = factory;		
		gerenteDeMaquina.iniciar(factory);
	}

	public void readBadge(int badgeCode) {
		gerenteDeMaquina.iniciarComCracha(factory, gerenteDeCaixa, badgeCode);
	}

	public void setPrice(Button drink, int priceCents) {
		gerenteDeMaquina.setPrecoDaBebida(drink,priceCents);
		gerenteDeMaquina.mensagemInicial(factory);
		
	}
}