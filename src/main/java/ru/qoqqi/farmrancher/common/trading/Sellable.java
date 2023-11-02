package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

import java.util.function.Function;

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

	public PriceRange getStackPrice(ServerLevel level) {
		return priceFactory.apply(level);
	}

	public boolean hasValidPrice(ServerLevel level) {
		return getStackPrice(level).isValid();
	}

	public Price getInitialPrice(ServerLevel level) {
		var price = getStackPrice(level).getAverage();

		return new Price((int) Math.floor(price));
	}
}
