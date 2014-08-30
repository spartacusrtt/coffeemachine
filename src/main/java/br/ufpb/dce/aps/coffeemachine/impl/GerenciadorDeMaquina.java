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

		if (gerenteFinanceiro.getTotal() >= gerenteDeBebidas.getValorDaBebida(button)) {
			gerenteFinanceiro.liberarTroco(factory, gerenteDeBebidas.getValorDaBebida(button));
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
		mensagemInicial(factory);
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

	public void mensagemInicial(ComponentsFactory factory){
		int valor = gerenteDeBebidas.getValor();
		int valorPreto = gerenteDeBebidas.getValorPreto();
		int valorCaldo = gerenteDeBebidas.getValorBouillon();
		factory.getButtonDisplay().show("Black: $0."+valorPreto, "White: $0."+valor,
				"Black with sugar: $0."+valor, "White with sugar: $0."+valor,
				"Bouillon: $0."+valorCaldo, null, null);
	}
	
	public void setPrecoDaBebida(Button drink, int priceCents) {
		gerenteDeBebidas.setPrecoDaBebida(drink, priceCents);	
	}

}