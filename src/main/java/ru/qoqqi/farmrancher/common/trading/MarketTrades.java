package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.plants.Plant;
import ru.qoqqi.farmrancher.common.plants.Plants;

public class MarketTrades {

	private static final MarketTrades INSTANCE = new MarketTrades();

	private static final int INFINITE_MAX_USES = 999;

	private static final int COINS_FOR_EXP = 8;

	private MerchantOffers offers;

	public static void addOffers(MerchantOffers offers) {
		INSTANCE.offers = offers;
		INSTANCE.addOffers();
	}

	public void addOffers() {
		Plants.getAll().forEach(this::addSellPlantOffers);
	}

	private void addSellPlantOffers(Plant plant) {
		//noinspection deprecation
		var maxStackSize = plant.fruitItem.getMaxStackSize();
		var price = plant.getInitialPrice();
		var priceStack = price.asSingleStack();
		var countOfCoins = priceStack.getCount();
		var minFruitCount = Mth.ceil((float) maxStackSize / countOfCoins);

		offers.add(new MerchantOffer(
				new ItemStack(plant.fruitItem, maxStackSize),
				priceStack,
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue()),
				1f
		));

		offers.add(new MerchantOffer(
				new ItemStack(plant.fruitItem, minFruitCount),
				new ItemStack(priceStack.getItem()),
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue() / countOfCoins),
				1f
		));
	}

	private int getExperienceForPrice(int price) {
		return roundExperience((float) price / COINS_FOR_EXP);
	}

	private static int roundExperience(float experience) {
		var rounded = Mth.floor(experience);
		var fractional = Mth.frac(experience);

		if (Math.random() < fractional) {
			++rounded;
		}

		return rounded;
	}
}
