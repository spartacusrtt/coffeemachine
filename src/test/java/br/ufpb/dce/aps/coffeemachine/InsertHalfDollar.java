package br.ufpb.dce.aps.coffeemachine;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class InsertHalfDollar extends CoffeeMachineTest {
	
	private InOrder inOrder;
	
	@Before
	public void given() {
		inOrder = prepareScenarioWithCoins(Coin.halfDollar);
	}

	@Test
	public void cancelWithOneCoin() {
		// Operation under test
		facade.cancel();

		// Verification
		verifyCancel(inOrder, Coin.halfDollar);
	}

	@Test
	public void selectBlackWithoutSugar() {
		// Simulating returns
		doContain(coffeePowderDispenser, anyDouble());
		doContain(waterDispenser, anyDouble());
		doContain(cupDispenser, 1);
		doNotContain(sugarDispenser, anyDouble()); // Out of Sugar

		// Operation under test
		facade.select(Button.BUTTON_3);

		// Verification
		inOrder.verify(cupDispenser).contains(1);
		inOrder.verify(waterDispenser).contains(anyDouble());
		inOrder.verify(coffeePowderDispenser).contains(anyDouble());
		inOrder.verify(sugarDispenser).contains(anyDouble());
		verifyOutOfIngredient(inOrder, Messages.OUT_OF_SUGAR, Coin.halfDollar);
	}

	@Test
	public void selectWhiteSugarWithChange() {
		// Simulating returns
		doCount(Coin.dime, 10);
		doCount(Coin.nickel, 10);
		doContainWhiteSugarIngredients();

		// Operation under test
		facade.select(Button.BUTTON_4);

		// Verification
		verifyWhiteSugarPlan(inOrder);
		verifyCount(inOrder, Coin.dime, Coin.nickel);
		verifyWhiteSugarMix(inOrder);
		verifyDrinkRelease(inOrder);
		verifyCloseSession(inOrder, Coin.dime, Coin.nickel);
	}

	@Test
	public void selectBlackWithoutEnoughtChange() {
		// Simulating returns
		doCount(Coin.dime, 0);
		doCount(Coin.nickel, 0);
		doCount(Coin.penny, 0); // Out of change
		doContainWhiteIngredients();

		// Operation under test
		facade.select(Button.BUTTON_2);

		// Verification
		verifyWhitePlan(inOrder);
		verifyCount(inOrder, Coin.dime, Coin.nickel, Coin.penny);
		inOrder.verify(display).warn(Messages.NO_ENOUGHT_CHANGE);
		verifyCloseSession(inOrder, Coin.halfDollar);
	}

	@Test
	public void selectWhiteWithNonTrivialChange() {
		// Simulating returns
		doCount(Coin.dime, 0);
		doCount(Coin.nickel, 10);
		doContainWhiteIngredients();

		// Operation under test
		facade.select(Button.BUTTON_2);

		// Verification
		verifyWhitePlan(inOrder);
		verifyCount(inOrder, Coin.dime, Coin.nickel);
		verifyWhiteMix(inOrder);
		verifyDrinkRelease(inOrder);

		inOrder.verify(cashBox, times(3)).release(Coin.nickel);
		verifyNewSession(inOrder);
	}

	@Test
	public void selectWhiteWithoutCreamer() {
		// Simulating returns
		doContain(coffeePowderDispenser, anyDouble());
		doContain(waterDispenser, anyDouble());
		doContain(cupDispenser, 1);
		doNotContain(creamerDispenser, anyDouble()); // Out of Creamer!

		// Operation under test
		facade.select(Button.BUTTON_2);

		// Verification
		inOrder.verify(cupDispenser).contains(1);
		inOrder.verify(waterDispenser).contains(anyDouble());
		inOrder.verify(coffeePowderDispenser).contains(anyDouble());
		inOrder.verify(creamerDispenser).contains(anyDouble());
		verifyOutOfIngredient(inOrder, Messages.OUT_OF_CREAMER, Coin.halfDollar);
	}
	
	@Test
	public void selectBouillonWithChange() {
		// Simulating returns
		doCount(Coin.quarter, 10);
		doContainBouillonIngredients();

		// Operation under test
		facade.select(Button.BUTTON_5);

		// Verification
		verifyBouillonPlan(inOrder);
		verifyCount(inOrder, Coin.quarter);
		verifyBouillonMix(inOrder);
		verifyDrinkRelease(inOrder);
		verifyCloseSession(inOrder, Coin.quarter);
	}
	
	@Test
	public void insertCoins() {
		// Operation under test
		facade.insertCoin(Coin.nickel);

		// Verification
		verifySessionMoney("0.55");
	}

	@Test
	public void newDrink() {
		// Preparing scenario: configure new drink
		Recipe recipe = sweetCreamRecipe();
		facade.configuteDrink(Button.BUTTON_6, recipe);

		// Simulating returns
		doContain(waterDispenser, anyDouble());
		doContain(creamerDispenser, anyDouble());
		doContain(sugarDispenser, anyDouble());
		doContain(cupDispenser, 1);

		// Operation under test
		facade.select(Button.BUTTON_6);

		// Verification
		verify(buttonDisplay).show("Black: $0.35", "White: $0.35",
				"Black with sugar: $0.35", "White with sugar: $0.35",
				"Bouillon: $0.25", "Sweet cream: $0.50", null);

		inOrder.verify(cupDispenser).contains(1);
		inOrder.verify(waterDispenser).contains(100.0);
		inOrder.verify(sugarDispenser).contains(15.0);
		inOrder.verify(creamerDispenser).contains(25.0);
		inOrder.verify(display).info(Messages.MIXING);
		inOrder.verify(waterDispenser).release(100.0);
		inOrder.verify(creamerDispenser).release(25.0);
		inOrder.verify(sugarDispenser).release(15.0);
		verifyDrinkRelease(inOrder);
		verifyNewSession(inOrder);
	}

	private Recipe sweetCreamRecipe() {
		Recipe recipe = new Recipe();
		recipe.setName("Sweet cream");
		recipe.setPriceCents(50);
		recipe.setItem(Recipe.WATER, 100.0);
		recipe.setItem(Recipe.CREAMER, 25.0);
		recipe.setItem(Recipe.SUGAR, 15.0);
		recipe.setPlanSequence(Recipe.WATER, Recipe.SUGAR, Recipe.CREAMER);
		recipe.setMixSequence(Recipe.WATER, Recipe.CREAMER, Recipe.SUGAR);
		return recipe;
	}

	@Test
	public void newDrinkWithNewDispenser() {
		// Preparing scenario: add dispenser and configure new drink
		facade.addDispenser(Recipe.CHOCOLATE, chocolateDispenser);
		facade.addDispenser(Recipe.MILK, milkDispenser);
		Recipe recipe = chocolatteRecipe();
		facade.configuteDrink(Button.BUTTON_6, recipe);
		inOrder = resetMocks();

		// Simulating returns
		doContain(chocolateDispenser, anyDouble());
		doContain(milkDispenser, anyDouble());
		doContain(waterDispenser, anyDouble());
		doContain(creamerDispenser, anyDouble());
		doContain(sugarDispenser, anyDouble());
		doContain(cupDispenser, 1);

		// Operation under test
		facade.select(Button.BUTTON_6);

		// Verification
		inOrder.verify(cupDispenser).contains(1);
		inOrder.verify(milkDispenser).contains(120.0);
		inOrder.verify(chocolateDispenser).contains(20.0);
		inOrder.verify(sugarDispenser).contains(5.0);
		inOrder.verify(display).info(Messages.MIXING);
		inOrder.verify(milkDispenser).release(120.0);
		inOrder.verify(chocolateDispenser).release(20.0);
		inOrder.verify(sugarDispenser).release(5.0);
		verifyDrinkRelease(inOrder);
		verifyNewSession(inOrder);
	}

	private Recipe chocolatteRecipe() {
		Recipe recipe = new Recipe();
		recipe.setName("Chocolatte");
		recipe.setPriceCents(50);
		recipe.setItem(Recipe.MILK, 120.0);
		recipe.setItem(Recipe.CHOCOLATE, 20.0);
		recipe.setItem(Recipe.SUGAR, 5.0);
		recipe.setPlanSequence(Recipe.MILK, Recipe.CHOCOLATE, Recipe.SUGAR);
		recipe.setMixSequence(Recipe.MILK, Recipe.CHOCOLATE, Recipe.SUGAR);
		return recipe;
	}

	@Test
	public void drinkWithSteamer() {
		// Preparing scenario: add dispenser and configure new drink
		facade.addDispenser(Recipe.MILK, milkDispenser);
		Recipe recipe = steamedMilk();
		facade.configuteDrink(Button.BUTTON_6, recipe);
		inOrder = resetMocks();

		// Simulating returns
		doContain(milkDispenser, anyDouble());
		doContain(cupDispenser, 1);

		// Operation under test
		facade.select(Button.BUTTON_6);

		// Verification
		inOrder.verify(cupDispenser).contains(1);
		inOrder.verify(milkDispenser).contains(150.0);
		inOrder.verify(display).info(Messages.MIXING);
		inOrder.verify(milkDispenser).release(150.0);
		inOrder.verify(steamer).steam();
		verifyDrinkRelease(inOrder);
		verifyNewSession(inOrder);
	}

	private Recipe steamedMilk() {
		Recipe recipe = new Recipe();
		recipe.setName("Steamed milk");
		recipe.setPriceCents(50);
		recipe.setItem(Recipe.MILK, 150.0);
		recipe.setPlanSequence(Recipe.MILK);
		recipe.setMixSequence(Recipe.MILK);
		recipe.setSteamed(true);
		return recipe;
	}

}
