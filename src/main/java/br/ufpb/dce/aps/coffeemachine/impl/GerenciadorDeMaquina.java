package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;
import br.ufpb.dce.aps.coffeemachine.Recipe;

public class GerenciadorDeMaquina {

	private GerenciadorDeBebidas gerenteDeBebidas = new GerenciadorDeBebidas();
	private static String modo = "";
	private int cracha = 0;
	public void iniciarPedidoDeBebida(ComponentsFactory factory,
			GerenciadorDeCaixa gerenteFinanceiro, Button drink) {
		if (modo.equals("cracha")){
			this.iniciarPedidoComCracha(factory, drink);
		}else{
			this.iniciarPedidoComMoedas(factory, gerenteFinanceiro, drink);
		}
		
	}
		
	public void iniciarPedidoComMoedas(ComponentsFactory factory,
			GerenciadorDeCaixa gerenteFinanceiro, Button button){
		this.gerenteDeBebidas.iniciarBebida(button);
		if (!gerenteFinanceiro.conferirDinheiro(factory,
				this.gerenteDeBebidas.getValorDaBebida(button))) {
			return;
		}

		if (!this.gerenteDeBebidas.conferirIngredientes(factory, button)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}
		if (!this.gerenteDeBebidas.verificaAcucar(factory)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}

		if (!gerenteFinanceiro.verificarTroco(factory,
			this.gerenteDeBebidas.getValorDaBebida(button))) {
			return;
		}
	

		gerenteDeBebidas.misturarIngredientes(factory, button);
		gerenteDeBebidas.release(factory);

		if (gerenteFinanceiro.getTotal() >= this.gerenteDeBebidas.getValorDaBebida(button)) {
					gerenteFinanceiro.liberarTroco(factory, this.gerenteDeBebidas.getValorDaBebida(button));
		}

		this.reIniciar(factory);
		
		gerenteFinanceiro.limparMoedas();	
	}
	
	public void iniciarPedidoComCracha(ComponentsFactory factory, Button button){
		this.gerenteDeBebidas.iniciarBebida(button);
		
		if (!this.gerenteDeBebidas.conferirIngredientes(factory, button)) {
			return;
		}
		if (!this.gerenteDeBebidas.verificaAcucar(factory)) {
			return;
		}
		if(!factory.getPayrollSystem().debit(gerenteDeBebidas.getValorDaBebida(button), this.cracha)){
			factory.getDisplay().warn(Messages.UNKNOWN_BADGE_CODE);
			this.reIniciar(factory);
			return;
		}
		
		this.gerenteDeBebidas.misturarIngredientes(factory, button);
		this.gerenteDeBebidas.release(factory);
		
		this.reIniciar(factory);
	}

	public void iniciar(ComponentsFactory factory) {	
			this.mensagemInicial(factory);
			factory.getDisplay().info(Messages.INSERT_COINS);
			GerenciadorDeMaquina.setModo("moedas");
	}
	
	public void mensagemInicial(ComponentsFactory factory){
		factory.getButtonDisplay().show("Black: $0."+gerenteDeBebidas.getValorBlack(), "White: $0."+gerenteDeBebidas.getValorWhite(),
				"Black with sugar: $0."+gerenteDeBebidas.getValorBlackWithSugar(), "White with sugar: $0."+gerenteDeBebidas.getValorWhiteWithSugar(),
				"Bouillon: $0."+gerenteDeBebidas.getValorBouillon(), null, null);
	}
	
	public void iniciarComCracha(ComponentsFactory factory, GerenciadorDeCaixa gerenteAuxiliar, int cracha) {
		if(gerenteAuxiliar.getTotal()>0){
			factory.getDisplay().warn(Messages.CAN_NOT_READ_BADGE);
			return;
		}
		else{
			factory.getDisplay().info(Messages.BADGE_READ);
			this.cracha = cracha;
			GerenciadorDeMaquina.setModo("cracha");
		}
	}
		
	
	public static void setModo(String novoModo) {
		modo = novoModo;
	}
	
	public String getModo(){
		return modo;
	}
	
	public void reIniciar(ComponentsFactory factory){
		factory.getDisplay().info(Messages.INSERT_COINS);
		GerenciadorDeMaquina.setModo (" ");
	}

	public void setPrecoDaBebida(Button drink, int priceCents) {
		this.gerenteDeBebidas.setPrecoDaBebida(drink, priceCents);	
	}

	public void mudarReceita(Button drink, Recipe recipe) {
		this.gerenteDeBebidas.mudarReceita(drink, recipe);
		
	}

}