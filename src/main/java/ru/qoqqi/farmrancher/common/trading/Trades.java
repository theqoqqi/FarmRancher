package ru.qoqqi.farmrancher.common.trading;

public class Trades {

	public static final OfferListFactory EXCHANGER = blockEntity -> {
		return ExchangerTrades.createOffers();
	};

	public static final OfferListFactory MARKET = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Sellables.FRUITS.values());
	};

	public static final OfferListFactory BUFFET = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Sellables.BUFFET.values());
	};

	public static final OfferListFactory CAFETERIA = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Sellables.CAFETERIA.values());
	};

	public static final OfferListFactory CONFECTIONERY = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Sellables.CONFECTIONERY.values());
	};

	public static final OfferListFactory RESTAURANT = blockEntity -> {
		return MerchantOffersGenerator.generateOffers(blockEntity, Sellables.RESTAURANT.values());
	};

	public static final OfferListFactory WORKSHOP = blockEntity -> {
		return WorkshopTrades.createOffers();
	};
}
