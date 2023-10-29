package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import ru.qoqqi.farmrancher.common.items.CoinItem;

public class ExchangerTrades {

	private static final ExchangerTrades INSTANCE = new ExchangerTrades();

	private static final int INFINITE_MAX_USES = 999;

	private MerchantOffers offers;

	public static void addOffers(MerchantOffers offers) {
		INSTANCE.offers = offers;
		INSTANCE.addOffers();
	}

	public void addOffers() {
		CoinItem.fromLowToHigh().forEach(this::addDoubleTierUpOffer);
		CoinItem.fromLowToHigh().forEach(this::addTierUpOffer);
		CoinItem.fromLowToHigh().forEach(this::addTierDownOffer);
	}

	public void addDoubleTierUpOffer(CoinItem coinItem) {
		if (coinItem.tier.up() == null) {
			return;
		}

		var resultItem = CoinItem.byTier(coinItem.tier.up());
		var exchangeRate = CoinItem.EXCHANGE_RATE;

		addDoubleOffer(coinItem, exchangeRate, resultItem, 2);
	}

	public void addTierUpOffer(CoinItem coinItem) {
		if (coinItem.tier.up() == null) {
			return;
		}

		var resultItem = CoinItem.byTier(coinItem.tier.up());
		var exchangeRate = CoinItem.EXCHANGE_RATE;

		addOffer(coinItem, exchangeRate, resultItem, 1);
	}

	public void addTierDownOffer(CoinItem coinItem) {
		if (coinItem.tier.down() == null) {
			return;
		}

		var resultItem = CoinItem.byTier(coinItem.tier.down());
		var exchangeRate = CoinItem.EXCHANGE_RATE;

		addOffer(coinItem, 1, resultItem, exchangeRate);
	}

	public void addDoubleOffer(Item from, int fromCount, Item to, int toCount) {
		offers.add(new MerchantOffer(
				new ItemStack(from, fromCount),
				new ItemStack(from, fromCount),
				new ItemStack(to, toCount),
				INFINITE_MAX_USES,
				0,
				1f
		));
	}

	public void addOffer(Item from, int fromCount, Item to, int toCount) {
		offers.add(new MerchantOffer(
				new ItemStack(from, fromCount),
				new ItemStack(to, toCount),
				INFINITE_MAX_USES,
				0,
				1f
		));
	}
}
