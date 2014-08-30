package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class Bouillon extends Bebidas {
	
	public Bouillon (Button button) {
		this.button = Button.BUTTON_5;
	}

	@Override
	public void release(ComponentsFactory factory) {
		factory.getWaterDispenser().release(100);
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(1);
	}
}