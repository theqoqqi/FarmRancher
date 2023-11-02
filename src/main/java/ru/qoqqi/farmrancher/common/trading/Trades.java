package ru.qoqqi.farmrancher.common.trading;

import ru.qoqqi.farmrancher.common.dishes.Dishes;
import ru.qoqqi.farmrancher.common.fruits.Fruits;

public class Trades {

	public static final OfferListFactory EXCHANGER = blockEntity -> {
		return ExchangerTrades.createOffers();
	};

	public static final OfferListFactory MARKET = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Fruits.getAll());
	};

	public static final OfferListFactory CAFETERIA = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Dishes.getAll());
	};

	public static final OfferListFactory RESTAURANT = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Dishes.getAll());
	};

	public static final OfferListFactory WORKSHOP = blockEntity -> {
		return WorkshopTrades.createOffers();
	};
}
