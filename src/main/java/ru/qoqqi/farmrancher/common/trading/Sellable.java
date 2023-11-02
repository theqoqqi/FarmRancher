package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

import java.util.function.Function;

import ru.qoqqi.farmrancher.common.util.IntRange;

public class Sellable {

	public final Item item;

	private final Function<ServerLevel, IntRange> priceFactory;

	public Sellable(Item item, Function<ServerLevel, IntRange> priceFactory) {
		this.item = item;
		this.priceFactory = priceFactory;
	}

	public Item getItem() {
		return item;
	}

	public IntRange getStackPrice(ServerLevel level) {
		return priceFactory.apply(level);
	}

	public boolean hasValidPrice(ServerLevel level) {
		var stackPrice = getStackPrice(level);

		return stackPrice.min > 0 && stackPrice.max >= stackPrice.min;
	}

	public Price getInitialPrice(ServerLevel level) {
		var price = getStackPrice(level).getLowerAverage();

		return new Price(price);
	}
}
