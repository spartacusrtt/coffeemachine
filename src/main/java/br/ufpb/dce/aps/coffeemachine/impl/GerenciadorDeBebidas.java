package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class GerenciadorDeBebidas {

	private Bebidas bebida;
	private int valor = 35, valorPreto = 35;
	private int valorCaldo = 25;

	public void iniciarBebida(ComponentsFactory factory, Button button) {
		if (button == Button.BUTTON_1 || button == Button.BUTTON_3) {
			bebida = new Preto(button);
		} else if (button == Button.BUTTON_2 || button == Button.BUTTON_4) {
			bebida = new Branco(button);
		} else {
			bebida = new Bouillon(button);
			setValor(valorCaldo);
		}
	}

	public boolean conferirIngredientes(ComponentsFactory factory, Button button) {
		if (bebida.getDrink() == Button.BUTTON_1|| bebida.getDrink() == Button.BUTTON_3) {
			return (conferirIngredientes(factory, button, 1, 100, 15, 0, 0));

		} else if (bebida.getDrink() == Button.BUTTON_2 || bebida.getDrink() == Button.BUTTON_4) {
			return (conferirIngredientes(factory, button, 1, 80, 15, 20, 0));
		} else {
			return (conferirIngredientes(factory, button, 1, 100, 0, 0, 10));
		}
	}

	public boolean verificaAcucar(ComponentsFactory factory) {

		if (bebida.getDrink() == Button.BUTTON_3 || bebida.getDrink() == Button.BUTTON_4) {
			if (!factory.getSugarDispenser().contains(5)) {
				factory.getDisplay().warn(Messages.OUT_OF_SUGAR);
				return false;
			}
		}
		return true;
	}

	public void misturarIngredientes(ComponentsFactory factory, Button button) {
		factory.getDisplay().info(Messages.MIXING);
		if (bebida.getDrink() == Button.BUTTON_5) {
			factory.getBouillonDispenser().release(10);
		} 
		else {
			factory.getCoffeePowderDispenser().release(15);
		}
	}

	public void release(ComponentsFactory factory) {
		bebida.release(factory);
		factory.getDrinkDispenser().release(100.0);
		factory.getDisplay().info(Messages.TAKE_DRINK);

	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor){
		this.valor = valor;
	}

	public boolean conferirIngredientes(ComponentsFactory factory, Button button, int cup, int water, int powder, int cream, int bouillon) {
		if (cup > 0) {
			if (!factory.getCupDispenser().contains(cup)) {
				factory.getDisplay().warn(Messages.OUT_OF_CUP);
				return false;
			}
		}
		if (!factory.getWaterDispenser().contains(water)) {
			factory.getDisplay().warn(Messages.OUT_OF_WATER);
			return false;
		}
		if (powder > 0) {
			if (!factory.getCoffeePowderDispenser().contains(powder)) {
				factory.getDisplay().warn(Messages.OUT_OF_COFFEE_POWDER);
				return false;
			}
		}
		if (bebida.getDrink() == Button.BUTTON_2 || bebida.getDrink() == Button.BUTTON_4) {
			if (!factory.getCreamerDispenser().contains(cream)) {
				factory.getDisplay().warn(Messages.OUT_OF_CREAMER);
				return false;
			}
		}
		if (bouillon > 0) {
			if (!factory.getBouillonDispenser().contains(bouillon)) {
				factory.getDisplay().warn(Messages.OUT_OF_BOUILLON_POWDER);
				return false;
			}
		}
		return true;
	}

	public int getValorDaBebida(Button button){
		if(button == Button.BUTTON_1){
			return this.getValorPreto();
		}
		else if(button == Button.BUTTON_2){
			return this.getValor();
		}
		else if(button == Button.BUTTON_3){
			return this.getValor();
		}
		else if(button == Button.BUTTON_4){
			return this.getValor();
		}
		else{
			return getValorBouillon();
		}
	}

	public int getValorPreto() {
		return valorPreto;
	}
	
	public void setValorPreto(int priceCents) {
		this.valorPreto = priceCents;
		
	}

	public void setPrecoDaBebida(Button button, int priceCents) {
		if(button == Button.BUTTON_1){
			this.setValorPreto(priceCents);
		}
		else if(button == Button.BUTTON_2){
			this.setValor(priceCents);
		}
		else if(button == Button.BUTTON_3){
			this.setValor(priceCents);
		}
		else if(button == Button.BUTTON_4){
			this.setValor(priceCents);
		}
		else{
			this.setValorBouillon(priceCents);
		}	
	}

	public int getValorBouillon() {
		return valorCaldo;
	}
	
	public void setValorBouillon(int priceCents) {
		this.valorCaldo = priceCents;
		
	}
}