package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeMaquina {

	private GerenciadorDeBebidas gerenteDeBebidas = new GerenciadorDeBebidas();
	private static String modo = "";
	private int cracha = 0;

	public void iniciarPedido(ComponentsFactory factory, GerenciadorDeCaixa gerenteFinanceiro, Button button) {
		if (modo.equals("cracha")){
			iniciarPedidoComCracha(factory, button);
		}else{
			iniciarPedidoComMoedas(factory, gerenteFinanceiro, button);
		}
	}

	public void iniciarPedidoComMoedas(ComponentsFactory factory, GerenciadorDeCaixa gerenteFinanceiro, Button button){

		gerenteDeBebidas.iniciarBebida(factory, button);

		if (!gerenteFinanceiro.conferirDinheiro(factory,
				gerenteDeBebidas.getValor())) {
			return;
		}

		if (!gerenteDeBebidas.conferirIngredientes(factory, button)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}
		if (!gerenteDeBebidas.verificaAcucar(factory)) {
			gerenteFinanceiro.liberarMoedas(factory, false);
			return;
		}

		if (!gerenteFinanceiro.verificarTroco(factory,
				gerenteDeBebidas.getValor())) {
			return;
		}


		gerenteDeBebidas.misturarIngredientes(factory, button);
		gerenteDeBebidas.release(factory);

		if (gerenteFinanceiro.getTotal() >= gerenteDeBebidas.getValor()) {
			gerenteFinanceiro.liberarTroco(factory, gerenteDeBebidas.getValor());
		}

		factory.getDisplay().info(Messages.INSERT_COINS);
		setModo (" ");

		gerenteFinanceiro.limparMoedas();	
	}

	public void iniciarPedidoComCracha(ComponentsFactory factory, Button button){

		gerenteDeBebidas.iniciarBebida(factory, button);

		if (!gerenteDeBebidas.conferirIngredientes(factory, button)) {
			return;
		}
		if (!gerenteDeBebidas.verificaAcucar(factory)) {
			return;
		}
		if(!factory.getPayrollSystem().debit(gerenteDeBebidas.getValor(), cracha)){
			factory.getDisplay().warn(Messages.UNKNOWN_BADGE_CODE);
			factory.getDisplay().info(Messages.INSERT_COINS);
			setModo(" ");
			return;
		}

		gerenteDeBebidas.misturarIngredientes(factory, button);
		gerenteDeBebidas.release(factory);

		factory.getDisplay().info(Messages.INSERT_COINS);
		setModo(" ");
	}

	public void iniciar(ComponentsFactory factory) {
		factory.getButtonDisplay().show("Black: $0.35", "White: $0.35",
				"Black with sugar: $0.35", "White with sugar: $0.35",
				"Bouillon: $0.25", null, null);
		factory.getDisplay().info(Messages.INSERT_COINS);
		setModo("moedas");
	}

	public void iniciarComCracha(ComponentsFactory factory, GerenciadorDeCaixa gerenteAuxiliar, int cracha) {
		if(gerenteAuxiliar.getTotal()>0){
			factory.getDisplay().warn(Messages.CAN_NOT_READ_BADGE);
			return;
		}
		else{
			factory.getDisplay().info(Messages.BADGE_READ);
			this.cracha = cracha;
			setModo("cracha");
		}
	}


	public static void setModo(String novoModo) {
		modo = novoModo;
	}

	public String getModo(){
		return modo;
	}

}