package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class MerchantOffersGenerator {

	private static final int INFINITE_MAX_USES = 999;

	private static final int COINS_FOR_EXP = 8;

	protected MerchantOffers offers;

	public MerchantOffersGenerator(MerchantOffers offers) {
		this.offers = offers;
	}

	protected void addSellOffers(ISellable tradable) {
		var item = tradable.getItem();
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var price = tradable.getInitialPrice();
		var priceStack = price.asSingleStack();
		var countOfCoins = priceStack.getCount();
		var minDishCount = Mth.ceil((float) maxStackSize / countOfCoins);

		offers.add(new MerchantOffer(
				new ItemStack(item, maxStackSize),
				priceStack,
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue()),
				1f
		));

		offers.add(new MerchantOffer(
				new ItemStack(item, minDishCount),
				new ItemStack(priceStack.getItem()),
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue()),
				1f
		));
	}

	protected int getExperienceForPrice(int price) {
		return roundExperience((float) price / COINS_FOR_EXP);
	}

	protected static int roundExperience(float experience) {
		var rounded = Mth.floor(experience);
		var fractional = Mth.frac(experience);

		if (Math.random() < fractional) {
			++rounded;
		}

		return rounded;
	}

	public static void generateOffers(MerchantOffers offers, Iterable<? extends ISellable> sellables) {
		var trades = new MerchantOffersGenerator(offers);

		sellables.forEach(trades::addSellOffers);
	}
}
