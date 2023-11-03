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

	public PriceRange getPrice(ServerLevel level) {
		return priceFactory.apply(level);
	}

	public boolean hasValidPrice(ServerLevel level) {
		return getPrice(level).isValid();
	}

	public Price getInitialStackPrice(ServerLevel level) {
		//noinspection deprecation
		var maxStackSize = item.getMaxStackSize();
		var price = getPrice(level).getAverage();

		return new Price((int) Math.round(price * maxStackSize));
	}
}
