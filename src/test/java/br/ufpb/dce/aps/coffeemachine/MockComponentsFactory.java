package br.ufpb.dce.aps.coffeemachine;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

public class MockComponentsFactory implements ComponentsFactory {

	private static final String COFFEE_POWDER = "coffeePowder";
	private static final String WATER = "water";
	private static final String CUP = "cup";
	private static final String SUGAR = "sugar";
	private static final String CREAMER = "creamer";
	private static final String BOUILLON = "bouillon";

	private Display display;
	private CashBox cashBox;
	private DrinkDispenser drinkDispenser;
	private Map<String,Dispenser> dispensers = new HashMap<String, Dispenser>();
	private PayrollSystem payrollSystem;
	private ButtonDisplay buttonDisplay;
	private Steamer steamer;

	public Display getDisplay() {
		if (display == null) {
			display = mock(Display.class);
		}

		return display;
	}

	public CashBox getCashBox() {
		if (cashBox == null) {
			cashBox = mock(CashBox.class);
		}

		return cashBox;
	}

	public Dispenser getCoffeePowderDispenser() {
		return getDispenser(COFFEE_POWDER);
	}
	
	public Dispenser getWaterDispenser() {
		return getDispenser(WATER);
	}

	public Dispenser getCupDispenser() {
		return getDispenser(CUP);
	}

	public DrinkDispenser getDrinkDispenser() {
		if (drinkDispenser == null) {
			drinkDispenser = mock(DrinkDispenser.class);
		}

		return drinkDispenser;
	}

	private Dispenser getDispenser(String key) {
		if (dispensers.get(key) == null) {
			dispensers.put(key, mock(Dispenser.class));
		}
		
		return dispensers.get(key);		
	}

	public Dispenser getSugarDispenser() {
		return getDispenser(SUGAR);
	}

	public Dispenser getCreamerDispenser() {
		return getDispenser(CREAMER);
	}

	public Dispenser getBouillonDispenser() {
		return getDispenser(BOUILLON);
	}

	public PayrollSystem getPayrollSystem() {
		if (payrollSystem == null) {
			payrollSystem = mock(PayrollSystem.class);
		}

		return payrollSystem;
	}

	public ButtonDisplay getButtonDisplay() {
		if (buttonDisplay == null) {
			buttonDisplay = mock(ButtonDisplay.class);
		}

		return buttonDisplay;
	}

	public Steamer getSteamer() {
		if (steamer == null) {
			steamer = mock(Steamer.class);
		}

		return steamer;
	}

}
