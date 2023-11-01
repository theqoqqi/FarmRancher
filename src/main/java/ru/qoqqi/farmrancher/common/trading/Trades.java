package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.world.item.trading.MerchantOffers;

import java.util.function.Consumer;

import ru.qoqqi.farmrancher.common.dishes.Dishes;
import ru.qoqqi.farmrancher.common.fruits.Fruits;

public class Trades {

	public static final Consumer<MerchantOffers> EXCHANGER = ExchangerTrades::addOffers;

	public static final Consumer<MerchantOffers> MARKET = offers -> {
		MerchantOffersGenerator.generateOffers(offers, Fruits.getAll());
	};

	public static final Consumer<MerchantOffers> CAFETERIA = offers -> {
		MerchantOffersGenerator.generateOffers(offers, Dishes.getAll());
	};

	public static final Consumer<MerchantOffers> RESTAURANT = offers -> {
		MerchantOffersGenerator.generateOffers(offers, Dishes.getAll());
	};

	public static final Consumer<MerchantOffers> WORKSHOP = WorkshopTrades::addOffers;
}
