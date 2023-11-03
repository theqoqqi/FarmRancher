package ru.qoqqi.farmrancher.common.trading.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

import java.util.function.Function;

import ru.qoqqi.farmrancher.common.trading.util.PriceRange;

public class Sellable {

	public final Item item;

	private final Function<ServerLevel, PriceRange> priceFactory;

	public Sellable(Item item, Function<ServerLevel, PriceRange> priceFactory) {
		this.item = item;
		this.priceFactory = priceFactory;
	}

	public Item getItem() {
		return item;
	}

	public PriceRange getPrice(ServerLevel level) {
		return priceFactory.apply(level);
	}

	public boolean hasValidPrice(ServerLevel level) {
		return getPrice(level).isValid();
	}
}
