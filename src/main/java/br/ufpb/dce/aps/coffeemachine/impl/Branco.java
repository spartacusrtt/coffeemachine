package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class Branco extends Bebidas {
	
	public Branco(Button button) {
		if (button == Button.BUTTON_2) {
			this.button = Button.BUTTON_2;
		} else {
			this.button = Button.BUTTON_4;
		}
		this.agua = 80;
		this.creme = 20;
		this.cafe = 15;
	}

	public void release(ComponentsFactory factory) {
		factory.getCoffeePowderDispenser().release(this.cafe);
		factory.getWaterDispenser().release(this.agua);
		factory.getCreamerDispenser().release(this.creme);
		if (this.button == Button.BUTTON_4) {
			factory.getSugarDispenser().release(5.0);
		}
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(this.copo);
	}
}