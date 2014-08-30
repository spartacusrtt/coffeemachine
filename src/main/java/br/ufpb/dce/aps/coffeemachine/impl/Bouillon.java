package br.ufpb.dce.aps.coffeemachine.impl;

import br.ufpb.dce.aps.coffeemachine.ComponentsFactory;
import br.ufpb.dce.aps.coffeemachine.Button;
import br.ufpb.dce.aps.coffeemachine.Messages;

public class Bouillon extends Bebidas {
	
	public Bouillon (Button button) {
		this.button = Button.BUTTON_5;
		this.agua = 100.0;
		this.caldo = 10;
	}

	@Override
	public void release(ComponentsFactory factory) {
		factory.getBouillonDispenser().release(this.caldo);
		factory.getWaterDispenser().release(this.agua);
		factory.getDisplay().info(Messages.RELEASING);
		factory.getCupDispenser().release(this.copo);
	}
}