package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;

public class MerchantOffersGenerator {

	private static final int INFINITE_MAX_USES = 999;

	private static final int COINS_FOR_EXP = 8;

	protected ServerLevel level;

	protected MerchantOffers offers;

	public MerchantOffersGenerator(ServerLevel level) {
		this.level = level;
		this.offers = new MerchantOffers();
	}

	protected void addSellOffers(Sellable tradable) {

		if (!tradable.hasValidPrice(level)) {
			return;
		}

		var item = tradable.getItem();
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var price = tradable.getInitialPrice(level);
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

	public static MerchantOffers generateOffers(TradingBlockEntity blockEntity, Iterable<? extends Sellable> sellables) {
		var trades = new MerchantOffersGenerator((ServerLevel) blockEntity.getLevel());

		sellables.forEach(trades::addSellOffers);

		return trades.offers;
	}
}
