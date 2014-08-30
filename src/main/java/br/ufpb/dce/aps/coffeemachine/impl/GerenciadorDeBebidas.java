package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Messages;
import br.ufpb.dce.aps.coffeemachine.Recipe;


public class GerenciadorDeBebidas {

	private Bebidas bebida;
	private int valorBlack = 35, valorBlackWithSugar = 35, valorWhite = 35, valorWhiteWithSugar = 35;
	private int valorBouillon = 25;

	public void iniciarBebida(Button button) {
		if (button == Button.BUTTON_1 || button == Button.BUTTON_3) {
			this.bebida = new Preto(button);
		} else if (button == Button.BUTTON_2 || button == Button.BUTTON_4) {
			this.bebida = new Branco(button);
		} else {
			this.bebida = new Bouillon(button);
		}
	}

	public boolean conferirIngredientes(ComponentsFactory factory, Button drink) {
			return (this.conferirIngredientes(factory, drink, this.bebida.getCopo(), this.bebida.getAgua(), this.bebida.getPoDeCafe(), this.bebida.getCreme(), this.bebida.getPoDeSopa()));
	}

	public boolean verificaAcucar(ComponentsFactory factory) {

		if (this.bebida.getDrink() == Button.BUTTON_3
				|| this.bebida.getDrink() == Button.BUTTON_4) {
			if (!factory.getSugarDispenser().contains(5.0)) {
				factory.getDisplay().warn(Messages.OUT_OF_SUGAR);
				return false;
			}
		}
		return true;
	}

	public void misturarIngredientes(ComponentsFactory factory, Button drink) {
		factory.getDisplay().info(Messages.MIXING);
	}

	public void release(ComponentsFactory factory) {
		this.bebida.release(factory);
		factory.getDrinkDispenser().release(100.0);
		factory.getDisplay().info(Messages.TAKE_DRINK);

	}

	public boolean conferirIngredientes(ComponentsFactory factory, Button drink,
			int copo, double agua, double po, double creme, double poDeSopa) {
		if (copo > 0) {
			if (!factory.getCupDispenser().contains(copo)) {
				factory.getDisplay().warn(Messages.OUT_OF_CUP);
				return false;
			}
		}
		if (!factory.getWaterDispenser().contains(agua)) {
			factory.getDisplay().warn(Messages.OUT_OF_WATER);
			return false;
		}
		if (po > 0) {
			if (!factory.getCoffeePowderDispenser().contains(po)) {
				factory.getDisplay().warn(Messages.OUT_OF_COFFEE_POWDER);
				return false;
			}
		}
		if (this.bebida.getDrink() == Button.BUTTON_2
				|| this.bebida.getDrink() == Button.BUTTON_4) {
			if (!factory.getCreamerDispenser().contains(creme)) {
				factory.getDisplay().warn(Messages.OUT_OF_CREAMER);
				return false;
			}
		}
		if (poDeSopa > 0) {
			if (!factory.getBouillonDispenser().contains(poDeSopa)) {
				factory.getDisplay().warn(Messages.OUT_OF_BOUILLON_POWDER);
				return false;
			}
		}
		return true;
	}
	
	public int getValorBlack() {
		return valorBlack;
	}

	public void setValorBlack(int valorBlack) {
		this.valorBlack = valorBlack;
	}

	public int getValorBlackWithSugar() {
		return valorBlackWithSugar;
	}

	public void setValorBlackWithSugar(int valorBlackWithSugar) {
		this.valorBlackWithSugar = valorBlackWithSugar;
	}

	public int getValorWhite() {
		return valorWhite;
	}

	public void setValorWhite(int valorWhite) {
		this.valorWhite = valorWhite;
	}

	public int getValorWhiteWithSugar() {
		return valorWhiteWithSugar;
	}

	public void setValorWhiteWithSugar(int valorWhiteWithSugar) {
		this.valorWhiteWithSugar = valorWhiteWithSugar;
	}	
	
	public int getValorBouillon(){
		return valorBouillon;
	}

	public void setValorBouillon(int valorBouillon) {
		this.valorBouillon = valorBouillon;
	}

	public int getValorDaBebida(Button button){
		if(button == Button.BUTTON_1){
			return this.getValorBlack();
		}
		else if(button == Button.BUTTON_2){
			return this.getValorWhite();
		}
		else if(button == Button.BUTTON_3){
			return this.getValorBlackWithSugar();
		}
		else if(button == Button.BUTTON_4){
			return this.getValorWhiteWithSugar();
		}
		else{
			return getValorBouillon();
		}
	}

	public void setPrecoDaBebida(Button button, int priceCents) {
		if(button == Button.BUTTON_1){
			this.setValorBlack(priceCents);
		}
		else if(button == Button.BUTTON_2){
			this.setValorWhite(priceCents);
		}
		else if(button == Button.BUTTON_3){
			this.setValorBlackWithSugar(priceCents);
		}
		else if(button == Button.BUTTON_4){
			this.setValorWhiteWithSugar(priceCents);
		}
		else{
			this.setValorBouillon(priceCents);
		}
	}

	public void mudarReceita(Button drink, Recipe recipe) {
		this.iniciarBebida(drink);
		if(null != recipe.getIngredientQuantity("Water")){
			this.bebida.setAgua(recipe.getIngredientQuantity("Water"));
		}
		if(null != recipe.getIngredientQuantity("Coffee Powder")){
			this.bebida.setPoDeCafe(recipe.getIngredientQuantity("Coffee Powder"));
		}	
		if(recipe.getPriceCents()>0){
			this.setPrecoDaBebida(drink,recipe.getPriceCents());
		}
		
	}

}