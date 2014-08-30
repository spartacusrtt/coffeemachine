package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class Preto extends Bebidas {

	public Preto (Button button) {
		if (button == Button.BUTTON_1) {
			this.button = Button.BUTTON_1;
		} else {
			this.button = Button.BUTTON_3;
		}
	}

	public void release(ComponentsFactory factory) {
		factory.getWaterDispenser().release(100);
		if (button == Button.BUTTON_3) {
			factory.getSugarDispenser().release(5);
		}
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(1);
	}
}