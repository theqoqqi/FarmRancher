package ru.qoqqi.farmrancher.common.trading;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;

import ru.qoqqi.farmrancher.common.util.IntRange;

public class Sellable {

	public final Item item;

	public final IntRange stackPrice;

	public Sellable(Item item, IntRange stackPrice) {
		this.item = item;
		this.stackPrice = stackPrice;
	}

	public Item getItem() {
		return item;
	}

	public Price getInitialPrice(ServerLevel level) {
		return new Price(stackPrice.getLowerAverage());
	}
}
