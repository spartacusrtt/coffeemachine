package br.ufpb.dce.aps.coffeemachine;

public enum Button {
	BUTTON_1(1), BUTTON_2(2), BUTTON_3(3), BUTTON_4(4), BUTTON_5(5), BUTTON_6(6), BUTTON_7(7);
	
	private final int position;

	private Button(final int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

}