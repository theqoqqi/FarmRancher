package ru.qoqqi.farmrancher.common.trading;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import org.slf4j.Logger;

import ru.qoqqi.farmrancher.common.blocks.entities.TradingBlockEntity;
import ru.qoqqi.farmrancher.common.trading.economics.Economics;
import ru.qoqqi.farmrancher.common.trading.util.Price;
import ru.qoqqi.farmrancher.common.trading.util.Sellable;

public class MerchantOffersGenerator {

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final int INFINITE_MAX_USES = 999;

	private static final int COINS_FOR_EXP = 8;

	private final ServerLevel level;

	private final MerchantOffers offers;

	private final Economics economics;

	public MerchantOffersGenerator(ServerLevel level) {
		this.level = level;
		this.offers = new MerchantOffers();
		this.economics = Economics.getInstance(level);
	}

	private void addSellOffers(Sellable tradable) {

		if (!tradable.hasValidPrice(level)) {
			return;
		}

		if (!economics.isInDemand(tradable)) {
			return;
		}

		var stackPriceValue = economics.getStackPrice(tradable);
		var item = tradable.getItem();

		var largeOffer = addLargeOffer(item, stackPriceValue);

		if (largeOffer.getResult().getCount() > 1) {
			addSmallOffer(item, stackPriceValue);
		}
	}

	private MerchantOffer addLargeOffer(Item item, double stackPriceValue) {
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var stackPrice = new Price(Mth.floor(stackPriceValue));
		var offerPrice = stackPrice.floor();
		var priceRatio = offerPrice.getValue() / stackPriceValue;
		var stackSize = Mth.ceil(maxStackSize * priceRatio);

		return addOffer(item, stackSize, offerPrice);
	}

	private void addSmallOffer(Item item, double stackPriceValue) {
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var singleItemPrice = stackPriceValue / maxStackSize;
		var decimalStackSize = singleItemPrice >= 1 ? 1 : 1 / singleItemPrice;
		var stackSize = Mth.ceil(decimalStackSize);
		var offerPrice = new Price(Mth.floor(singleItemPrice * decimalStackSize + 0.001));

		addOffer(item, stackSize, offerPrice);
	}

	private MerchantOffer addOffer(Item item, int count, Price price) {
		var offer = new MerchantOffer(
				new ItemStack(item, count),
				price.asSingleStack(),
				INFINITE_MAX_USES,
				getExperienceForPrice(price.getValue()),
				1f
		);

		offers.add(offer);

		return offer;
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
