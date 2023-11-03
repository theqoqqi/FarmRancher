package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;

public class MerchantOffersGenerator {

	private static final int INFINITE_MAX_USES = 999;

	private static final int COINS_FOR_EXP = 8;

	private final ServerLevel level;

	private final MerchantOffers offers;

	public MerchantOffersGenerator(ServerLevel level) {
		this.level = level;
		this.offers = new MerchantOffers();
	}

	private void addSellOffers(Sellable tradable) {

		if (!tradable.hasValidPrice(level)) {
			return;
		}

		var stackPrice = tradable.getInitialStackPrice(level);

		if (!stackPrice.isValid()) {
			return;
		}

		var item = tradable.getItem();

		addLargeOffer(item, stackPrice);
		addSmallOffer(item, stackPrice);
	}

	private void addLargeOffer(Item item, Price stackPrice) {
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var offerPrice = stackPrice.floor();
		var priceRatio = (double) offerPrice.getValue() / stackPrice.getValue();
		var stackSize = Mth.ceil(maxStackSize * priceRatio);

		addOffer(item, stackSize, offerPrice);
	}

	private void addSmallOffer(Item item, Price stackPrice) {
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var singleItemPrice = (double) stackPrice.getValue() / maxStackSize;
		var decimalStackSize = singleItemPrice >= 1 ? 1 : 1 / singleItemPrice;
		var stackSize = Mth.ceil(decimalStackSize);
		var offerPrice = new Price(Mth.floor(singleItemPrice * decimalStackSize + 0.001));

		addOffer(item, stackSize, offerPrice);
	}

	private void addOffer(Item item, int count, Price price) {
		offers.add(new MerchantOffer(
				new ItemStack(item, count),
				price.asSingleStack(),
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue()),
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

	public static MerchantOffers generateOffers(TradingBlockEntity blockEntity, Iterable<? extends Sellable> sellables) {
		var trades = new MerchantOffersGenerator((ServerLevel) blockEntity.getLevel());

		sellables.forEach(trades::addSellOffers);

		return trades.offers;
	}
}
